package bgu.spl181.net.impl.commands;

import bgu.spl181.net.impl.MoviesDB;
import bgu.spl181.net.impl.UsersDB;

public class ChangepriceCommand extends RequestCommand {

    String movieName;
    Integer price;

    public ChangepriceCommand(UsersDB UsersDB, MoviesDB MoviesDB, int connectionId, String[] commandParameters, String movieName, Integer price) {
        super(UsersDB, MoviesDB, connectionId, commandParameters);
        this.movieName = movieName;
        this.price = price;
    }

    @Override
    public String run() {
        if (price > 0) {
            MoviesDB.lock.writeLock().lock();
            if (MoviesDB.isMovieExists(movieName)) {
                MoviesDB.setMovieprice(movieName, price);
                MoviesDB.updateJsonMovies();
                MoviesDB.lock.writeLock().unlock();
                return "ACK changeprice \"" + movieName + "\" success";
            }
            MoviesDB.lock.writeLock().unlock();
        }
        return "ERROR request changeprice failed";
    }
}
