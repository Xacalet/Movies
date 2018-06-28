package com.alexbarcelo.movies.data;

import com.alexbarcelo.movies.data.model.MovieSummary;
import com.alexbarcelo.movies.data.model.PaginatedList;

import io.reactivex.Single;

/**
 * Interface to a repository of movies
 */
public interface MoviesRepository {
    Single<PaginatedList<MovieSummary>> searchMoviesByText(int page, String searchText);

    Single<PaginatedList<MovieSummary>> getPopularMovies(int page);
}
