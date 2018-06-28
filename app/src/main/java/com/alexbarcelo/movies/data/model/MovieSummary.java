package com.alexbarcelo.movies.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import javax.annotation.Nullable;

/**
 * Class that represents a movie in The Movie DataBase API
 */
@AutoValue
public abstract class MovieSummary {
    public abstract String title();

    @SerializedName("release_date")
    public abstract String releaseDate();

    public abstract String overview();

    @SerializedName("poster_path")
    @Nullable
    public abstract String posterPath();

    public static TypeAdapter<MovieSummary> typeAdapter(Gson gson) {
        return new AutoValue_MovieSummary.GsonTypeAdapter(gson);
    }

    public static MovieSummary create(String title, String releaseDate, String overview, String posterPath) {
        return new AutoValue_MovieSummary(title, releaseDate, overview, posterPath);
    }
}
