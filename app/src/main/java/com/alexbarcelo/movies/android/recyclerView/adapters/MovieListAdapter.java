package com.alexbarcelo.movies.android.recyclerView.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexbarcelo.movies.R;
import com.alexbarcelo.movies.data.model.MovieSummary;
import com.alexbarcelo.movies.glide.GlideApp;
import com.alexbarcelo.movies.android.recyclerView.holders.EmptyViewHolder;
import com.alexbarcelo.movies.android.recyclerView.holders.MovieViewHolder;
import com.alexbarcelo.movies.android.recyclerView.holders.RetryButtonHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to a list of movies implemented with a recycler view. It also includes a footer item to display
 * either a spinner (when data is being loaded) or a retry button (in case of error while fetching data).
 */
public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieSummary> mMovies = new ArrayList<>();
    private Context mContext;
    private FooterItemType mFooterItemType = FooterItemType.NONE;
    private View.OnClickListener mOnRetryButtonClickListener;

    public MovieListAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Check if viewType matches any of the holder types for the footer. Otherwise, holder will be related to a movie item.
        if (viewType == FooterItemType.NONE.ViewType) {
            return new EmptyViewHolder(inflater.inflate(android.R.layout.simple_list_item_1, parent, false));
        } else if (viewType == FooterItemType.LOADING_SPINNER.ViewType) {
            return new EmptyViewHolder(inflater.inflate(R.layout.list_item_loading_more_items, parent, false));
        } else if (viewType == FooterItemType.RETRY_BUTTON.ViewType) {
            return new RetryButtonHolder(inflater.inflate(R.layout.list_item_retry_button, parent, false));
        } else {
            return new MovieViewHolder(inflater.inflate(R.layout.list_item_movie, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position < getItemCount() - 1) {
            // If it's not the footer, is a movie item
            MovieSummary movie = mMovies.get(position);
            MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
            movieViewHolder.setOverviewText(movie.overview());

            String year = movie.releaseDate().isEmpty() || movie.releaseDate().length() < 4 ? "????" : movie.releaseDate().substring(0, 4);
            movieViewHolder.setTitleText(String.format("%s (%s)", movie.title(), year));
            // Load poster image
            GlideApp.with(mContext.getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w200" + movie.posterPath())
                    .placeholder(R.drawable.movie_placeholder)
                    .into(movieViewHolder.getPosterView());
        } else if (holder.getItemViewType() == FooterItemType.RETRY_BUTTON.ViewType) {
            RetryButtonHolder retryButtonHolder = (RetryButtonHolder) holder;
            retryButtonHolder.setOnRetryButtonClickListener(mOnRetryButtonClickListener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // If we're asking for the footer, return the current type. Otherwise, it's a movie item.
        if (position == getItemCount() - 1) {
            return mFooterItemType.ViewType;
        } else {
            return -1;
        }
    }

    @Override
    public int getItemCount() {
        // +1 refers to the footer item
        return mMovies.size() + 1;
    }

    public void setMovies(List<MovieSummary> movies) {
        mMovies = new ArrayList<>(movies);
        notifyDataSetChanged();
    }

    public void showLoadingMoreItemsIndicator(boolean active) {
        mFooterItemType = active ? FooterItemType.LOADING_SPINNER : FooterItemType.NONE;
        notifyItemChanged(getItemCount() - 1);
    }

    public void showRetryButton(boolean active) {
        mFooterItemType = active ? FooterItemType.RETRY_BUTTON : FooterItemType.NONE;
        notifyItemChanged(getItemCount() - 1);
    }

    public void setOnRetryButtonClickListener(View.OnClickListener listener) {
        mOnRetryButtonClickListener = listener;
    }

    private enum FooterItemType {
        NONE, LOADING_SPINNER, RETRY_BUTTON;

        private int ViewType;

        static {
            NONE.ViewType = 0;
            LOADING_SPINNER.ViewType = 1;
            RETRY_BUTTON.ViewType = 2;
        }
    }
}
