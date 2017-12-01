package sredoc.smart_traffic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import sredoc.smart_traffic.data.GenericRetrofitManager;
import sredoc.smart_traffic.data.RequestKeys;
import sredoc.smart_traffic.data.model.DirectionResult;
import sredoc.smart_traffic.data.model.Incidents;
import sredoc.smart_traffic.utils.CommonUtils;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.fromResource;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, MapHelper.CurrentLocationFetchListener {

    public static final String TAG = MapActivity.class.getSimpleName();
    private GoogleMap googleMap;
    private Polyline line;
    private List<Incidents.Incident> incidents;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private HashMap<Integer, Incidents.Incident> map = new HashMap<>();

    private GoogleMap.OnMarkerClickListener markerClickListener = marker -> {
        Object tag = marker.getTag();
            if(map.containsKey(Integer.valueOf((String) tag))){
                String title = null;
                Incidents.Incident incident = map.get(tag);
                if(incident.getType() == 1){
                    title = "Construction";
                }else if(incident.getType() == 2){
                    title = "Event";
                }else if(incident.getType() == 3){
                    title = "Congestion";
                }else{
                    title = "Accident";
                }
                marker.setTitle(title);
                googleMap.setContentDescription(incident.getFullDesc() + " Severity: "+incident.getSeverity());
            }
        return true;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void startFetchPathAPI(LatLng sourceLatLng, LatLng destinationLatLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 15));
        Map<String, Object> params = new HashMap<>();
        params.put(RequestKeys.GOOGLE_MAP_ORIGIN, sourceLatLng.latitude + "," + sourceLatLng.longitude);
        params.put(RequestKeys.GOOGLE_MAP_DESTINATION, destinationLatLng.latitude + "," + destinationLatLng.longitude);
        params.put(RequestKeys.GOOGLE_MAP_SENSOR, false);
        params.put(RequestKeys.GOOGLE_MAP_KEY, BuildConfig.GOOGLE_MAPS_DIRECTIONS_API_KEY);
        Disposable disposable = GenericRetrofitManager
                .getApiService("https://maps.googleapis.com/").getDirections(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Response<DirectionResult>>() {
                    @Override public void onNext(Response<DirectionResult> response) {
                        DirectionResult directionResult = response.body();
                        if (directionResult == null) return;
                        else {
                            List<LatLng> latLngList = directionResult.getLatLngList();
                            if (CommonUtils.isEmptyList(latLngList)) return;
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            PolylineOptions polyLineOptions = new PolylineOptions()
                                    .width(12)
                                    .color(ContextCompat.getColor(getApplicationContext(), R.color.accent));
                            Log.d(TAG, "onNext: size: "+latLngList.size()+"routes: "+directionResult.getRoutes().size());
                            for (LatLng latLng : latLngList) {
                                polyLineOptions.add(latLng);
                                builder.include(latLng);
                            }
                            drawPath(polyLineOptions, builder.build());
                        }
                    }

                    @Override public void onError(Throwable throwable) {
                        Log.e(TAG, String.valueOf(throwable));}

                    @Override public void onComplete() {}
                });
        compositeDisposable.add(disposable);
    }

    public void drawPath(PolylineOptions polylineOptions, LatLngBounds bounds) {
        if (line != null) line.remove();
        line = googleMap.addPolyline(polylineOptions);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.35);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        googleMap.animateCamera(cu);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng source = new LatLng(39.95, -105.25);
        LatLng destination = new LatLng(39.75, -105.50);

        googleMap.addMarker(new MarkerOptions()
                .position(source)
                .icon(fromResource(R.drawable.ic_current_location)))
                .setTag(null);
        googleMap.addMarker(new MarkerOptions()
                .position(destination)
                .icon(fromResource(R.drawable.ic_dest)))
                .setTag(null);
        googleMap.setTrafficEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(source, 15.5f));

        googleMap.setOnMarkerClickListener(markerClickListener);
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        startFetchPathAPI(source, destination);
        fetchObstructions();
    }

    private void fetchObstructions(){
        Intent i = getIntent();
        incidents = i.getParcelableArrayListExtra("LIST_INCIDENTS");
        if(!CommonUtils.isEmptyList(incidents)){
            for(Incidents.Incident incident: incidents){
                LatLng latLng = new LatLng(incident.getLat(), incident.getLng());
                map.put(incident.getId(), incident);
                if(googleMap!=null) {
                    googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .icon(fromResource(R.drawable.obstruction)))
                            .setTag(String.valueOf(incident.getId()));
                }
            }
        }
        Log.d(TAG, "onCreate: MAP: "+incidents);
    }

//    public Bitmap getBitmapFromURL(String imageUrl) {
//        Bitmap myBitmap;
//            Runnable r = new Runnable() {
//                @Override public void run() {
//                    try {
//                    URL url = new URL(imageUrl);
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    connection.setDoInput(true);
//                    connection.connect();
//                    InputStream input = connection.getInputStream();
//                     myBitmap = BitmapFactory.decodeStream(input);
//                    return myBitmap;
//                }catch (IOException e) {
//                        e.printStackTrace();
//                        return null;
//                    }
//            }
//        }
//    }

    private void drawCurrentLocationMarker(LatLng latLng) {
        if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(fromResource(R.drawable.ic_current_location)))
                    .setTag(null);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.5f));
        }
    }

    @Override public void onCurrentLocationFetched(LatLng latLng) {
        drawCurrentLocationMarker(latLng);
    }
    @Override public void onDestroy() {
        if (compositeDisposable != null) compositeDisposable.clear();
        super.onDestroy();
    }
}
