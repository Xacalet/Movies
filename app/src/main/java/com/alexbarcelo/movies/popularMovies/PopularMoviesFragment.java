package com.alexbarcelo.movies.popularMovies;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexbarcelo.commons.di.ActivityScoped;
import com.alexbarcelo.movies.R;
import com.alexbarcelo.movies.android.dialogs.ErrorDialogFragment;
import com.alexbarcelo.movies.android.recyclerView.adapters.MovieListAdapter;
import com.alexbarcelo.movies.data.model.MovieSummary;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class PopularMoviesFragment extends DaggerFragment implements PopularMoviesContract.View {

    @Inject PopularMoviesContract.Presenter mPresenter;

    @BindView(R.id.movie_list_view) RecyclerView mMovieListView;

    private MovieListAdapter mAdapter;

    @Inject
    public PopularMoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_popular_movies, container, false);

        ButterKnife.bind(this, v);

        mAdapter = new MovieListAdapter(this.getActivity());
        mAdapter.setOnRetryButtonClickListener(view -> mPresenter.retryMovieLoading());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mMovieListView.setLayoutManager(linearLayoutManager);

        mMovieListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastItemIndex = mAdapter.getItemCount() - 1;
                if (layoutManager.findLastVisibleItemPosition() == lastItemIndex) {
                    mPresenter.loadMovies();
                }
            }
        });

        mMovieListView.setAdapter(mAdapter);
        mMovieListView.setHasFixedSize(false);

        mPresenter.loadMovies();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.takeView(this);
    }

    @Override
    public void showMovies(List<MovieSummary> movies) {
        mAdapter.setMovies(movies);
    }

    @Override
    public void showLoadingMoreItemsIndicator(boolean active) {
        mAdapter.showLoadingMoreItemsIndicator(active);
    }

    @Override
    public void showRetryButton(boolean active) {
        mAdapter.showRetryButton(active);
    }

    @Override
    public void showError(String message) {
        ErrorDialogFragment dialogFragment = ErrorDialogFragment.newInstance(message);
        dialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), null);
    }
}
