package com.northseadevs.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.northseadevs.popularmovies.movie.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    private Movie mMovie;

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

        if (mMovie != null) populateUI();

    }

    private void populateUI() {
        setupActionBar();
        Picasso.get().load(mMovie.getBackdrop()).into(movieBackdrop);
        Picasso.get().load(mMovie.getPoster()).into(mMoviePoster);

        mMovieTitle.setText(mMovie.getOriginalTitle());
        mMoviePlot.setText(mMovie.getPlot());
        mMovieReleaseDate.setText(mMovie.getRelease_date());
        mMovieRating.setText(getString(R.string.rating, mMovie.getRating()));
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mMovie.getOriginalTitle());
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
