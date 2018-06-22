package bgu.spl181.net.impl;


import bgu.spl181.net.impl.commands.*;
import bgu.spl181.net.srv.ConnectionHandler;

import java.util.*;

public class BBProtocol extends UserTextServiceProtocol {

    private MoviesDB MovieDB;

    public BBProtocol(MoviesDB MovieDB, UsersDB UsersDB) {
        super(UsersDB);
        this.MovieDB = MovieDB;
    }

    public void process(Object message) {
        String[] commandName = removeRedandentCharsAndSplitBySpace(message);
        if (commandName[0].equalsIgnoreCase("REQUEST")) {
            String messageBack = requestCommand(UsersDB, MovieDB, connectionId, (removeRedandentCharsAndSplitBySpace(((String) message).substring(commandName[0].length() + 1))));
            connections.send(connectionId, messageBack);
            if (messageBack.split(" ")[0].equals("ACK") && neededBroadcast(messageBack.split(" ")[1])) {
                broadcastMessage(messageBack);
            }
        } else
            super.process(message);
    }

    public String requestCommand(UsersDB UsersDB, MoviesDB MoviesDB, int connectionId, String[] commandParameters) {
        if (UsersDB.isSomeoneLoggedInConnectionId(connectionId)) { //the user rgistered & loggedin
            switch (commandParameters[0].toLowerCase()) {
                case ("balance"): {
                    return new BalanceCommand(UsersDB, MoviesDB, connectionId, commandParameters).run();
                }
                case ("info"): {
                    String movieName = null;
                    if (commandParameters.length == 2)
                        movieName = commandParameters[1];
                    return new InfoCommand(UsersDB, MoviesDB, connectionId, commandParameters, movieName).run();
                }
                case ("rent"): {
                    if (commandParameters.length == 2) {
                        return new RentCommand(UsersDB, MoviesDB, connectionId, commandParameters, commandParameters[1]).run();
                    }
                    return "ERROR request rent failed";
                }
                case ("return"): {
                    if (commandParameters.length == 2)
                        return new ReturnCommand(UsersDB, MoviesDB, connectionId, commandParameters, commandParameters[1]).run();
                    return "ERROR request rent failed";
                }
                case ("addmovie"): {
                    if (UsersDB.isAdmin(connectionId) && commandParameters.length >= 4) {
                        List<String> bannedCountries = null;
                        if (commandParameters.length > 4) {
                            bannedCountries = new LinkedList<>();
                            for (int i = 4; i < commandParameters.length; i++) {
                                bannedCountries.add(commandParameters[i].replace("\"", ""));
                            }
                        }
                        return new AddmovieCommand(UsersDB, MoviesDB, connectionId, commandParameters, commandParameters[1], Integer.parseInt(commandParameters[3]), bannedCountries, Integer.parseInt(commandParameters[2])).run();
                    }
                    return "ERROR request addmovie failed";
                }
                case ("remmovie"): {
                    if (UsersDB.isAdmin(connectionId) && commandParameters.length == 2)
                        return new RemmovieCommand(UsersDB, MoviesDB, connectionId, commandParameters, commandParameters[1]).run();
                    return "ERROR request remmovie failed";
                }
                case ("changeprice"): {
                    if (UsersDB.isAdmin(connectionId) && commandParameters.length == 3)
                        return new ChangepriceCommand(UsersDB, MoviesDB, connectionId, commandParameters, commandParameters[1], Integer.parseInt(commandParameters[2])).run();
                    return "ERROR request changeprice failed";
                }
            }
        }
        return "ERROR request " + commandParameters[0].toLowerCase() + " failed";
    }

    private void broadcastMessage(Object msg) {
        String[] splitMsg = ((String) msg).split(" ");
        if (splitMsg[1].equalsIgnoreCase("remmovie")) {
            String movieName = new String(((String) msg).substring(((String) msg).indexOf("\"") + 1, ((String) msg).lastIndexOf("\"")));
            msg = "BROADCAST movie " + "\"" + movieName + "\"" + " removed";
        } else {
            String movieName = new String(((String) msg).substring(((String) msg).indexOf("\"") + 1, ((String) msg).lastIndexOf("\"")));
            msg = "BROADCAST movie " + "\"" + movieName + "\"" + " " + MovieDB.movieAvaiablaAmount(movieName.replace("\"", "")) + " " + MovieDB.movieprice(movieName.replace("\"", ""));
        }
        LinkedList connectionsId = new LinkedList();
        connectionsId.addAll(connections.getConnections().keySet());

        for (Object entry : connections.getConnections().values())
            if (UsersDB.isSomeoneLoggedInConnectionId((Integer) connectionsId.remove()))
                ((ConnectionHandler) entry).send(msg);
    }

    private boolean neededBroadcast(String commandName) {
        switch (commandName.toLowerCase()) {
            case ("rent"): {
                return true;
            }
            case ("return"): {
                return true;
            }
            case ("addmovie"): {
                return true;
            }
            case ("remmovie"): {
                return true;
            }
            case ("changeprice"): {
                return true;
            }
        }
        return false;
    }

}