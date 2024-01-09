package com.example.locationtracker.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MyLocationDao {

    @Query("select * from location")
    List<MyLocation> getLocations();

    @Insert()
    void addLocation(MyLocation location);
}
