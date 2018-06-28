package com.alexbarcelo.movies.searchMovies;

import android.os.Bundle;

import com.alexbarcelo.movies.R;

import dagger.android.support.DaggerAppCompatActivity;

public class SearchMoviesActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movies);

        SearchMoviesFragment fragment = (SearchMoviesFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);

        if (fragment == null) {
            fragment = new SearchMoviesFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, fragment)
                    .commit();
        }
    }
}
