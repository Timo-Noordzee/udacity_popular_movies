package com.northseadevs.popularmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.northseadevs.popularmovies.movie.Movie;

@Database(entities = {Movie.class}, version = 2, exportSchema = false)
@TypeConverters(DataTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                String databaseName = "movies";
                sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, databaseName).fallbackToDestructiveMigration().build();
            }
        }
        return sInstance;
    }

    public abstract MovieDao movieDao();
}
