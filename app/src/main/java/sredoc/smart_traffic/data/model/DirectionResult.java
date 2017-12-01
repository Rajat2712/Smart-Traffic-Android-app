package sredoc.smart_traffic.data.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import sredoc.smart_traffic.utils.CommonUtils;

public class DirectionResult {

    @SerializedName("routes") private List<Route> routes;

    public List<Route> getRoutes() {return routes;}

    public class Route {

        @SerializedName("overview_polyline") private Legs.Steps.OverviewPolyLine overviewPolyLine;
        @SerializedName("legs") private List<Legs> legs;

        public Legs.Steps.OverviewPolyLine getOverviewPolyLine() {return overviewPolyLine;}

        public List<Legs> getLegs() {return legs;}

        public class Legs {

            @SerializedName("steps") private List<Steps> steps;

            public List<Steps> getSteps() { return steps;}

            public class Steps {

                @SerializedName("start_location") private Location startLocation;
                @SerializedName("end_location") private Location endLocation;
                @SerializedName("polyline") private OverviewPolyLine polyline;

                public Location getStartLocation() {return startLocation;}

                public Location getEndLocation() {return endLocation;}

                public OverviewPolyLine getPolyline() {return polyline;}

                public class OverviewPolyLine {

                    @SerializedName("points") public String points;

                    public String getPoints() {return points;}
                }

                public class Location {

                    @SerializedName("lat") private double lat;
                    @SerializedName("lng") private double lng;

                    public double getLat() {return lat;}

                    public double getLng() {return lng;}
                }
            }
        }
    }

    public List<LatLng> getLatLngList() {
        ArrayList<LatLng> latLngList = new ArrayList<LatLng>();
        if (getRoutes().size() > 0) {
            ArrayList<LatLng> decodelist;
            Route routeA = getRoutes().get(0);
            if (routeA.getLegs().size() > 0) {
                List<Route.Legs.Steps> steps = routeA.getLegs().get(0).getSteps();
                Route.Legs.Steps step;
                Route.Legs.Steps.Location location;
                String polyline;
                for (int i = 0; i < steps.size(); i++) {
                    step = steps.get(i);
                    location = step.getStartLocation();
                    latLngList.add(new LatLng(location.getLat(), location.getLng()));
                    polyline = step.getPolyline().getPoints();
                    decodelist = CommonUtils.decodePoly(polyline);
                    latLngList.addAll(decodelist);
                    location = step.getEndLocation();
                    latLngList.add(new LatLng(location.getLat(), location.getLng()));
                }
            }
        }
        return latLngList;
    }

}