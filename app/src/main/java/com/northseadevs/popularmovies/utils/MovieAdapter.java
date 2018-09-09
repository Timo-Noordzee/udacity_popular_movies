package com.northseadevs.popularmovies.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.northseadevs.popularmovies.movie.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends BaseAdapter {

    private List<Movie> mMovies;
    private Context mContext;

    public MovieAdapter(Context context) {
        mContext = context;
        mMovies = new ArrayList<Movie>();
        notifyDataSetChanged();
    }

    public MovieAdapter(Context context, List<Movie> movies){
        mContext = context;
        mMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        Movie movie = getItem(position);
        if(convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            //imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.get().load(movie.getImage()).into(imageView);
        return imageView;
    }

    public void setMovieData(List<Movie> movies){
        this.mMovies = movies;
        notifyDataSetChanged();
    }


}
