/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alexbarcelo.movies.searchMovies;

import com.alexbarcelo.commons.rxjava.schedulers.BaseSchedulerProvider;
import com.alexbarcelo.commons.rxjava.schedulers.ImmediateSchedulerProvider;
import com.alexbarcelo.movies.data.MoviesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.TestScheduler;

import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of {@link SearchMoviesPresenter}
 */
public class SearchMoviesPresenterTest {

    @Mock
    SearchMoviesContract.View mView;

    @Mock
    MoviesRepository mRepository;

    BaseSchedulerProvider schedulerProvider;

    SearchMoviesPresenter mSearchMoviesPresenter;

    @Before
    public void setupMocksAndView() {
        MockitoAnnotations.initMocks(this);
        schedulerProvider = new ImmediateSchedulerProvider();
        mSearchMoviesPresenter = new SearchMoviesPresenter(mRepository, schedulerProvider);
    }

    @Test
    public void searchMovies_displaysData() {
        mSearchMoviesPresenter.takeView(mView);

        String query = "f";
        mSearchMoviesPresenter.searchMovies(query, true);

        verify(mRepository).searchMoviesByText(0, query);
    }
}
