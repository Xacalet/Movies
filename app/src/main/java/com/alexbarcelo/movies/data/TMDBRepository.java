package com.alexbarcelo.movies.data;

import com.alexbarcelo.movies.data.model.MovieSummary;
import com.alexbarcelo.movies.data.model.PaginatedList;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.Retrofit;

/**
 * Implementation of {@link MoviesRepository } that retrieves data from The Movie DataBase API
 */
public class TMDBRepository implements MoviesRepository {

    public static final String MOVIE_DATABASE_API_KEY = "93aea0c77bc168d8bbce3918cefefa45";
    public static final String POSTER_IMAGES_BASE_URL = "https://image.tmdb.org/t/p/w200";

    private TMDBAPI mAPIService;
    private String mLanguage = "en-US";

    @Inject
    public TMDBRepository(Retrofit retrofit) {
        mAPIService = retrofit.create(TMDBAPI.class);
    }

    @Override
    public Single<PaginatedList<MovieSummary>> searchMoviesByText(int page, String searchText) {
        return mAPIService.searchMovies(MOVIE_DATABASE_API_KEY, this.mLanguage, page, searchText);
    }

    @Override
    public Single<PaginatedList<MovieSummary>> getPopularMovies(int page) {
        return mAPIService.getPopularMovies(MOVIE_DATABASE_API_KEY, this.mLanguage, page);
    }
}
