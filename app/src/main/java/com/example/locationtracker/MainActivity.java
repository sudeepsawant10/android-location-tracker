package com.example.locationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;

public class MainActivity extends AppCompatActivity {
    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 30;
    // Google API for location update
//    FusedLocationProviderClient fusedLocationProviderClient;
    // Location Request is a config file for FusedLocationClient
//    LocationRequest locationRequest;
    TextView tv_lat, tv_lon, tv_altitude, tv_accuracy;
    TextView  tv_speed, tv_sensor, tv_updates, tv_address ;
    Switch sw_locationsupdates, sw_gps;
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

        // set all properties of Location Request
//        locationRequest = new LocationRequest();
//        locationRequest.setInterval(1000* DEFAULT_UPDATE_INTERVAL);
//
//        locationRequest.setFastestInterval(FAST_UPDATE_INTERVAL);
//        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);



    }

}