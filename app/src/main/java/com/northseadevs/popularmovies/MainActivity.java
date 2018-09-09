package com.northseadevs.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.northseadevs.popularmovies.movie.Movie;
import com.northseadevs.popularmovies.utils.MovieAdapter;
import com.northseadevs.popularmovies.networking.FetchMoviesTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FetchMoviesTask.FetchMoviesCallback{

    public static final String INTENT_MOVIE_KEY = "movie";
    private static final String BUNDLE_MOVIES_KEY = "movies";

    private GridView mGridView;
    private List<Movie> mMovies;
    private MovieAdapter mAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new MovieAdapter(this);
        mGridView = findViewById(R.id.gridView_movies);
        mGridView.setColumnWidth(GridView.AUTO_FIT);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(INTENT_MOVIE_KEY, mAdapter.getItem(position));
                startActivity(intent);
            }
        });
        mGridView.setAdapter(mAdapter);
        mProgressBar = findViewById(R.id.progressBar_movies);

        if(savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_MOVIES_KEY)){
            mMovies = savedInstanceState.getParcelableArrayList(BUNDLE_MOVIES_KEY);

            mProgressBar.setVisibility(View.INVISIBLE);
            mAdapter.setMovieData(mMovies);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            FetchMoviesTask fetchMoviesTask = new FetchMoviesTask("popular", this);
            fetchMoviesTask.execute();
            Log.d(getClass().getSimpleName(), "Loading movies from database");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(getClass().getSimpleName(), "Saving movies to instance state");
        outState.putParcelableArrayList(BUNDLE_MOVIES_KEY, (ArrayList<? extends Parcelable>) mMovies);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMoviesFetched(List<Movie> movies) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mMovies = movies;
        mAdapter.setMovieData(movies);
        Log.d(getClass().getSimpleName(), "Movie count: " + movies.size());
    }
}
