package com.northseadevs.popularmovies.movie;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity(tableName = "movies", indices = {@Index(value = {"id"}, unique = true)})
public class Movie implements Parcelable {

    @Ignore
    public static final float POSTER_ASPECT_RATIO = 1.5f;

    @Ignore
    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w342";
    @Ignore
    private static final String BASE_BACKDROP_URL = "http://image.tmdb.org/t/p/w780";

    @Ignore
    private static final int RATING_RANGE = 10;
    @Ignore
    private static final int NUM_OF_STARS = 5;

    @SerializedName("id")
    @PrimaryKey(autoGenerate = false)
    private int id;
    @SerializedName("original_title")
    private String title;
    @SerializedName("poster_path")
    private String poster;
    @SerializedName("backdrop_path")
    private String backdrop;
    @SerializedName("overview")
    private String plot;
    @SerializedName("vote_average")
    private double rating;
    @SerializedName("release_date")
    private String release_date;

    private List<Review> reviews;
    private List<Video> videos;

    @Ignore private boolean isFavourite;

    public Movie(int id, String title, String poster, String backdrop, String plot, double rating, String release_date, List<Review> reviews, List<Video> videos) {
        this.reviews = new ArrayList<Review>();
        this.videos = new ArrayList<Video>();

        this.id = id;
        this.title = title;
        this.poster = poster;
        this.backdrop = backdrop;
        this.plot = plot;
        this.rating = rating;
        this.release_date = release_date;
        this.reviews = reviews;
        this.videos = videos;
    }

    @Ignore
    private Movie(Parcel in) {
        this.reviews = new ArrayList<Review>();
        this.videos = new ArrayList<Video>();

        id = in.readInt();
        title = in.readString();
        poster = in.readString();
        backdrop = in.readString();
        plot = in.readString();
        rating = in.readDouble();
        release_date = in.readString();
        in.readTypedList(reviews, Review.CREATOR);
        in.readTypedList(videos, Video.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(poster);
        parcel.writeString(backdrop);
        parcel.writeString(plot);
        parcel.writeDouble(rating);
        parcel.writeString(release_date);
        parcel.writeTypedList(reviews);
        parcel.writeTypedList(videos);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<Video> getVideos() {
        return this.videos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        return BASE_POSTER_URL + poster;
    }

    public String getPoster() {
        return poster;
    }

    public String getBackdropUrl() {
        return BASE_BACKDROP_URL + backdrop;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public String getPlot() {
        return plot;
    }

    public double getRating() {
        return rating;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getRelease_date() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = simpleDateFormat.parse(this.release_date);
            simpleDateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
            return simpleDateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return release_date;
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
