package com.alexbarcelo.movies.android.recyclerView.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexbarcelo.movies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ViewHolder that holds data for a movie item in a recycler view
 */
public class MovieViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.movie_overview) TextView mOverviewView;
    @BindView(R.id.movie_title) TextView mTitleview;
    public @BindView(R.id.movie_poster_image) ImageView mPosterView;

    public MovieViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setOverviewText(String text) {
        mOverviewView.setText(text);
    }

    public void setTitleText(String text) {
        mTitleview.setText(text);
    }

    public ImageView getPosterView() {
        return mPosterView;
    }
}
