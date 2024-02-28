package com.roseno.curbcrime.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationGeocoder {
    private final String TAG = "LocationGeocoder";
    
    /**
     * 주소 검색
     * @param context
     * @param latitude      위도
     * @param longitude     경도
     * @return
     * @throws IOException
     */
    public static String reverseGeocode(final Context context, double latitude, double longitude) throws IOException {
        return reverseGeocode(context, latitude, longitude, null);
    }

    public static String reverseGeocode(final Context context, double latitude, double longitude, String defaultAddress) throws IOException {
        String address = defaultAddress;

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

        if (!addresses.isEmpty()) {
            Address firstAddress = addresses.get(0);

            address = firstAddress.getAddressLine(0);
        }

        return address;
    }
}
