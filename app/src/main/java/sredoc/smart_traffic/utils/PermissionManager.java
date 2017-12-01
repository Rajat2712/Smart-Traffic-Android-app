package sredoc.smart_traffic.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.Arrays;
import java.util.List;

/**
 * Created by aditya on 22/03/17.
 */
public class PermissionManager {

    private static final int MAX_PERMISSION_REQUEST_ATTEMPT = 3;
    private static int permissionRequestAttempt = 0;

    /**
     * Check if we got all permission
     *
     * @param context
     * @param perms
     * @return
     */
    public static boolean hasPermissions(Context context, String... perms) {
        for (String perm : perms) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED);
            if (!hasPerm) {
                return false;
            }
        }
        return true;
    }

    public static void requestPermissions(PermissionListener listener, Object object, int requestCode, final String... perms) {
        if (listener == null) return;
        if (permissionRequestAttempt < MAX_PERMISSION_REQUEST_ATTEMPT) {
            permissionRequestAttempt++;
            executePermissionsRequest(object, perms, requestCode);
        } else {
            listener.onPermissionRequestRejected(requestCode);
        }
    }

    /**
     * Permission rationale dialog
     *
     * @param object
     * @param perm
     * @return
     */
    private static boolean shouldShowRequestPermissionRationale(Object object, String perm) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, perm);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else {
            return false;
        }
    }

    /**
     * Make permission request
     *
     * @param object
     * @param perms
     * @param requestCode
     */
    private static void executePermissionsRequest(Object object, String[] perms, int requestCode) {
        checkCallingObjectSuitability(object);
        if (object instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) object, perms, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(perms, requestCode);
        }
    }

    public static void onRequestPermissionsResult(Object object, PermissionListener listener, int requestCode,
                                                  @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (listener == null) return;
        boolean allPermissionGranted = true;
        String missingPermission = "";
        if (grantResults.length == 0) allPermissionGranted = false;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                missingPermission = permissions[i];
                allPermissionGranted = false;
                break;
            }
        }
        if (allPermissionGranted) {
            permissionRequestAttempt = 0;
            listener.onPermissionsGranted(requestCode, Arrays.asList(permissions));
        } else if (!missingPermission.isEmpty() && shouldShowRequestPermissionRationale(object, missingPermission)) {
            requestPermissions(listener, object, requestCode, permissions);
        } else {
            listener.onPermissionRequestRejected(requestCode);
        }
    }

    /**
     * Check if called from within fragment or activity
     *
     * @param object
     */
    private static void checkCallingObjectSuitability(Object object) {
        // Make sure Object is an Activity or Fragment
        if (!((object instanceof Fragment) || (object instanceof Activity))) {
            throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
        }
    }

    public interface PermissionListener {

        void onPermissionsGranted(int requestCode, List<String> perms);

        void onPermissionRequestRejected(int requestCode);
    }
}