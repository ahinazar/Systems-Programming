package bgu.spl181.net.impl.commands;

import bgu.spl181.net.impl.MoviesDB;
import bgu.spl181.net.impl.UsersDB;
import com.exampleMovies.Movie;

import java.util.List;

public class AddmovieCommand extends RequestCommand {

    String movieName;
    Integer price;
    List<String> bannedCountries;
    Integer totalAmount;


    public AddmovieCommand(UsersDB UsersDB, MoviesDB MoviesDB, int connectionId, String[] commandParameters, String movieName, Integer price, List<String> bannedCountries, Integer totalAmount) {
        super(UsersDB, MoviesDB, connectionId, commandParameters);
        this.movieName = movieName;
        this.bannedCountries = bannedCountries;
        this.price = price;
        this.totalAmount = totalAmount;
    }

    @Override
    public String run() {
        if (totalAmount > 0 && price > 0) {
            MoviesDB.lock.writeLock().lock();
            if (!MoviesDB.isMovieExists(movieName)) {
                MoviesDB.addNewMovie(movieName, new Movie(movieName, price, bannedCountries, totalAmount));
                MoviesDB.updateJsonMovies();
                MoviesDB.lock.writeLock().unlock();
                return "ACK addmovie \"" + movieName + "\" success";
            }
            MoviesDB.lock.writeLock().unlock();
        }
        return "ERROR request addmovie failed";
    }
}
