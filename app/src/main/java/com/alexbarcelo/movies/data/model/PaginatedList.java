package com.alexbarcelo.movies.data.model;

import com.alexbarcelo.movies.data.MoviesRepository;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Class that represents a movie in The Movie DataBase API
 */
@AutoValue
public abstract class PaginatedList<T> {

    @SerializedName("total_pages")
    public abstract int totalPages();

    public abstract List<T> results();

    public static <T> TypeAdapter<PaginatedList<T>> typeAdapter(Gson gson, TypeToken<? extends PaginatedList<T>> typeToken) {
        return new AutoValue_PaginatedList.GsonTypeAdapter(gson, typeToken);
    }


    public static <T> PaginatedList create(int totalPages, List<T> results) {
        return new AutoValue_PaginatedList(totalPages, results);
    }
}
