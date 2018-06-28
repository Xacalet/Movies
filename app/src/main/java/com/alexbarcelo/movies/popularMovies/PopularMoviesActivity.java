package com.alexbarcelo.movies.popularMovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.alexbarcelo.movies.R;
import com.alexbarcelo.movies.searchMovies.SearchMoviesActivity;

import dagger.android.support.DaggerAppCompatActivity;

public class PopularMoviesActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        PopularMoviesFragment fragment = (PopularMoviesFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);

        if (fragment == null) {
            fragment = new PopularMoviesFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popular_movies_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.open_movie_search_menu_item) {
            Intent intent = new Intent(this, SearchMoviesActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
