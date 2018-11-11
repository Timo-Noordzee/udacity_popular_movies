package com.northseadevs.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.northseadevs.popularmovies.database.AppDatabase;
import com.northseadevs.popularmovies.movie.Movie;
import com.northseadevs.popularmovies.movie.Reviews;
import com.northseadevs.popularmovies.movie.Video;
import com.northseadevs.popularmovies.movie.Videos;
import com.northseadevs.popularmovies.networking.TheMovieDatabaseQuery;
import com.northseadevs.popularmovies.utils.AppExecutors;
import com.northseadevs.popularmovies.utils.ReviewAdapter;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.iv_details_movie_backdrop)
    ImageView movieBackdrop;
    @BindView(R.id.tb_details_toolbar)
    Toolbar toolbar;
    @BindView(R.id.details_app_bar)
    AppBarLayout appBarLayout;
    @BindView(R.id.details_collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.tv_details_title)
    TextView mMovieTitle;
    @BindView(R.id.tv_details_plot)
    TextView mMoviePlot;
    @BindView(R.id.tv_details_release_date)
    TextView mMovieReleaseDate;
    @BindView(R.id.tv_details_rating)
    TextView mMovieRating;
    @BindView(R.id.iv_details_movie_poster)
    ImageView mMoviePoster;
    @BindView(R.id.iv_details_favourite)
    ImageView mFavouriteButton;

    private Movie mMovie;
    private TheMovieDatabaseQuery mDatabaseQuery;
    private ReviewAdapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.INTENT_MOVIE_KEY)) {
            mMovie = intent.getParcelableExtra(MainActivity.INTENT_MOVIE_KEY);
        } else {
            Log.d("Details", "No movie found!");
        }

        mReviewAdapter = new ReviewAdapter();
        RecyclerView mRecyclerView = findViewById(R.id.rv_reviews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mReviewAdapter);
        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);

        if (mMovie != null) populateUI();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheMovieDatabaseQuery.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mDatabaseQuery = retrofit.create(TheMovieDatabaseQuery.class);

        Call<Reviews> loadReviews = mDatabaseQuery.loadReviews(mMovie.getId(), BuildConfig.API_KEY);
        loadReviews.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(@NonNull Call<Reviews> call, @NonNull Response<Reviews> response) {
                Reviews reviews = response.body();
                if (reviews != null && reviews.getReviews().size() > 0) {
                    mMovie.setReviews(reviews.getReviews());
                    mReviewAdapter.setReviews(mMovie.getReviews());
                    updateMovieInDatabase();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Reviews> call, @NonNull Throwable t) {
                Log.w(getClass().getSimpleName(), t.getMessage());
            }
        });

        Call<Videos> loadVideos = mDatabaseQuery.loadVideos(mMovie.getId(), BuildConfig.API_KEY);
        loadVideos.enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(@NonNull Call<Videos> call, @NonNull Response<Videos> response) {
                Videos videos = response.body();
                if (videos != null) {
                    mMovie.setVideos(videos.getVideos());
                    updateMovieInDatabase();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Videos> call, @NonNull Throwable t) {
                Log.w(getClass().getSimpleName(), t.getMessage());
            }
        });

    }

    private void populateUI() {
        setupActionBar();
        Picasso.get().load(mMovie.getBackdropUrl()).into(movieBackdrop);
        Picasso.get().load(mMovie.getPosterUrl()).placeholder(R.drawable.placeholder_movie_poster).into(mMoviePoster);

        mMovieTitle.setText(mMovie.getTitle());
        mMoviePlot.setText(mMovie.getPlot());
        mMovieReleaseDate.setText(mMovie.getRelease_date());
        mMovieRating.setText(getString(R.string.rating, mMovie.getRating()));

        movieBackdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMovie.getVideos() != null && mMovie.getVideos().size() > 0){
                    for(int i = 0; i < mMovie.getVideos().size(); i++){
                        Video trailer = mMovie.getVideos().get(i);
                        if(trailer.getSite().equalsIgnoreCase("YouTube")){
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey())));
                            return;
                        }
                    }
                    Toast.makeText(DetailsActivity.this, getString(R.string.no_trailer_found), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DetailsActivity.this, getString(R.string.no_trailer_found), Toast.LENGTH_LONG).show();
                }
            }
        });

        mFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMovie.isFavourite()){
                    mMovie.setFavourite(false);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            AppDatabase.getInstance(DetailsActivity.this).movieDao().deleteMovie(mMovie);
                        }
                    });
                } else {
                    mMovie.setFavourite(true);
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            AppDatabase.getInstance(DetailsActivity.this).movieDao().insertMovie(mMovie);
                        }
                    });
                }
                mFavouriteButton.setImageResource(mMovie.isFavourite() ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
            }
        });

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                boolean favourite = AppDatabase.getInstance(DetailsActivity.this).movieDao().contains(mMovie.getId()) == 1;
                mMovie.setFavourite(favourite);
                mFavouriteButton.setImageResource(favourite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
            }
        });

        if(mMovie.getReviews() != null){
            mReviewAdapter.setReviews(mMovie.getReviews());
        }
    }

    private void updateMovieInDatabase(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(DetailsActivity.this).movieDao().updateMovie(mMovie);
            }
        });
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mMovie.getTitle());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
