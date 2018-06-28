package com.alexbarcelo.movies.data;

import com.alexbarcelo.movies.data.model.MovieSummary;
import com.alexbarcelo.movies.data.model.PaginatedList;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface that provides a set of methods to access data from The Movie Database through its REST API
 */

public interface TMDBAPI {
    @GET("/3/search/movie")
    Single<PaginatedList<MovieSummary>> searchMovies(@Query("api_key") String apiKey,
                                                     @Query("language") String language,
                                                     @Query("page") int page,
                                                     @Query("query") String query);

    @GET("/3/movie/popular")
    Single<PaginatedList<MovieSummary>> getPopularMovies(@Query("api_key") String apiKey,
                                                         @Query("language") String language,
                                                         @Query("page") int page);
}
