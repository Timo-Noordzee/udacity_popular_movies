package com.northseadevs.popularmovies.movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable{

    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w342";

    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("poster_path")
    private String image;
    @SerializedName("overview")
    private String plot;
    @SerializedName("vote_average")
    private float rating;
    @SerializedName("release_date")
    private String release_date;

    public Movie(String title, String image, String plot, float rating, String release_date) {
        this.originalTitle = title;
        this.image = image;
        this.plot = plot;
        this.rating = rating;
        this.release_date = release_date;
    }

    private Movie(Parcel in){
        originalTitle = in.readString();
        image = in.readString();
        plot = in.readString();
        rating = in.readFloat();
        release_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(originalTitle);
        parcel.writeString(image);
        parcel.writeString(plot);
        parcel.writeFloat(rating);
        parcel.writeString(release_date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getImage() {
        return BASE_IMAGE_URL + image;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };
}
