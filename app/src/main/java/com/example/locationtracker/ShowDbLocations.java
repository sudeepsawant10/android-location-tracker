package com.example.locationtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.example.locationtracker.db.DatabaseHelper;
import com.example.locationtracker.db.MyLocation;
import com.example.locationtracker.db.MyLocationDao;

import java.util.List;

public class ShowDbLocations extends AppCompatActivity {
    Context context = this;
    RecyclerView rvLocationList;
    List<MyLocation> myLocationList;
    AdaptorMyLocationList adaptorMyLocationList;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_db_locations);
        rvLocationList = findViewById(R.id.rvLocationList);
        databaseHelper = DatabaseHelper.getDB(context);
        myLocationList = databaseHelper.myLocationDao().getLocations();

        rvLocationList.setLayoutManager(new LinearLayoutManager(context));
        adaptorMyLocationList = new AdaptorMyLocationList(context, myLocationList);
        rvLocationList.setAdapter(adaptorMyLocationList);

    }
}