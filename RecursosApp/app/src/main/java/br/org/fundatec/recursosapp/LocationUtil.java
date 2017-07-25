package br.org.fundatec.recursosapp;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by guto_ on 17/10/2016.
 */

public class LocationUtil {



    public static boolean hasLocationEnabled(Context context) {
        try {
            LocationManager locationManager = (LocationManager) context
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                return false;
            }

            return true;

        }catch (Exception e) {
            return false;
        }
    }


    public static Location getLastKnownLocation(Context context) {
        try {
            LocationManager mLocationManager = (LocationManager) context.getApplicationContext().getSystemService(LOCATION_SERVICE);
            List<String> providers = mLocationManager.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
            return bestLocation;
        }catch (SecurityException e){
            e.printStackTrace();
            return null;
        }
    }


}
