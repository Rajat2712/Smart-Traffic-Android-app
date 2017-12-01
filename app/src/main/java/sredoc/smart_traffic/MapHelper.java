package sredoc.smart_traffic;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import butterknife.ButterKnife;
import sredoc.smart_traffic.base.BaseLocationActivity;

public class MapHelper extends BaseLocationActivity implements BaseLocationActivity.LocationUpdateListener {

    public final static String TAG = MapHelper.class.getSimpleName();
    private CurrentLocationFetchListener currentLocationFetchListener;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initComponents();
    }

    /**
     * Init UI Components
     */
    private void initComponents() {
        setContext(MapHelper.this);
        setUnbinder(ButterKnife.bind(this));
    }

    public void getLocation() {
        fetchLocation(this);
    }

    @Override public void onSuccess(Location location) {
        if (location == null) {
            fetchLocation(this);
        } else {
            Singleton.getInstance().setCurrentLatitude(location.getLatitude());
            Singleton.getInstance().setCurrentLongitude(location.getLongitude());
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            currentLocationFetchListener.onCurrentLocationFetched(latLng);
        }
    }

    @Override public void onFail(int errorCode) {

    }

    @Override public void onDestroy() {
        super.onDestroy();
    }

    public interface CurrentLocationFetchListener {
        void onCurrentLocationFetched(LatLng latLng);
    }
}
