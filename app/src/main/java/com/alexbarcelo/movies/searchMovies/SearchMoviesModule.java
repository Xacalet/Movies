package com.alexbarcelo.movies.searchMovies;

import com.alexbarcelo.commons.di.ActivityScoped;
import com.alexbarcelo.commons.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SearchMoviesModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SearchMoviesFragment searchMoviesFragment();

    @ActivityScoped
    @Binds
    abstract SearchMoviesContract.Presenter searchMoviesPresenter(SearchMoviesPresenter presenter);
}
