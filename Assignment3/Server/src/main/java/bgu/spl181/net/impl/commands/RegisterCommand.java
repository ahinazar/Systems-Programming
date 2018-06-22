package bgu.spl181.net.impl.commands;

import bgu.spl181.net.impl.UsersDB;
import com.exampleUsers.User;

public class RegisterCommand implements ExecuteCommand {

    UsersDB UsersDB;
    String userName;
    String password;
    String country;

    public RegisterCommand(UsersDB UsersDB, String userName, String password, String country) {
        this.UsersDB = UsersDB;
        this.userName = userName;
        this.password = password;
        this.country = country;
    }

    @Override
    public String run() {
        UsersDB.lock.writeLock().lock();
        if (!UsersDB.isRegistered(userName) && !UsersDB.isLoggedIn(userName)) {
            UsersDB.registerNewUser(userName, new User(userName, password, country));
            UsersDB.updateJsonUsers();

            UsersDB.lock.writeLock().unlock();
            return "ACK registration succeeded";
        }
        UsersDB.lock.writeLock().unlock();
        return "ERROR registration failed";
    }
}
