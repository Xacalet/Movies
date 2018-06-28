package com.alexbarcelo.movies.di;

import com.alexbarcelo.movies.data.MoviesRepository;
import com.alexbarcelo.movies.data.TMDBRepository;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class MovieRepositoryModule {
    @Binds
    abstract MoviesRepository bindRepository(TMDBRepository repository);
}
