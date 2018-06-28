package com.alexbarcelo.commons.rxjava;

import com.alexbarcelo.commons.rxjava.schedulers.BaseSchedulerProvider;
import com.alexbarcelo.commons.rxjava.schedulers.SchedulerProvider;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public abstract class SchedulersModule {

    @Provides
    @Named("AndroidScheduler")
    static Scheduler providesAndroidScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Named("ProcessScheduler")
    static Scheduler providesProcessScheduler() {
        return Schedulers.newThread();
    }

    @Provides
    static BaseSchedulerProvider providesSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

}
