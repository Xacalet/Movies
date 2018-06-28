package com.alexbarcelo.movies.android.recyclerView.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.alexbarcelo.movies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ViewHolder that holds a retry button in a recycler view item
 */
public class RetryButtonHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.retry_button) Button mRetryButton;

    public RetryButtonHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setOnRetryButtonClickListener(View.OnClickListener listener) {
        mRetryButton.setOnClickListener(listener);
    }
}