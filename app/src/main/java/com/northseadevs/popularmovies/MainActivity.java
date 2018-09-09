package com.northseadevs.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.northseadevs.popularmovies.movie.Movie;
import com.northseadevs.popularmovies.networking.FetchMoviesTask;
import com.northseadevs.popularmovies.utils.MovieAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements FetchMoviesTask.FetchMoviesCallback{

    public static final String INTENT_MOVIE_KEY = "movie";

    private static final String BUNDLE_MOVIES_KEY = "movies";

    private String mSortBy = FetchMoviesTask.MOST_POPULAR;

    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.gridView_movies) GridView mGridView;
    @BindView(R.id.progressBar_movies) ProgressBar mProgressBar;
    private List<Movie> mMovies;
    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //Setup the GridView and Adapter
        mAdapter = new MovieAdapter(this);
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

        //Setup TabLayout
        setSupportActionBar(mToolbar);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.most_popular));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.top_rated));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        fetchMovies(FetchMoviesTask.MOST_POPULAR);
                        break;
                    case 1:
                        fetchMovies(FetchMoviesTask.TOP_RATED);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Check if the savedInstanceState contains movies and restore them
        if(savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_MOVIES_KEY)){
            mMovies = savedInstanceState.getParcelableArrayList(BUNDLE_MOVIES_KEY);
            mProgressBar.setVisibility(View.INVISIBLE);
            mAdapter.setMovieData(mMovies);
        } else {
            fetchMovies(mSortBy);
        }
    }

    //Fetch movies using the TheMovieDatabase API
    private void fetchMovies(String sortBy){
        mProgressBar.setVisibility(View.VISIBLE);
        mAdapter.setMovieData(new ArrayList<Movie>());
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(sortBy, this);
        fetchMoviesTask.execute();
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
