package bgu.spl181.net.impl.commands;

import bgu.spl181.net.impl.UsersDB;

public class LogInCommand implements ExecuteCommand {
    UsersDB UsersDB;
    String password;
    String userName;
    int connectionId;

    public LogInCommand(String userName, String password, UsersDB UsersDB, int connectionId) {
        this.UsersDB = UsersDB;
        this.userName = userName;
        this.password = password;
        this.connectionId = connectionId;
    }


    @Override
    public String run() {
        if (UsersDB.isRegistered(userName) && UsersDB.isPasswordMatches(userName, password) && !UsersDB.isSomeoneLoggedInConnectionId(connectionId)) {
            UsersDB.lock.writeLock().lock();
            if (!UsersDB.isLoggedIn(userName)) {
                UsersDB.loginUser(userName, connectionId);
                UsersDB.lock.writeLock().unlock();
                return "ACK login succeeded";
            }
            UsersDB.lock.writeLock().unlock();
        }
        return "ERROR login failed";
    }
}
