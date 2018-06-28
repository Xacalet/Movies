package com.alexbarcelo.movies.searchMovies;

import com.alexbarcelo.commons.mvp.BasePresenter;
import com.alexbarcelo.commons.mvp.BaseView;
import com.alexbarcelo.movies.data.model.MovieSummary;

import java.util.List;

public interface SearchMoviesContract {
    interface View extends BaseView<Presenter> {
        void updateSearchResults(List<MovieSummary> movies);

        void showLoadingMoreItemsIndicator(boolean active);

        void showRetryButton(boolean active);

        void showError(String message);

        void showNoResultsMessage(boolean active);

        void clearSearchResults();
    }

    interface Presenter extends BasePresenter<View> {
        void searchMovies(String searchText, boolean newSearch);

        void retryMovieSearch(String searchText);
    }
}
