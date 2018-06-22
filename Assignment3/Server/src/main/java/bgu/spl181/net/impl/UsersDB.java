package bgu.spl181.net.impl;

import com.exampleUsers.ExampleUsers;
import com.exampleUsers.Movie;
import com.exampleUsers.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UsersDB {

    String workingDir = System.getProperty("user.dir");
    ConcurrentHashMap<Integer, String> loggedInUsers = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, User> UsersDB;
    public ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public ReentrantReadWriteLock lockuser = new ReentrantReadWriteLock();

    public UsersDB(ConcurrentHashMap<String, User> UsersDB) {
        this.UsersDB = UsersDB;
    }

    public void updateJsonUsers() {
        ExampleUsers exampleUsers = new ExampleUsers(new ArrayList<User>(UsersDB.values()));
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            Writer writer = new FileWriter(workingDir + "/Database/Users.json");
            gson.toJson(exampleUsers, writer);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRegistered(String userName) {
        return UsersDB.containsKey(userName.toLowerCase());
    }

    public void loginUser(String userName, int connectionId) {
        loggedInUsers.put(connectionId, userName.toLowerCase());
    }

    public void logoutUser(int connectionId) {
        loggedInUsers.remove(connectionId);
    }

    public boolean isLoggedIn(String userName) {
        return loggedInUsers.containsValue(userName.toLowerCase());
    }

    public boolean isPasswordMatches(String userName, String password) {
        return UsersDB.get(userName.toLowerCase()).getPassword().equals(password);
    }

    public boolean isSomeoneLoggedInConnectionId(int connectionId) {
        return loggedInUsers.containsKey(connectionId);
    }

    public void registerNewUser(String username, User user) {
        UsersDB.put(username.toLowerCase(), user);
    }

    public Integer getBalance(int connectionId) {
        return UsersDB.get(loggedInUsers.get(connectionId)).getBalance();
    }

    public void setBalance(int connectionId, int newBalance) {
        UsersDB.get(loggedInUsers.get(connectionId)).setBalance(newBalance);
    }

    public void userRentAMovie(int connectionId, int newBalance, Movie movieToAdd) {
        UsersDB.get(loggedInUsers.get(connectionId)).setBalance(newBalance);
        UsersDB.get(loggedInUsers.get(connectionId)).getMovies().add(movieToAdd);
    }

    public boolean canRent(int connectionId, List<String> bannedCountries, String movieName) {
        if (UsersDB.get(loggedInUsers.get(connectionId)).getCountry() != null && bannedCountries != null) {
            LinkedList<String> lowercaseBannedCountries = new LinkedList<>();
            for (String bannedCountryName : bannedCountries) {
                lowercaseBannedCountries.add(bannedCountryName.toLowerCase());
            }
            return ((!haveThisMovie(connectionId, movieName.toLowerCase()) && !(bannedCountries.contains(UsersDB.get(loggedInUsers.get(connectionId)).getCountry().toLowerCase()))));
        }
        return !haveThisMovie(connectionId, movieName.toLowerCase());
    }

    public boolean haveThisMovie(int connectionId, String movieName) {
        for (Movie movies : UsersDB.get(loggedInUsers.get(connectionId)).getMovies())
            if (movieName.toLowerCase().equals(movies.getName().toLowerCase()))
                return true;
        return false;
    }

    public void userReturnAMovie(int connectionId, String movieName) {
        User currUser = UsersDB.get(loggedInUsers.get(connectionId));
        if (currUser.getMovies() != null)
            for (Movie movies : currUser.getMovies())
                if (movieName.toLowerCase().equals(movies.getName().toLowerCase()))
                    currUser.getMovies().remove(movies);

    }

    public boolean isAdmin(int connectionId) {
        return UsersDB.get(loggedInUsers.get(connectionId)).getType().equalsIgnoreCase("admin");
    }

}
