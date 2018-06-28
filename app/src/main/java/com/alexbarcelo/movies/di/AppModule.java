package com.alexbarcelo.movies.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class AppModule {
    @Binds
    abstract Context bindContext(Application application);


    @Provides
    @Named("url")
    static String providesUrl() {
        return "https://api.themoviedb.org/";
    }
}
