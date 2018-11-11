package com.northseadevs.popularmovies.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.northseadevs.popularmovies.movie.Review;
import com.northseadevs.popularmovies.movie.Video;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class DataTypeConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public double toDouble(Float f){
        return f.doubleValue();
    }

    @TypeConverter
    public float toFloat(Double d){
        return  d.floatValue();
    }

    @TypeConverter
    public static List<Video> stringToVideos(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Video>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String videosToString(List<Video> videos) {
        return gson.toJson(videos);
    }

    @TypeConverter
    public static List<Review> stringToReviews(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Review>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String reviewsToString(List<Review> reviews) {
        return gson.toJson(reviews);
    }
}
