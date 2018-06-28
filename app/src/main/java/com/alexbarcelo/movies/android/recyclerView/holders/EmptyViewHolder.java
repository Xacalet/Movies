package com.alexbarcelo.movies.android.recyclerView.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Holder with no views to interact with. This was created as ViewHolder class is abstract and
 * cannot be instantiated.
 */
public class EmptyViewHolder extends RecyclerView.ViewHolder {

    public EmptyViewHolder(View itemView) {
        super(itemView);
    }
}