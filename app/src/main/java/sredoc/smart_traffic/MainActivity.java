package sredoc.smart_traffic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import sredoc.smart_traffic.data.GenericRetrofitManager;
import sredoc.smart_traffic.data.RequestKeys;
import sredoc.smart_traffic.data.model.Incidents;
import sredoc.smart_traffic.utils.CommonUtils;
import sredoc.smart_traffic.utils.DateUtils;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final int PLACE_PICKER_REQUEST = 1;
    public double valueLat = 0;
    public double valueLng = 0;
    public long valueTime = 0;

    @BindView(R.id.tvEventLocation) TextView tvEventLocation;
    @BindView(R.id.tvEventTime) TextView tvEventTime;

    @OnClick({R.id.llEventLocation, R.id.tvEventTime, R.id.cvSignIn})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.llEventLocation:
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tvEventTime:
                showTimePicker();
                break;
            case R.id.cvSignIn:
                LatLng source = new LatLng(39.95, -105.25);
                LatLng destination = new LatLng(43.05, -101.50);
                fetchObstructionData(source, destination);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Start map view
     */
    public void switchToMapView() {
        startActivity(new Intent(this, MapActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fetchObstructionData(LatLng sourceLatLng, LatLng destinationLatLng) {
        Map<String, Object> params = new HashMap<>();
        params.put(RequestKeys.MAP_API_KEY, "7FxRtqptMKlRfDuGmuTyE9s9Xv1RJOx7");
        params.put(RequestKeys.MAP_BOUNDING_BOX, sourceLatLng.latitude + "," + sourceLatLng.longitude+","+destinationLatLng.latitude+","+destinationLatLng.longitude);
//        params.put(RequestKeys.MAP_FILTERS, false);
        Disposable disposable = GenericRetrofitManager
                .getApiService("http://www.mapquestapi.com/").getIncidents(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Response<Incidents>>() {
                    @Override public void onNext(Response<Incidents> response) {
                        Incidents responseList = response.body();
                        if (responseList == null) return;
                        else {
                            List<Incidents.Incident> incidents = responseList.getIncidents();
                            if (CommonUtils.isEmptyList(incidents)) return;
                            else {
                                Log.d(TAG, "onNext: list "+incidents);
                                Intent i = new Intent(MainActivity.this, MapActivity.class);
                                i.putParcelableArrayListExtra("LIST_INCIDENTS", (ArrayList<? extends Parcelable>) incidents);
                                startActivity(i);
                            }
                        }
                    }

                    @Override public void onError(Throwable throwable) {
                        Log.e(TAG, String.valueOf(throwable));}

                    @Override public void onComplete() {}
                });
        compositeDisposable.add(disposable);
    }

    @Override public void onDestroy() {
        if (compositeDisposable != null) compositeDisposable.clear();
        super.onDestroy();
    }

    @Override @SuppressLint("NewApi") protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                valueLat = place.getLatLng().latitude;
                valueLng = place.getLatLng().longitude;
                tvEventLocation.setText(place.getAddress());
            }
        }
    }

    private TimePickerDialog.OnTimeSetListener onTimeSetListener = (view, hourOfDay, minute, second) -> {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        valueTime = calendar.getTimeInMillis() / 1000;
        tvEventTime.setText(DateUtils.changeDateFormat(calendar, DateUtils.TIME_12_HOUR_FORMAT));
    };

    /**
     * Open time picker
     */
    private void showTimePicker() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                onTimeSetListener,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false);
        tpd.setMinTime(new Timepoint(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE)));
        tpd.show(getFragmentManager(), "Select time of your journey");
    }
}
