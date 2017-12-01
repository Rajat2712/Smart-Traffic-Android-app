package sredoc.smart_traffic.base;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.List;

import sredoc.smart_traffic.utils.PermissionManager;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;

public abstract class BaseLocationActivity extends BaseActivity implements GoogleApiClient
        .ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String TAG = BaseLocationActivity.class.getSimpleName();
    public static final int LOCATION_ERROR_PERMISSION_DENIED = 101;
    public static final int LOCATION_ERROR_PLAY_SERVICE_NOT_AVAILABLE = 102;
    public static final int LOCATION_ERROR_UNAVAILABLE = 103;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private static final long LOCATION_TIME_INTERVAL = 180000;
    protected static final int REQUEST_CHECK_SETTINGS = 1;
    private boolean isLocationRequested = false;
    private LocationUpdateListener locationUpdateListener;
    private final PermissionManager.PermissionListener permissionListener = new PermissionManager.PermissionListener() {

        @Override public void onPermissionsGranted(int requestCode, List<String> perms) {
            switch (requestCode) {
                case REQUEST_CODE_LOCATION:
                    fetchLocation(locationUpdateListener);
                    break;
            }
        }

        @Override
        public void onPermissionRequestRejected(int requestCode) {
            if (locationUpdateListener == null) return;
            locationUpdateListener.onFail(LOCATION_ERROR_PERMISSION_DENIED);
        }
    };

    /**
     * Call this method to init location updates
     */
    public void fetchLocation(LocationUpdateListener locationUpdateListener) {
        this.locationUpdateListener = locationUpdateListener;
        isLocationRequested = true;
        if (PermissionManager.hasPermissions(context, new String[]{
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION
        })) {
            requestLocationServices();
        } else {
            requestPermission(REQUEST_CODE_LOCATION, permissionListener);
        }
    }

    /**
     * Check if google play services are enabled or not
     *
     * @param activity
     * @return
     */
    public static boolean checkPlayServices(Activity activity) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(activity);
        return result == ConnectionResult.SUCCESS;
    }

    /**
     * Call google services for requesting location
     */
    public void requestLocationServices() {
        if (checkPlayServices(this)) {
            if (this.googleApiClient == null) {
                this.googleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
            }
            if (this.googleApiClient.isConnected()) {
                this.googleApiClient.disconnect();
                this.googleApiClient.connect();
                return;
            }
            this.googleApiClient.connect();
        } else {
            if (locationUpdateListener == null) return;
            locationUpdateListener.onFail(LOCATION_ERROR_PLAY_SERVICE_NOT_AVAILABLE);
        }
    }

    @Override public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.locationRequest = LocationRequest.create();
        this.locationRequest.setPriority(PRIORITY_BALANCED_POWER_ACCURACY);
        this.locationRequest.setInterval(LOCATION_TIME_INTERVAL);
        this.locationRequest.setFastestInterval(LOCATION_TIME_INTERVAL);
        this.locationRequest.setSmallestDisplacement(500.0f);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.
                checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    if (googleApiClient.isConnected()) {
                        LocationServices.FusedLocationApi.requestLocationUpdates(this.googleApiClient, this.locationRequest, this);
                    }
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        Log.d(TAG, e.getLocalizedMessage());
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    break;
            }
        });
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        requestLocationServices();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }

    @Override public void onConnectionSuspended(int i) {
        this.googleApiClient.connect();
    }

    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        this.googleApiClient.connect();
    }

    @Override public void onLocationChanged(Location location) {
        if (isLocationRequested) {
            if (locationUpdateListener == null) return;
            if (location == null) {
                locationUpdateListener.onFail(LOCATION_ERROR_UNAVAILABLE);
            } else {
                isLocationRequested = false;
                locationUpdateListener.onSuccess(location);
            }
        }
    }

    public interface LocationUpdateListener {
        void onSuccess(Location location);
        void onFail(int errorCode);
    }

    @Override protected void onPause() {
        if (googleApiClient != null) googleApiClient.disconnect();
        super.onPause();
    }
}
