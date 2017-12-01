package sredoc.smart_traffic;

/**
 * Created by rishabhshukla on 10/10/17.
 */

public class Singleton {


    public static final String TAG = Singleton.class.getSimpleName();

    private static Singleton singleton;
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;

    public static synchronized Singleton getInstance() {
        if (singleton == null) //if none created
            singleton = new Singleton(); //create rate_one
        return singleton; //return it
    }

    public double getCurrentLatitude() {return currentLatitude;}

    public void setCurrentLatitude(double currentLatitude) {this.currentLatitude = currentLatitude;}

    public double getCurrentLongitude() {return currentLongitude;}

    public void setCurrentLongitude(double currentLongitude) {this.currentLongitude = currentLongitude;}

}
