package com.alexbarcelo.movies.di;

import android.app.Application;

import com.alexbarcelo.commons.network.NetworkModule;
import com.alexbarcelo.commons.rxjava.SchedulersModule;
import com.alexbarcelo.movies.CustomApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Dagger component that injects the module dependencies on each activity
 */

@Singleton
@Component(modules = {
        AppModule.class,
        NetworkModule.class,
        ActivityBindingModule.class,
        MovieRepositoryModule.class,
        SchedulersModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<CustomApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}