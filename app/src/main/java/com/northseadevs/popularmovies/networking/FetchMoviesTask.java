package com.northseadevs.popularmovies.networking;

import android.os.AsyncTask;

import com.northseadevs.popularmovies.BuildConfig;
import com.northseadevs.popularmovies.movie.Movie;
import com.northseadevs.popularmovies.movie.Movies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FetchMoviesTask extends AsyncTask<Void, Void, List<Movie>> {

    public final static String MOST_POPULAR = "popular";
    public final static String TOP_RATED = "top_rated";
    public final static String FAVORITES = "favorites";

    private static final String BASE_URL = "https://api.themoviedb.org";

    private String mSortBy;
    private static FetchMoviesCallback mFetchMoviesCallback;

    public interface FetchMoviesCallback {
        void onMoviesFetched(List<Movie> movies);
    }

    public FetchMoviesTask(String sortBy, FetchMoviesCallback fetchMoviesCallback) {
        mSortBy = sortBy;
        mFetchMoviesCallback = fetchMoviesCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Movie> doInBackground(Void... voids) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TheMovieDatabaseQuery databaseQuery = retrofit.create(TheMovieDatabaseQuery.class);
        Call<Movies> call = databaseQuery.loadMovies(mSortBy, BuildConfig.API_KEY);

        try {
            Response<Movies> response = call.execute();
            Movies movies = response.body();
            if(movies != null && movies.getMovies().size() > 0) return movies.getMovies(); else return new ArrayList<Movie>();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        mFetchMoviesCallback.onMoviesFetched(movies);
    }
}
