package com.example.locationtracker.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = MyLocation.class, exportSchema = false, version = 1)
public abstract class DatabaseHelper extends RoomDatabase {
    private static final String DB_NAME = "mylocation_db";
    private static DatabaseHelper instance;

    // sync => don't provide multiple access on multiple requests
    public static synchronized DatabaseHelper getDB(Context context){
        if (instance == null) {
            instance = Room.databaseBuilder(context, DatabaseHelper.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract MyLocationDao myLocationDao();
}
