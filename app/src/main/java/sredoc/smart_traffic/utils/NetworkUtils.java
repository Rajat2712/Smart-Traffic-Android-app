package sredoc.smart_traffic.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * Created by aditya on 21/02/17.
 */

public class NetworkUtils {

    public static final int CODE_200 = 200;
    public static final int CODE_400 = 400;
    public static final int CODE_401 = 401;
    public static final int CODE_402 = 402;
    public static final int CODE_403 = 403;
    public static final int CODE_404 = 404;
    public static final int CODE_405 = 405;
    public static final int CODE_406 = 406;
    public static final int CODE_407 = 407;
    public static final int CODE_408 = 408;
    public static final int CODE_409 = 409;
    public static final int CODE_410 = 410;
    public static final int CODE_411 = 411;
    public static final int CODE_412 = 412;
    public static final int CODE_413 = 413;
    public static final int CODE_414 = 414;
    public static final int CODE_415 = 415;
    public static final int CODE_416 = 416;
    public static final int CODE_417 = 417;

    public static final int CODE_500 = 500;
    public static final int CODE_501 = 501;
    public static final int CODE_502 = 502;
    public static final int CODE_503 = 503;
    public static final int CODE_504 = 504;
    public static final int CODE_505 = 505;

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void showInternetConnectivityDialog(final Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        AlertDialog alertDialog;
        alertDialogBuilder.setTitle("No Internet Connectivity Found");
        alertDialogBuilder
                .setMessage("Move to internet connection settings?")
                .setCancelable(false)
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss())
                .setPositiveButton("Yes", (dialog, id) -> {
                    dialog.dismiss();
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    context.startActivity(intent);
                });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static String getErrorMessage(int statusCode) {
        switch (statusCode) {
            case CODE_400:
                return "Bad Request";
            case CODE_401:
                return "Invalid Credentials";
            case CODE_402:
                return "Payment Required";
            case CODE_403:
                return "Forbidden";
            case CODE_404:
                return "Not Found";
            case CODE_405:
                return "Method Not Allowed";
            case CODE_406:
                return "Not Acceptable";
            case CODE_407:
                return "Proxy Authentication Required";
            case CODE_408:
                return "Request Timeout";
            case CODE_409:
                return "Conflict";
            case CODE_410:
                return "Gone";
            case CODE_411:
                return "Length Required";
            case CODE_412:
                return "Precondition Failed";
            case CODE_413:
                return "Request Entity Too Large";
            case CODE_414:
                return "Request-URI Too Long";
            case CODE_415:
                return "Unsupported Media Type";
            case CODE_416:
                return "Requested Range Not Satisfiable";
            case CODE_417:
                return "Expectation Failed";

            case CODE_500:
                return "Internal Server Error";
            case CODE_501:
                return "Not Implemented";
            case CODE_502:
                return "Bad Gateway";
            case CODE_503:
                return "Service Unavailable";
            case CODE_504:
                return "Gateway Timeout";
            case CODE_505:
                return "HTTP Version Not Supported";
            default:
                return "Server Error";
        }
    }
}
