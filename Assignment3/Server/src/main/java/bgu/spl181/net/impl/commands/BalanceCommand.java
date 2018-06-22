package bgu.spl181.net.impl.commands;

import bgu.spl181.net.impl.MoviesDB;
import bgu.spl181.net.impl.UsersDB;

public class BalanceCommand extends RequestCommand {


    public BalanceCommand(UsersDB UsersDB, MoviesDB MoviesDB, int connectionId, String[] commandParameters) {
        super(UsersDB, MoviesDB, connectionId, commandParameters);
    }

    @Override
    public String run() {
        UsersDB.lock.writeLock().lock();
        if (commandParameters.length == 2 || commandParameters.length == 3) {
            if (commandParameters[1].equalsIgnoreCase("info")) {
                if (commandParameters.length == 2) {
                    return balanceInfo();
                }
            } else if (commandParameters[1].equalsIgnoreCase("add")) { //locking user
                if (commandParameters.length == 3) {
                    int amount = Integer.parseInt(commandParameters[2]);
                    return balanceAdd(amount);
                }
            }
        }
        UsersDB.lock.writeLock().unlock();
        return "ERROR request balance failed";
    }

    private String balanceInfo() {
        Integer balance = UsersDB.getBalance(connectionId);
        UsersDB.lock.writeLock().unlock();
        return "ACK balance " + balance;
    }

    private String balanceAdd(int amount) {
        Integer balance = UsersDB.getBalance(connectionId);
        UsersDB.setBalance(connectionId, balance + amount);
        UsersDB.updateJsonUsers();
        UsersDB.lock.writeLock().unlock();
        return "ACK balance " + (balance + amount) + " added " + amount;
    }
}
