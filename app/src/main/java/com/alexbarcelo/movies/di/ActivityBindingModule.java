package com.alexbarcelo.movies.di;

import com.alexbarcelo.commons.di.ActivityScoped;
import com.alexbarcelo.movies.popularMovies.PopularMoviesActivity;
import com.alexbarcelo.movies.popularMovies.PopularMoviesModule;
import com.alexbarcelo.movies.searchMovies.SearchMoviesActivity;
import com.alexbarcelo.movies.searchMovies.SearchMoviesModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
    Dagger module that will inject dependencies into activities
 */
@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = SearchMoviesModule.class)
    abstract SearchMoviesActivity searchMoviesActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = PopularMoviesModule.class)
    abstract PopularMoviesActivity popularMoviesActivity();
}
