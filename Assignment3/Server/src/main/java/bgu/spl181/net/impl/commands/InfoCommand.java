package bgu.spl181.net.impl.commands;


import bgu.spl181.net.impl.MoviesDB;
import bgu.spl181.net.impl.UsersDB;

public class InfoCommand extends RequestCommand {

    String movieName;

    public InfoCommand(UsersDB UsersDB, MoviesDB MoviesDB, int connectionId, String[] commandParameters, String movieName) {
        super(UsersDB, MoviesDB, connectionId, commandParameters);
        this.movieName = movieName;
    }

    @Override
    public String run() {
        String movieName = null;
        if (commandParameters.length == 2)
            movieName = commandParameters[1];
        return infoCommand(movieName);
    }

    private String infoCommand(String movieName) {
        if (movieName == null) {//prints all movies
            MoviesDB.lock.writeLock().lock();
            String allMoviesString = MoviesDB.allMoviesInDBNames();
            MoviesDB.lock.writeLock().unlock();
            return allMoviesString;
        } else {
            MoviesDB.lock.writeLock().lock();
            if (MoviesDB.isMovieExists(movieName)) {
                String msg = "ACK info \"" + movieName + "\" " + MoviesDB.movieAvaiablaAmount(movieName) + " " + MoviesDB.movieprice(movieName) +
                        " " + MoviesDB.allBannedCountries(movieName);
                MoviesDB.lock.writeLock().unlock();
                return msg.trim();
            }
            return "ERROR request info failed";
        }
    }
}
