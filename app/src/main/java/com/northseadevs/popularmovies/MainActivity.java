package com.northseadevs.popularmovies;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.northseadevs.popularmovies.fragments.MoviesFragment;
import com.northseadevs.popularmovies.networking.FetchMoviesTask;
import com.northseadevs.popularmovies.utils.ViewPageAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String INTENT_MOVIE_KEY = "movie";

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.vp_main)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        ViewPageAdapter mViewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());

        mViewPageAdapter.addFragment(MoviesFragment.newInstance(FetchMoviesTask.MOST_POPULAR), getString(R.string.most_popular));
        mViewPageAdapter.addFragment(MoviesFragment.newInstance(FetchMoviesTask.TOP_RATED), getString(R.string.top_rated));

        mViewPager.setAdapter(mViewPageAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        setSupportActionBar(mToolbar);
    }
}
