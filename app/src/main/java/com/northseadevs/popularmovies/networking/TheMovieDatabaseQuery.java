package com.northseadevs.popularmovies.networking;

import com.northseadevs.popularmovies.movie.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDatabaseQuery {

    @GET("/3/movie/{sort_by}")
    Call<Movies> loadMovies(@Path("sort_by") String sortBy, @Query("api_key") String apiKey);

}
