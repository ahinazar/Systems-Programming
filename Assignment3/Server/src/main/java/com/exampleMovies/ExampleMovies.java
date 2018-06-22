
package com.exampleMovies;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExampleMovies {

    @SerializedName("movies")
    @Expose
    private List<Movie> movies = null;

    public ExampleMovies() {
    }

    public ExampleMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public List<Movie> getMovies() {
        return movies;
    }

}
