package com.alexbarcelo.movies.popularMovies;

import com.alexbarcelo.commons.di.ActivityScoped;
import com.alexbarcelo.commons.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class PopularMoviesModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract PopularMoviesFragment popularMoviesFragment();

    @ActivityScoped
    @Binds
    abstract PopularMoviesContract.Presenter popularMoviesPresenter(PopularMoviesPresenter presenter);
}
