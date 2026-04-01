package com.filip.safealert;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;

public class LocationHelper {
    @SuppressLint("MissingPermission")
    public static String obtineLinkLocatie(LocationManager lm) {
        try {
            Location l = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (l == null) {
                l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (l != null) {
                return "http://maps.google.com/?q=" + l.getLatitude() + "," + l.getLongitude();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Locatie nedetectata";
    }
}