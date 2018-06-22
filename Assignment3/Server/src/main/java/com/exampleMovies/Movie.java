
package com.exampleMovies;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("bannedCountries")
    @Expose
    private List<String> bannedCountries = null;
    @SerializedName("availableAmount")
    @Expose
    private String availableAmount;
    @SerializedName("totalAmount")
    @Expose
    private String totalAmount;

    public Movie(String movieName, Integer price, List<String> bannedCountries, Integer totalAmount) {
        this.id = String.valueOf(-1);
        this.name = movieName;
        this.price = String.valueOf(price);
        this.bannedCountries = bannedCountries;
        this.availableAmount = String.valueOf(totalAmount);
        this.totalAmount = String.valueOf(totalAmount);
    }

    public Integer getId() {
        return Integer.valueOf(id);
    }

    public void setId(Integer id) {
        this.id = String.valueOf(id);
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return Integer.valueOf(price);
    }

    public void setPrice(Integer price) {
        this.price = String.valueOf(price);
    }

    public List<String> getBannedCountries() {
        return bannedCountries;
    }

    public Integer getAvailableAmount() {
        return Integer.valueOf(availableAmount);
    }

    public void setAvailableAmount(Integer availableAmount) {
        this.availableAmount = String.valueOf(availableAmount);
    }

    public Integer getTotalAmount() {
        return Integer.valueOf(totalAmount);
    }

}
