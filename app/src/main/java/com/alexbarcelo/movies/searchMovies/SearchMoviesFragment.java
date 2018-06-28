package com.alexbarcelo.movies.searchMovies;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexbarcelo.commons.di.ActivityScoped;
import com.alexbarcelo.movies.R;
import com.alexbarcelo.movies.android.dialogs.ErrorDialogFragment;
import com.alexbarcelo.movies.android.recyclerView.adapters.MovieListAdapter;
import com.alexbarcelo.movies.data.model.MovieSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class SearchMoviesFragment extends DaggerFragment implements SearchMoviesContract.View {

    @Inject SearchMoviesContract.Presenter mPresenter;

    @BindView(R.id.movie_list_view) RecyclerView mMovieListView;
    @BindView(R.id.search_view) SearchView mSearchView;
    @BindView(R.id.no_results_text_view) TextView mNoResultsView;

    private MovieListAdapter mAdapter;

    @Inject
    public SearchMoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_movies, container, false);

        ButterKnife.bind(this, v);

        mAdapter = new MovieListAdapter(this.getActivity());
        mAdapter.setOnRetryButtonClickListener(view -> mPresenter.retryMovieSearch(mSearchView.getQuery().toString()));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mMovieListView.setLayoutManager(linearLayoutManager);

        mMovieListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastItemIndex = mAdapter.getItemCount() - 1;
                if (layoutManager.findLastVisibleItemPosition() == lastItemIndex && !mSearchView.getQuery().toString().isEmpty()) {
                    mPresenter.searchMovies(mSearchView.getQuery().toString(), false);
                }
            }
        });

        mMovieListView.setAdapter(mAdapter);
        mMovieListView.setHasFixedSize(false);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty())
                    clearSearchResults();
                else
                    mPresenter.searchMovies(newText, true);
                return true;
            }
        });

        mSearchView.setIconifiedByDefault(true);
        //mSearchView.setQueryHint("按姓名或电子邮件搜索");
        mSearchView.setIconified(false);
        mSearchView.setFocusable(false);
        mSearchView.clearFocus();
        mSearchView.requestFocus();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.takeView(this);
    }

    @Override
    public void updateSearchResults(List<MovieSummary> movies) {
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

    @Override
    public void showNoResultsMessage(boolean active) {
        mNoResultsView.setVisibility(active ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void clearSearchResults() {
        mAdapter.setMovies(new ArrayList<>());
        showLoadingMoreItemsIndicator(false);
        showNoResultsMessage(false);
        showRetryButton(false);
    }
}
