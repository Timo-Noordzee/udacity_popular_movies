package com.northseadevs.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.northseadevs.popularmovies.movie.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.iv_movie_cover) ImageView movieCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent.hasExtra(MainActivity.INTENT_MOVIE_KEY)){
            Movie movie = (Movie) intent.getParcelableExtra(MainActivity.INTENT_MOVIE_KEY);
            Picasso.get().load(movie.getImage()).into(movieCover);
        } else {
            Log.d("Details", "No movie found!");
        }
    }
}
