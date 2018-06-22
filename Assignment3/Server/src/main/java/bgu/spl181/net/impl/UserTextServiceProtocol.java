package bgu.spl181.net.impl;

import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.impl.commands.LogInCommand;
import bgu.spl181.net.impl.commands.RegisterCommand;
import bgu.spl181.net.impl.commands.SignOutCommand;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserTextServiceProtocol implements BidiMessagingProtocol {
    protected UsersDB UsersDB;
    protected ConnectionsImpl connections;
    protected int connectionId;
    private AtomicBoolean shouldTerminate = new AtomicBoolean(false);

    public UserTextServiceProtocol(UsersDB UsersDB) {
        this.UsersDB = UsersDB;
    }

    @Override
    public void start(int connectionId, Connections connections) {
        this.connections = (ConnectionsImpl) connections;
        this.connectionId = connectionId;
    }

    @Override
    public void process(Object message) {
        String[] commandName = removeRedandentCharsAndSplitBySpace(message);
        switch (commandName[0].toUpperCase()) {
            case ("REGISTER"): {
                if (commandName.length == 4) {
                    String country = commandName[3];
                    connections.send(connectionId, new RegisterCommand(UsersDB, commandName[1], commandName[2], country).run());
                } else {
                    connections.send(connectionId, "ERROR registration failed");
                }
                break;
            }
            case ("LOGIN"): {
                if (commandName.length == 3) {
                    connections.send(connectionId, new LogInCommand(commandName[1], commandName[2], UsersDB, connectionId).run());
                } else {
                    connections.send(connectionId, "ERROR login failed");
                }
                break;
            }
            case ("SIGNOUT"): {
                if (commandName.length == 1) {
                    String messageBack = new SignOutCommand(UsersDB, connectionId).run();
                    connections.send(connectionId, messageBack);
                    if (((String) message).split(" ")[0].equals("ACK") && !connections.thereIsActiveConnection()) {
                        shouldTerminate.set(true);
                    }
                } else {
                    connections.send(connectionId, "ERROR signout failed");
                }
                break;
            }
        }
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate.get();
    }

    public String[] removeRedandentCharsAndSplitBySpace(Object message) {
        boolean country = false;
        String msg = ((String) message).replace("\n", "").replace("\r", "").trim();
        String[] checkForRegister = msg.split(" ");
        LinkedList<String> stringsCollector = new LinkedList();
        stringsCollector.add(checkForRegister[0]);
        if (checkForRegister.length == 1)
            msg = "";
        else
            msg = msg.substring(checkForRegister[0].length()); //removing the first word- the command
        int i = 0;
        boolean trimCountry = false;

        while (i < msg.length()) {
            if (msg.split(" ").length == 4 && checkForRegister[0].equalsIgnoreCase("register")) {
                String[] msgParts = msg.split(" ");
                String supposedToBeCountry = msgParts[3];
                if (supposedToBeCountry.contains("=")) {
                    String[] splitByEq = supposedToBeCountry.split("=");
                    if (!splitByEq[0].equalsIgnoreCase("country")) {
                        trimCountry = true;
                        msg = " " + msgParts[1] + " " + msgParts[2];
                        break;
                    }
                }
            }
            if (checkForRegister[0].equalsIgnoreCase("register") && checkForRegister.length >= 4 && msg.substring(i, i + 2).equalsIgnoreCase(" c")) {
                String tmp = "";
                int j = ++i;
                while (j < msg.length() && i + 9 > j) {
                    tmp = tmp + msg.charAt(j);
                    j++;
                }
                if (tmp.equalsIgnoreCase("country=\"")) {
                    country = true;
                    msg = msg.substring(i + 8); //8?
                    msg = " " + msg;
                    i = 0;
                }

            }
            try {


                if (msg.substring(i, i + 2).equalsIgnoreCase(" \"")) {
                    String tmp = "";
                    i += 2;
                    while (i < msg.length()) {
                        if (i < msg.length() - 2 && msg.substring(i, i + 2).equalsIgnoreCase("\" ")) {
                            i++;
                            break;
                        }
                        if (i == msg.length() - 2 && msg.substring(i, i + 2).equalsIgnoreCase("\" ")) {
                            i = msg.length();
                            break;
                        }
                        if (i == msg.length() - 1) {
                            i++;
                            break;
                        }
                        tmp = tmp + msg.charAt(i);
                        i++;
                    }
                    stringsCollector.add(tmp);
                } else if (msg.charAt(i) == ' ' && (msg.charAt(i + 1) != '\"')) {
                    i++;
                    String tmp = "";
                    while (i < msg.length() && msg.charAt(i) != ' ') {
                        tmp = tmp + msg.charAt(i);
                        i++;
                    }
                    stringsCollector.add(tmp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        String[] finalArray;

        if (checkForRegister[0].equalsIgnoreCase("register") && !country && checkForRegister.length > 1 && !trimCountry) {
            finalArray = new String[stringsCollector.size() - 1];
            stringsCollector.remove(stringsCollector.size() - 1);
        } else {
            finalArray = new String[stringsCollector.size()];
        }

        i = 0;
        try {
            while (!stringsCollector.isEmpty()) {
                finalArray[i] = stringsCollector.remove();
                finalArray[i].trim();
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return finalArray;
    }
}