
package com.exampleUsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    public Movie(Integer id, String name) {
        this.id = String.valueOf(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
