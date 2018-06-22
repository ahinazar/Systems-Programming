package bgu.spl181.net.impl.commands;

import bgu.spl181.net.impl.MoviesDB;
import bgu.spl181.net.impl.UsersDB;

public abstract class RequestCommand implements ExecuteCommand {

    UsersDB UsersDB;
    MoviesDB MoviesDB;
    int connectionId;
    String[] commandParameters;

    public RequestCommand(UsersDB UsersDB, MoviesDB MoviesDB, int connectionId, String[] commandParameters) {
        this.UsersDB = UsersDB;
        this.MoviesDB = MoviesDB;
        this.connectionId = connectionId;
        this.commandParameters = commandParameters;
    }
}
