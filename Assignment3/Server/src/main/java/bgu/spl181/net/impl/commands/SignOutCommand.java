package bgu.spl181.net.impl.commands;

import bgu.spl181.net.impl.UsersDB;

public class SignOutCommand implements ExecuteCommand {

    UsersDB UsersDB;
    int connectionId;

    public SignOutCommand(UsersDB UsersDB, int connectionId) {
        this.UsersDB = UsersDB;
        this.connectionId = connectionId;
    }

    @Override
    public String run() {
        if (UsersDB.isSomeoneLoggedInConnectionId(connectionId)) {
            UsersDB.logoutUser(connectionId);
            return "ACK signout succeeded";
        }
        return "ERROR signout failed";
    }
}
