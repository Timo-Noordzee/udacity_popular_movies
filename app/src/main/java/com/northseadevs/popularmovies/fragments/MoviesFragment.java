package com.northseadevs.popularmovies.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.northseadevs.popularmovies.BuildConfig;
import com.northseadevs.popularmovies.DetailsActivity;
import com.northseadevs.popularmovies.MainActivity;
import com.northseadevs.popularmovies.R;
import com.northseadevs.popularmovies.database.AppDatabase;
import com.northseadevs.popularmovies.database.MovieViewModel;
import com.northseadevs.popularmovies.movie.Movie;
import com.northseadevs.popularmovies.movie.Movies;
import com.northseadevs.popularmovies.networking.FetchMoviesTask;
import com.northseadevs.popularmovies.networking.TheMovieDatabaseQuery;
import com.northseadevs.popularmovies.utils.MovieAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesFragment extends Fragment implements MovieAdapter.MovieClickHandler {

    public static final String SORT_TYPE_ARGS_KEY = "sort_type";
    private static final String MOVIES_BUNDLE_KEY = "movies";

    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_no_internet)
    ImageView mNoInternet;
    @BindView(R.id.pb_loading_movies_indicator)
    ProgressBar mLoadingMoviesIndicator;
    @BindView(R.id.tv_loading_movies_indicator)
    TextView mLoadingMoviesText;

    private ArrayList<Movie> mMovies;

    private String mSortBy = FetchMoviesTask.MOST_POPULAR;
    private TheMovieDatabaseQuery databaseQuery;
    private MovieAdapter mAdapter;
    private String dataType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_list_fragment, container, false);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        if (args != null) mSortBy = args.getString(SORT_TYPE_ARGS_KEY, FetchMoviesTask.MOST_POPULAR);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheMovieDatabaseQuery.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        databaseQuery = retrofit.create(TheMovieDatabaseQuery.class);
        AppDatabase mDb = AppDatabase.getInstance(getContext());

        //Setup the RecyclerView and Adapter

        mAdapter = new MovieAdapter(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.grid_column_count)));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        if(savedInstanceState != null && savedInstanceState.containsKey(MOVIES_BUNDLE_KEY)){
            Log.d("Movie", "SavedInstances");
            mMovies = savedInstanceState.getParcelableArrayList(MOVIES_BUNDLE_KEY);
            onMoviesFetched(mMovies);
        } else {
            Log.d("Movie", "Fetch");
            fetchMovies(mSortBy);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIES_BUNDLE_KEY, mMovies);
    }

    //Fetch movies using the TheMovieDatabase API
    private void fetchMovies(String sortBy) {
        mLoadingMoviesIndicator.setVisibility(View.VISIBLE);
        mLoadingMoviesText.setVisibility(View.VISIBLE);

        if(sortBy.equals(TheMovieDatabaseQuery.MOST_POPULAR) || sortBy.equals(TheMovieDatabaseQuery.TOP_RATED)) {
            Call<Movies> call = databaseQuery.loadMovies(sortBy, BuildConfig.API_KEY);
            call.enqueue(new Callback<Movies>() {
                @Override
                public void onResponse(@NonNull Call<Movies> call, @NonNull Response<Movies> response) {
                    Movies movies = response.body();
                    if (movies != null && movies.getMovies().size() > 0) {
                        onMoviesFetched(movies.getMovies());
                    } else {
                        onMoviesFetched(new ArrayList<Movie>());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Movies> call, @NonNull Throwable t) {
                    onMoviesFetched(new ArrayList<Movie>());
                }
            });
        } else if(sortBy.equals(TheMovieDatabaseQuery.FAVORITES)){
            MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
            movieViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    onMoviesFetched((ArrayList<Movie>) movies);
                }
            });
        }
    }

    public void onMoviesFetched(ArrayList<Movie> movies) {
        if(movies != null && movies.size() > 0) {
            mMovies = movies;
            mLoadingMoviesIndicator.setVisibility(View.GONE);
            mLoadingMoviesText.setVisibility(View.GONE);
            mNoInternet.setVisibility(View.INVISIBLE);
            mAdapter.setMovieData(movies);
            Log.d(getClass().getSimpleName(), movies.size() + " movies loaded!");
        } else {
            mLoadingMoviesIndicator.setVisibility(View.GONE);
            mLoadingMoviesText.setText(mSortBy.equals(TheMovieDatabaseQuery.FAVORITES) ? getString(R.string.error_no_favourites) : getString(R.string.error_no_internet));
            mNoInternet.setImageResource(mSortBy.equals(TheMovieDatabaseQuery.FAVORITES) ? R.drawable.ic_favorite_border : R.drawable.ic_signal_cellular_connected_no_internet);
            mNoInternet.setVisibility(View.VISIBLE);
            mAdapter.setMovieData(new ArrayList<Movie>());
            Snackbar.make(getView().findViewById(R.id.movie_list_fragment), "No internet", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchMovies(mSortBy);
                }
            }).show();
        }
    }

    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra(MainActivity.INTENT_MOVIE_KEY, movie);
        startActivity(intent);
    }

    public static MoviesFragment newInstance(String sortBy){
        MoviesFragment moviesFragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putString(SORT_TYPE_ARGS_KEY, sortBy);
        moviesFragment.setArguments(args);
        return moviesFragment;
    }
}
