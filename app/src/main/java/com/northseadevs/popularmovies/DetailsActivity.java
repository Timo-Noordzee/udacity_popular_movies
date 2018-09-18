package com.northseadevs.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
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

    @BindView(R.id.tv_movie_title)
    TextView mMovieTitle;
    @BindView(R.id.rb_movie_rating)
    RatingBar mMovieRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.INTENT_MOVIE_KEY)) {
            mMovie = (Movie) intent.getParcelableExtra(MainActivity.INTENT_MOVIE_KEY);
        } else {
            Log.d("Details", "No movie found!");
        }

        if (mMovie != null) populateUI();

        setupActionBar();
    }

    private void populateUI() {
        Picasso.get().load(mMovie.getBackdrop()).into(movieBackdrop);
        mMovieTitle.setText(mMovie.getOriginalTitle());
        TextViewCompat.setAutoSizeTextTypeWithDefaults(mMovieTitle, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        mMovieRating.setRating(mMovie.getRating(true));
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        //Register the scroll listener to hide the toolbar title
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(mMovie.getOriginalTitle());
                    mMovieTitle.setVisibility(View.GONE);
                    isShow = false;
                } else if (!isShow) {
                    collapsingToolbarLayout.setTitle(" "); // Careful! There should be a space between double quote. Otherwise it won't work.
                    mMovieTitle.setVisibility(View.VISIBLE);
                    isShow = true;
                }
            }
        });
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
