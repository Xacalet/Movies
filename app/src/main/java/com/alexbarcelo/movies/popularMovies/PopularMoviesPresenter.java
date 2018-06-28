package com.alexbarcelo.movies.popularMovies;

import com.alexbarcelo.movies.data.MoviesRepository;
import com.alexbarcelo.movies.data.model.MovieSummary;
import com.alexbarcelo.movies.data.model.PaginatedList;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class PopularMoviesPresenter implements PopularMoviesContract.Presenter {

    @Inject MoviesRepository mRepository;

    @Nullable private PopularMoviesContract.View mView;

    private int mLastLoadedPage = 0;
    private int mPageCount = Integer.MAX_VALUE;
    private List<MovieSummary> mMovies = new ArrayList<>();
    private boolean mCanLoadData = true;
    private Disposable mCurrentRequest = null;

    @Inject
    PopularMoviesPresenter() { }

    @Override
    public void loadMovies() {
        if (mLastLoadedPage < mPageCount && mCanLoadData) {
            mCanLoadData = false;
            if (mView != null) {
                mView.showLoadingMoreItemsIndicator(true);
            }
            mCurrentRequest = new DisposableSingleObserver<PaginatedList<MovieSummary>>() {
                @Override
                public void onSuccess(PaginatedList<MovieSummary> movieSummaryPaginatedList) {
                    //Update view
                    mMovies.addAll(movieSummaryPaginatedList.results());
                    if (mView != null) {
                        mView.showMovies(mMovies);
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
            mRepository.getPopularMovies(mLastLoadedPage + 1)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((DisposableSingleObserver) mCurrentRequest);
        }
    }

    @Override
    public void retryMovieLoading() {
        if (mView != null) {
            mView.showRetryButton(false);
            mCanLoadData = true;
            loadMovies();
        }
    }

    @Override
    public void takeView(PopularMoviesContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
