
package com.exampleUsers;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("movies")
    @Expose
    private ConcurrentLinkedQueue<Movie> movies = null;
    @SerializedName("balance")
    @Expose
    private String balance;

    public User(String username, String password, String country) {
        this.username = username;
        this.type = "normal";
        this.password = password;
        this.movies = new ConcurrentLinkedQueue<>();
        this.balance = String.valueOf(0);
        this.country = country;
    }

    public String getUsername() {
        return username;
    }

    public String getType() {
        return type;
    }

    public String getPassword() {
        return password;
    }

    public String getCountry() {
        return country;
    }

    public ConcurrentLinkedQueue<Movie> getMovies() {
        return movies;
    }

    public Integer getBalance() {
        return Integer.valueOf(balance);
    }

    public void setBalance(Integer balance) {
        this.balance = String.valueOf(balance);
    }

}
