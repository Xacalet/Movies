package com.alexbarcelo.movies.popularMovies;

import com.alexbarcelo.commons.mvp.BasePresenter;
import com.alexbarcelo.commons.mvp.BaseView;
import com.alexbarcelo.movies.data.model.MovieSummary;

import java.util.List;

public interface PopularMoviesContract {
    interface View extends BaseView<Presenter> {
        void showMovies(List<MovieSummary> movies);

        void showLoadingMoreItemsIndicator(boolean active);

        void showRetryButton(boolean active);

        void showError(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void loadMovies();

        void retryMovieLoading();
    }
}
