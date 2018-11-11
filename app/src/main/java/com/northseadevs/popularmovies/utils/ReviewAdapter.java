package com.northseadevs.popularmovies.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.northseadevs.popularmovies.R;
import com.northseadevs.popularmovies.movie.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{

    private List<Review> mReviews;

    public ReviewAdapter() {
        mReviews = new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.mInitial.setText(review.getAuthor().substring(0, 1));
        holder.mTitle.setText(review.getAuthor());
        holder.mContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if(mReviews == null) return 0;
        return mReviews.size();
    }

    public void setReviews(List<Review> reviews) {
        this.mReviews = reviews;
        notifyDataSetChanged();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mAvatar;
        private TextView mInitial;
        private TextView mTitle;
        private TextView mContent;

        ReviewViewHolder(View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.iv_review_avatar);
            mInitial = itemView.findViewById(R.id.tv_review_avatar_initial);
            mTitle = itemView.findViewById(R.id.tv_review_title);
            mContent = itemView.findViewById(R.id.tv_review_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
