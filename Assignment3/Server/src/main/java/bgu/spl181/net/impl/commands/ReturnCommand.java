package bgu.spl181.net.impl.commands;

import bgu.spl181.net.impl.MoviesDB;
import bgu.spl181.net.impl.UsersDB;

public class ReturnCommand extends RequestCommand {

    String movieName;

    public ReturnCommand(UsersDB UsersDB, MoviesDB MoviesDB, int connectionId, String[] commandParameters, String movieName) {
        super(UsersDB, MoviesDB, connectionId, commandParameters);
        this.movieName = movieName;
    }

    @Override
    public String run() {
        if (MoviesDB.isMovieExists(movieName)) {
            MoviesDB.lock.writeLock().lock();
            UsersDB.lock.writeLock().lock();
            if (UsersDB.haveThisMovie(connectionId, movieName)) {
                UsersDB.userReturnAMovie(connectionId, movieName);
                MoviesDB.userReturnUpdatesAvailablaAmount(movieName);
                MoviesDB.updateJsonMovies();
                UsersDB.updateJsonUsers();

                UsersDB.lock.writeLock().unlock();
                MoviesDB.lock.writeLock().unlock();

                return "ACK return \"" + movieName + "\" success";
            }
            UsersDB.lock.writeLock().unlock();
            MoviesDB.lock.writeLock().unlock();
        }
        return "ERROR request return failed";
    }
}
