package com.androiditgroup.loclook.v2.utils;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.ui.general.MainActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by OS1 on 25.02.2017.
 */
public class DefineUserLocationName {

    private LocationManager mLocationManager;

    public DefineUserLocationName(LocationManager locationManager) {
        mLocationManager = locationManager;
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Constants.LOCATION_REQUEST_INTERVAL, 10, mLocationListener);
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
                setUserLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }

        @Override
        public void onProviderEnabled(String provider) {
            setUserLocation(mLocationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onProviderDisabled(String provider) { }
    };

    private void setUserLocation(Location location) {
        if((LocLookApp.user != null) && (location != null)) {
            LocLookApp.user.setLocation(location);

            // получаем данные местности
            ArrayList<String> locationDataList = getLocationData(location);
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < locationDataList.size(); i++) {
                if (i == 0) {
                    LocLookApp.user.setRegionName(locationDataList.get(i));
                    sb.append(locationDataList.get(i));
                } else if (i == 1) {
                    LocLookApp.user.setStreetName(locationDataList.get(i));
                    sb.append(", ");
                    sb.append(locationDataList.get(i));
                }
            }
            MainActivity.mNavigationUserLocationName.setText(sb.toString());
        }
    }

    private static ArrayList<String> getLocationData(Location location) {
        // создаем список для частей данных местоположения определенных по точке на карте
        ArrayList<String> list = new ArrayList<>();

        Geocoder geoCoder = new Geocoder(LocLookApp.context, Locale.getDefault());

        try {
            // получаем данные на основании заданных координат
            List<Address> addresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            // если данные получены
            if (addresses.size() > 0) {

                // сформировать результат в зависимости от полученного количества фрагментов названия объекта
                switch(addresses.get(0).getMaxAddressLineIndex()) {

                    case 3:
                        // вернуть названия города и улицы
                        list.add(addresses.get(0).getAddressLine(1));
                        list.add(addresses.get(0).getAddressLine(0));
                        break;
                    case 4:
                        // вернуть названия города и улицы
                        list.add(addresses.get(0).getAddressLine(1));
                        list.add(addresses.get(0).getAddressLine(0));
                        break;
                    default:
                        // вернуть название города
                        list.add(addresses.get(0).getAddressLine(0));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // возвращаем результат
        return list;
    }
}
