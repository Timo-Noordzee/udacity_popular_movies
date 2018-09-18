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
import android.widget.ImageView;

import com.northseadevs.popularmovies.movie.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    private Movie mMovie;

    @BindView(R.id.iv_details_movie_backdrop) ImageView movieBackdrop;
    @BindView(R.id.tb_details_toolbar) Toolbar toolbar;
    @BindView(R.id.details_app_bar) AppBarLayout appBarLayout;
    @BindView(R.id.details_collapsing_toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent.hasExtra(MainActivity.INTENT_MOVIE_KEY)){
            mMovie = (Movie) intent.getParcelableExtra(MainActivity.INTENT_MOVIE_KEY);
            Picasso.get().load(mMovie.getBackdrop()).into(movieBackdrop);
        } else {
            Log.d("Details", "No movie found!");
        }

        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) getWindow().setStatusBarColor(Color.TRANSPARENT);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

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
                    isShow = false;
                } else if (!isShow) {
                    collapsingToolbarLayout.setTitle(" "); // Careful! There should be a space between double quote. Otherwise it won't work.
                    isShow = true;
                }
            }
        });
    }
}
