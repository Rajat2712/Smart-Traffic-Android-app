package sredoc.smart_traffic.utils;

import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rishabhshukla on 10/10/17.
 */

public class CommonUtils {
    public static final String APP_UBER = "com.ubercab";
    public static final String APP_GOOGLE_MAP = "com.google.android.apps.maps";
    public static final String APP_GOOGLE_PLAY_SERVICES = "com.google.android.gms";
    public static final String APP_LINKED_IN = "com.linkedin.android";
    public static final String APP_TWITTER = "com.twitter.android";
    public static final String APP_FACEBOOK = "com.facebook.katana";
    public static final String APP_INSTAGRAM = "com.instagram.android";

    /**
     * Returns true if the list is null or 0-length.
     *
     * @param list the List to be examined
     * @return true if list is null or zero length
     */
    public static <T> boolean isEmptyList(@Nullable List<T> list) {
        return list == null || list.size() == 0;
    }

    /**
     * Decode polyline string to list of lat-lng
     * @param encoded
     * @return
     */
    public static ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }

}
