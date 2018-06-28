package com.alexbarcelo.movies.di;

import com.alexbarcelo.movies.data.MoviesRepository;
import com.alexbarcelo.movies.data.model.MovieSummary;
import com.alexbarcelo.movies.data.model.PaginatedList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Single;

public class FakeMovieRepositoryImpl implements MoviesRepository {

    public final static int PAGE_SIZE = 20;
    public final static int TOTAL_PAGES = 3;
    public final static int LATENCY_IN_MS = 2000;
    public final static String QUERY_WITH_NO_RESULTS = "xxx";
    public final static String QUERY_WITH_NO_NETWORK = "net";
    public final static String SEARCH_RESULTS_TITLE_FORMAT = "SEARCHED MOVIE #%s";
    public final static String SEARCH_RESULTS_OVERVIEW_FORMAT = "OVERVIEW %s";
    public final static String POPULAR_MOVIES_TITLE_FORMAT = "POPULAR MOVIE #%s";
    public final static String POPULAR_MOVIES_OVERVIEW_FORMAT = "OVERVIEW";
    public final static String RELEASE_DATE = "1999-01-01";

    @Inject
    public FakeMovieRepositoryImpl() {
    }

    @Override
    public Single<PaginatedList<MovieSummary>> searchMoviesByText(int page, String searchText) {
        // If search text = QUERY_WITH_NO_RESULTS, returns an empty list
        List<MovieSummary> results = (searchText.equalsIgnoreCase(QUERY_WITH_NO_RESULTS)) ? new ArrayList() : createSearchResults(page, searchText);
        Single<PaginatedList<MovieSummary>> observable;
        // If search text = QUERY_WITH_NO_NETWORK, returns an error
        if (searchText.equalsIgnoreCase(QUERY_WITH_NO_NETWORK))
            observable = Single.error(new Exception("Connection error on fake repository"));
        else
            observable = Single.just(PaginatedList.create(TOTAL_PAGES, results));
        return observable.delay(LATENCY_IN_MS, TimeUnit.MILLISECONDS);
    }

    @Override
    public Single<PaginatedList<MovieSummary>> getPopularMovies(int page) {
        List<MovieSummary> results = createPopularMovies(page);
        Single<PaginatedList<MovieSummary>> observable = Single.just(PaginatedList.create(TOTAL_PAGES, results));
        return observable.delay(LATENCY_IN_MS, TimeUnit.MILLISECONDS);
    }

    private List<MovieSummary> createSearchResults(int page, String searchText) {
        List<MovieSummary> results = new ArrayList();
        for (int i = 0; i < PAGE_SIZE; i++) {
            int x = ((page - 1) * PAGE_SIZE) + i + 1;
            MovieSummary movie = MovieSummary.create(
                    String.format(SEARCH_RESULTS_TITLE_FORMAT, String.valueOf(x)),
                    RELEASE_DATE,
                    String.format(SEARCH_RESULTS_OVERVIEW_FORMAT, searchText),
                    "");
            results.add(movie);
        }
        return results;
    }

    private List<MovieSummary> createPopularMovies(int page) {
        List<MovieSummary> results = new ArrayList();
        for (int i = 0; i < PAGE_SIZE; i++) {
            int x = ((page - 1) * PAGE_SIZE) + i + 1;
            MovieSummary movie = MovieSummary.create(
                    String.format(POPULAR_MOVIES_TITLE_FORMAT, String.valueOf(x)),
                    RELEASE_DATE,
                    POPULAR_MOVIES_OVERVIEW_FORMAT,
                    "");
            results.add(movie);
        }
        return results;
    }
}
