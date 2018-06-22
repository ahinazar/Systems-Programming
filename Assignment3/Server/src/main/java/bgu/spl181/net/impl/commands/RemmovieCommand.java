package bgu.spl181.net.impl.commands;

import bgu.spl181.net.impl.MoviesDB;
import bgu.spl181.net.impl.UsersDB;

public class RemmovieCommand extends RequestCommand {

    String movieName;

    public RemmovieCommand(UsersDB UsersDB, MoviesDB MoviesDB, int connectionId, String[] commandParameters, String movieName) {
        super(UsersDB, MoviesDB, connectionId, commandParameters);
        this.movieName = movieName;
    }


    @Override
    public String run() {
        MoviesDB.lock.writeLock().lock();
        if (MoviesDB.isMovieExists(movieName) && MoviesDB.movieAvaiablaAmount(movieName) == MoviesDB.movieTotalAmount(movieName)) {
            MoviesDB.removeMovie(movieName);
            MoviesDB.updateJsonMovies();
            MoviesDB.lock.writeLock().unlock();
            return "ACK remmovie \"" + movieName + "\" success";
        }
        MoviesDB.lock.writeLock().unlock();
        return "ERROR request remmovie failed";
    }
}
