package com.northseadevs.popularmovies.networking;

import com.northseadevs.popularmovies.movie.Movies;
import com.northseadevs.popularmovies.movie.Reviews;
import com.northseadevs.popularmovies.movie.Videos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDatabaseQuery {

    String BASE_URL = "https://api.themoviedb.org";
    String MOST_POPULAR = "popular";
    String TOP_RATED = "top_rated";
    String FAVORITES = "favorites";

    @GET("/3/movie/{sort_by}")
    Call<Movies> loadMovies(@Path("sort_by") String sortBy, @Query("api_key") String apiKey);

    @GET("/3/movie/{id}/videos")
    Call<Videos> loadVideos(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("/3/movie/{id}/reviews")
    Call<Reviews> loadReviews(@Path("id") int id, @Query("api_key") String apiKey);

}