package bgu.spl181.net.impl.commands;

import bgu.spl181.net.impl.MoviesDB;
import bgu.spl181.net.impl.UsersDB;

public class RentCommand extends RequestCommand {

    String movieName;

    public RentCommand(UsersDB UsersDB, MoviesDB MoviesDB, int connectionId, String[] commandParameters, String movieName) {
        super(UsersDB, MoviesDB, connectionId, commandParameters);
        this.movieName = movieName;
    }

    @Override
    public String run() {
        if (MoviesDB.isMovieExists(movieName)) {
            int price = MoviesDB.movieprice(movieName);
            int availableAmount = MoviesDB.movieAvaiablaAmount(movieName);
            int userBalance = UsersDB.getBalance(connectionId);
            if (availableAmount > 0 && userBalance >= price) {
                MoviesDB.lock.writeLock().lock();
                UsersDB.lock.writeLock().lock();
                if (UsersDB.canRent(connectionId, MoviesDB.getBannedContries(movieName), movieName)) {
                    MoviesDB.userRentUpdatesAvailablaAmount(movieName);
                    com.exampleUsers.Movie movieToAdd = new com.exampleUsers.Movie(MoviesDB.getId(movieName), movieName);
                    UsersDB.userRentAMovie(connectionId, userBalance - price, movieToAdd);
                    MoviesDB.updateJsonMovies();
                    UsersDB.updateJsonUsers();

                    UsersDB.lock.writeLock().unlock();
                    MoviesDB.lock.writeLock().unlock();
                    return "ACK rent \"" + movieName + "\" success";
                }
                UsersDB.lock.writeLock().unlock();
                MoviesDB.lock.writeLock().unlock();
            }
        }
        return "ERROR request rent failed";
    }
}

