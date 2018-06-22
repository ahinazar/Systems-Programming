
package com.exampleUsers;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExampleUsers {

    @SerializedName("users")
    @Expose
    private List<User> users = null;

    public ExampleUsers() {
    }

    public ExampleUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

}
