package com.northseadevs.popularmovies.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.northseadevs.popularmovies.movie.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> movies;

    public MovieViewModel(Application application) {
        super(application);
        movies = AppDatabase.getInstance(this.getApplication()).movieDao().getAllMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}
