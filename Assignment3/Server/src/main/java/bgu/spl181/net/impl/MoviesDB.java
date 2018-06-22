package bgu.spl181.net.impl;

import com.exampleMovies.ExampleMovies;
import com.exampleMovies.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MoviesDB {

    String workingDir = System.getProperty("user.dir");
    ConcurrentHashMap<String, Movie> MoviesDB;
    AtomicInteger idFactory;
    public static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public MoviesDB(ConcurrentHashMap<String, Movie> MoviesDB) {
        this.MoviesDB = MoviesDB;
    }

    public void updateJsonMovies() {
        ExampleMovies exampleMovies = new ExampleMovies(new ArrayList<Movie>(MoviesDB.values()));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            Writer writer = new FileWriter(workingDir + "/Database/Movies.json");

            gson.toJson(exampleMovies, writer);

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String allMoviesInDBNames() {
        Collection<Movie> movies = MoviesDB.values();
        String moviesNameString = "ACK info ";
        for (Movie moviesname : movies) {
            moviesNameString = moviesNameString + "\"" + moviesname.getName() + "\" ";
        }
        return moviesNameString;
    }

    public String allBannedCountries(String movieName) {
        Movie requstedMovie = MoviesDB.get(movieName.toLowerCase());
        if (requstedMovie.getBannedCountries() != null) {
            String bannedContries = "";
            for (String bannedcountry : requstedMovie.getBannedCountries()) {
                bannedContries = bannedContries + "\"" + bannedcountry + "\" ";
            }
            return bannedContries;
        }
        return "";
    }

    public boolean isMovieExists(String movieName) {
        return MoviesDB.containsKey(movieName.toLowerCase());
    }

    public int movieAvaiablaAmount(String movieName) {
        return MoviesDB.get(movieName.toLowerCase()).getAvailableAmount();
    }

    public int movieTotalAmount(String movieName) {
        return MoviesDB.get(movieName.toLowerCase()).getTotalAmount();
    }

    public int movieprice(String movieName) {
        return MoviesDB.get(movieName.toLowerCase()).getPrice();
    }

    public List<String> getBannedContries(String movieName) {
        return MoviesDB.get(movieName.toLowerCase()).getBannedCountries();
    }

    public void userRentUpdatesAvailablaAmount(String movieName) {
        MoviesDB.get(movieName.toLowerCase()).setAvailableAmount(MoviesDB.get(movieName.toLowerCase()).getAvailableAmount() - 1);
    }

    public void userReturnUpdatesAvailablaAmount(String movieName) {
        MoviesDB.get(movieName.toLowerCase()).setAvailableAmount(MoviesDB.get(movieName.toLowerCase()).getAvailableAmount() + 1);
    }

    public int getId(String movieName) {
        return MoviesDB.get(movieName.toLowerCase()).getId();
    }

    public void addNewMovie(String movieName, Movie movie) {
        if (idFactory == null) {
            int max = 0;
            Collection<String> movies = MoviesDB.keySet();
            for (String movieRunner : movies) {
                if (MoviesDB.get(movieRunner).getId().intValue() > max) {
                    max = MoviesDB.get(movieRunner).getId().intValue();
                }
            }
            idFactory = new AtomicInteger(max);
        }
        movie.setId(idFactory.incrementAndGet());
        MoviesDB.put(movieName.toLowerCase(), movie);
    }

    public void removeMovie(String movieName) {
        if (idFactory != null && MoviesDB.get(movieName.toLowerCase()).getId() == idFactory.get()) {
            idFactory.decrementAndGet();
        }
        MoviesDB.remove(movieName.toLowerCase());
    }

    public void setMovieprice(String movieName, int price) {
        MoviesDB.get(movieName.toLowerCase()).setPrice(price);
    }

}
