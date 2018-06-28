package com.alexbarcelo.movies.searchMovies;

import com.alexbarcelo.commons.rxjava.schedulers.BaseSchedulerProvider;
import com.alexbarcelo.movies.data.MoviesRepository;
import com.alexbarcelo.movies.data.model.MovieSummary;
import com.alexbarcelo.movies.data.model.PaginatedList;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;

public class SearchMoviesPresenter implements SearchMoviesContract.Presenter {

    private MoviesRepository mRepository;
    private BaseSchedulerProvider mSchedulerProvider;

    @Nullable private SearchMoviesContract.View mView;

    private int mLastLoadedPage = 0;
    private int mPageCount = Integer.MAX_VALUE;
    private List<MovieSummary> mMovies = new ArrayList<>();
    private boolean mCanLoadData = false;
    private Disposable mCurrentRequest = null;

    @Inject
    SearchMoviesPresenter(MoviesRepository repository, BaseSchedulerProvider schedulerProvider) {
        this.mRepository = repository;
        this.mSchedulerProvider = schedulerProvider;
    }

    @Override
    public void searchMovies(String searchText, boolean newSearch) {
        if (newSearch || searchText.isEmpty()) {
            //If it's a new search:
            //Clear search results on view
            mMovies = new ArrayList<>();
            if (mView != null) {
                mView.clearSearchResults();
            }
            //Reset state variables
            mLastLoadedPage = 0;
            mPageCount = Integer.MAX_VALUE;
            mCanLoadData = true;
            //Cancel pending request (if any)
            if (mCurrentRequest != null)
                mCurrentRequest.dispose();
        }
        if (mLastLoadedPage < mPageCount && mCanLoadData && !searchText.isEmpty()) {
            mCanLoadData = false;
            if (mView != null) {
                mView.showNoResultsMessage(false);
                mView.showLoadingMoreItemsIndicator(true);
            }
            mCurrentRequest = new DisposableSingleObserver<PaginatedList<MovieSummary>>() {
                @Override
                public void onSuccess(PaginatedList<MovieSummary> movieSummaryPaginatedList) {
                    //Update view
                    mMovies.addAll(movieSummaryPaginatedList.results());
                    if (mView != null) {
                        if (mMovies.isEmpty()) {
                            mView.showNoResultsMessage(true);
                        }
                        mView.updateSearchResults(mMovies);
                        mView.showLoadingMoreItemsIndicator(false);
                    }
                    //Update state variables
                    mLastLoadedPage++;
                    mPageCount = movieSummaryPaginatedList.totalPages();
                    mCanLoadData = true;
                }

                @Override
                public void onError(Throwable e) {
                    if (mView != null) {
                        mView.showError(e.getMessage());
                        mView.showRetryButton(true);
                    }
                    mCanLoadData = false;
                }
            };
            mRepository.searchMoviesByText(mLastLoadedPage + 1, searchText)
                    .subscribeOn(mSchedulerProvider.computation())
                    .observeOn(mSchedulerProvider.ui())
                    .subscribe((DisposableSingleObserver) mCurrentRequest);
        }
    }

    @Override
    public void retryMovieSearch(String searchText) {
        if (mView != null) {
            mView.showRetryButton(false);
            mCanLoadData = true;
            searchMovies(searchText, false);
        }
    }

    @Override
    public void takeView(SearchMoviesContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
