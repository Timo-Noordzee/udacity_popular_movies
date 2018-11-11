package com.northseadevs.popularmovies.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.northseadevs.popularmovies.R;
import com.northseadevs.popularmovies.movie.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> mMovies;
    private MovieClickHandler mClickHandler;

    public interface MovieClickHandler {
        void onMovieClicked(Movie movie);
    }


    public MovieAdapter(MovieClickHandler clickHandler) {
        mClickHandler = clickHandler;
        mMovies = new ArrayList<>();
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mPoster;

        MovieViewHolder(View itemView) {
            super(itemView);
            mPoster = itemView.findViewById(R.id.iv_movie_list_item_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovies.get(adapterPosition);
            mClickHandler.onMovieClicked(movie);
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int grid_column_count = context.getResources().getInteger(R.integer.grid_column_count);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie, parent, false);
        view.getLayoutParams().height = (int) (parent.getWidth() / grid_column_count * Movie.POSTER_ASPECT_RATIO);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        Log.d("Adapter", "Loading poster: " + movie.getPoster());
        Picasso.get().load(movie.getPosterUrl()).into(holder.mPoster);
    }

    @Override
    public void onViewRecycled(@NonNull MovieViewHolder holder) {
        super.onViewRecycled(holder);
        Picasso.get().cancelRequest(holder.mPoster);
    }

    @Override
    public int getItemCount() {
        if(mMovies == null) return 0;
        return mMovies.size();
    }

    public void setMovieData(List<Movie> movies) {
        this.mMovies = movies;
        notifyDataSetChanged();
    }
}
