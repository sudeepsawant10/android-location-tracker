package com.example.locationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.locationtracker.db.DatabaseHelper;
import com.example.locationtracker.db.MyLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 30;
    public static final int PERMISSION_FINE_LOCATION = 99;
    private static final String TAG = MainActivity.class.getSimpleName();
    // Google API for location update
    FusedLocationProviderClient fusedLocationProviderClient;
    // Location Request is a config file for FusedLocationClient
    LocationRequest locationRequest;
    // for the fused client to get location updates
    LocationCallback locationCallback;

    // current location
    Location currentLocation;
    // saved locations
    List<Location> savedLocations;

    DatabaseHelper databaseHelper;
    List<MyLocation> myLocationsList;

    Context context = this;
    TextView tv_lat, tv_lon, tv_altitude, tv_accuracy;
    TextView tv_speed, tv_sensor, tv_updates, tv_address, tv_wayPointsCount;
    Switch sw_locationsupdates, sw_gps;

    Button btn_showWayPointList, btn_newWayPoint, btn_showMap, btn_showLocationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_speed = findViewById(R.id.tv_speed);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);
        tv_address = findViewById(R.id.tv_address);
        sw_locationsupdates = findViewById(R.id.sw_locationsupdates);
        sw_gps = findViewById(R.id.sw_gps);
        tv_wayPointsCount = findViewById(R.id.tv_wayPointsCount);
        btn_showLocationList = findViewById(R.id.btn_showLocationList);

        btn_newWayPoint = findViewById(R.id.btn_newWayPoint);
        btn_showWayPointList = findViewById(R.id.btn_showWayPointList);
        btn_showMap = findViewById(R.id.btn_showMap);

        // set all properties of Location Request
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);

        locationRequest.setFastestInterval(FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        databaseHelper = DatabaseHelper.getDB(context);
        myLocationsList = databaseHelper.myLocationDao().getLocations();



        // event that is triggered whenever the update interval is met
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // save the new location
                Location location = locationResult.getLastLocation();
//                Log.d(TAG, "Location");
//                Log.d(TAG, "Location Worker printing last location LocationResult: $$$$$$$$$$$$$$$$$$");
//                Log.d(TAG, "Location Worker Last Location address===> : " + location.toString());
//                Log.d(TAG, "Location Worker Last Location lat===> : " + location.getLatitude());
//                Log.d(TAG, "Location Worker Last Location lon===> : " + location.getLongitude());
//                String lastLocationAddress = getAddressLine(location);
//                Log.d(TAG, "Locaiton Worker last location Address Line ^^^^^^^^^^^^^^^^^^^ : " + lastLocationAddress);
//                Log.d(TAG, "Location Worker");
//                Log.d(TAG, "Latest Location");
//                int latestLocation = locationResult.getLocations().size() - 1;
//                double latitude = locationResult.getLocations().get(latestLocation).getLatitude();
//                double longitude = locationResult.getLocations().get(latestLocation).getLongitude();
//                Location latestLocationObj = locationResult.getLocations().get(Integer.parseInt(String.valueOf(latestLocation)));
//                String latestLocationAddress = getAddressLine(latestLocationObj);
//                Log.d(TAG, "Location Worker Printing latest Location lat ===> : " + latitude);
//                Log.d(TAG, "Location Worker Printing latest Location lon ===> : " + longitude);
//                Log.d(TAG, "^^^^^^^^^^ Location Worker latest Location Address line ^^^^^^^^^^^^^^^^^^ : " + latestLocationAddress);
//                // Format the current date and time
////                String dateTime = getCurrentDateTime();
                String lat = String.valueOf(location.getLatitude());
                String lon = String.valueOf(location.getLongitude());
                String dateTime = getCurrentDateTime();
                String address = getAddressLine(location);
                MyLocation myLocation = new MyLocation(lat, lon, address, dateTime);
//                databaseHelper.myLocationDao().addLocation(myLocation);
                Toast.makeText(context, "adding by callback", Toast.LENGTH_SHORT).show();
                updateUIValues(location);
            }
        };


        btn_newWayPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the gps location
                // add into global list
                MyApplication myApplication = (MyApplication) getApplicationContext();
                savedLocations = myApplication.getMyLocations();
                savedLocations.add(currentLocation);

                String lat = String.valueOf(currentLocation.getLatitude());
                String lon = String.valueOf(currentLocation.getLongitude());
                String dateTime = getCurrentDateTime();
                String address = getAddressLine(currentLocation);

                databaseHelper.myLocationDao().addLocation(new MyLocation(lat, lon, address, dateTime));
            }
        });


        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_gps.isChecked()) {
                    // most accurate use gps
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    tv_sensor.setText("Using GPS Sensors");
                } else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    tv_sensor.setText("Using Tower + WIFI");
                }
            }
        });

        sw_locationsupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_locationsupdates.isChecked()) {
                    // turn on location tracking
                    startLocationUpdates();
                } else {
                    // turn off tracking
                    stopLocationUpdates();
                }
            }
        });

        btn_showWayPointList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowSavedLocationList.class);
                startActivity(intent);
            }
        });

        btn_showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        btn_showLocationList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowDbLocations.class);
                startActivity(intent);
            }
        });

        updateGPS();
    }

    private void stopLocationUpdates() {
        tv_updates.setText("Location is not being tracked");
        tv_lat.setText("Not tracking location");
        tv_lon.setText("Not tracking location");
        tv_accuracy.setText("Not tracking location");
        tv_altitude.setText("Not tracking location");
        tv_address.setText("Not tracking location");
        tv_sensor.setText("Not tracking location");
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void startLocationUpdates() {
        tv_updates.setText("Location is being tracked");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Main", "Permissions are required");
        } else {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            updateGPS();
        }
    }

    // get the current location from fused client
    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // user provided permissions
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // put the location values
                    updateUIValues(location);
                    String lat = String.valueOf(location.getLatitude());
                    String lon = String.valueOf(location.getLongitude());
                    String dateTime = getCurrentDateTime();
                    String address = getAddressLine(location);
                    MyLocation myLocation = new MyLocation(lat, lon, address, dateTime);
                    databaseHelper.myLocationDao().addLocation(myLocation);
                    Toast.makeText(context, "Location added on checked", Toast.LENGTH_SHORT).show();
                    currentLocation = location;
                }
            });
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
            }
        }
    }

    private void updateUIValues(Location location) {
        // update the text fields with new location
        String lat = String.valueOf(location.getLatitude());
        String lon = String.valueOf(location.getLongitude());
        tv_lat.setText(lat);
        tv_lon.setText(lon);
//        tv_accuracy.setText(String.valueOf(location.getAccuracy()));
//        Log.d("MainActivity", "Main Activity locaiton = " + location);


        if (location.hasAccuracy()) {
            tv_altitude.setText(String.valueOf(location.getAltitude()));
        } else {
            tv_altitude.setText("Not Available");
        }

        if (location.hasAltitude()) {
            tv_altitude.setText(String.valueOf(location.getAltitude()));
        } else {
            tv_altitude.setText("Not Available");
        }

        // for speed check
        if (location.hasSpeed()) {
            tv_altitude.setText(String.valueOf(location.getSpeed()));
        } else {
            tv_altitude.setText("No Speed Available");
        }

//        Geocoder geocoder = new Geocoder(MainActivity.this);
//        try {
//            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//            String address = getAddressLine(geocoder, addresses);
//            tv_address.setText(address);
//            /// saving into room db
////            databaseHelper.myLocationDao().addLocation(new MyLocation(lat, lon,address,dateTime));
//
//        } catch (Exception e){
//            tv_address.setText("Unable to get address");
//            e.printStackTrace();
//        }
        String address = getAddressLine(location);
        tv_address.setText(address);
        MyApplication myApplication = (MyApplication) getApplicationContext();
        savedLocations = myApplication.getMyLocations();
        // show the number of way points saved
        Log.d(TAG, "updateUIValues: "+savedLocations);
        tv_wayPointsCount.setText(Integer.toString(savedLocations.size()));


    }


    public String getAddressLine(Location location){
        Geocoder geocoder = new Geocoder(MainActivity.this);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            tv_address.setText(addresses.get(0).getAddressLine(0));
            /// saving into room db
//            databaseHelper.myLocationDao().addLocation(new MyLocation(lat, lon,address,dateTime));
            return address;
        } catch (Exception e){
            tv_address.setText("Unable to get address");
            e.printStackTrace();
            return "";
        }

    }

    public String getCurrentDateTime(){
        // Get the current date and time
        Date currentDateTime = new Date();

        // Define the desired date and time format
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

        // Format the current date and time
        String dateTime = dateTimeFormat.format(currentDateTime);
        return dateTime;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();
                } else {
                    Toast.makeText(this, "This is app requires permission to work properly!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }
}