package com.example.locationtracker.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "location")
public class MyLocation {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "latitude")
    private String latitude;
    @ColumnInfo(name = "longitude")
    private String longitude;
    @ColumnInfo(name = "address")
    private String address;
    @ColumnInfo(name = "time")
    private String time;

    public MyLocation(int id, String latitude, String longitude, String address, String time) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.time = time;
    }
    @Ignore
    public MyLocation(String latitude, String longitude, String address, String time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
