package sredoc.smart_traffic.data;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import sredoc.smart_traffic.data.model.DirectionResult;
import sredoc.smart_traffic.data.model.Incidents;

/**
 * Created by rishabhshukla on 10/10/17.
 */

public interface ApiHelper {

    @GET("/maps/api/directions/json")
    Observable<Response<DirectionResult>> getDirections(@QueryMap Map<String, Object> params);
    @GET("/traffic/v2/incidents")
    Observable<Response<Incidents>> getIncidents(@QueryMap Map<String, Object> params);
}
