package bgu.spl181.net.impl;

import bgu.spl181.net.api.bidi.Connections;
import bgu.spl181.net.srv.ConnectionHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConnectionsImpl<T> implements Connections<T> {
    private HashMap<Integer, ConnectionHandler<T>> connectionHandlerHashMap = new HashMap<Integer, ConnectionHandler<T>>();


    public int add(int connectionID, ConnectionHandler<T> connection) {
        connectionHandlerHashMap.put(connectionID, connection);
        return connectionID;
    }

    public ConnectionHandler get(int connectionID) {
        return connectionHandlerHashMap.get(connectionID);
    }


    /**
     * Sends a message to a given client (The connections are stored as field)
     *
     * @param connectionId - the destination to send to
     * @param msg          - the message to send
     * @return true if the message was sent
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean send(int connectionId, Object msg) {
        try {
            connectionHandlerHashMap.get(connectionId).send((T) msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean thereIsActiveConnection() {
        if (!connectionHandlerHashMap.keySet().isEmpty())
            return true;
        return false;
    }


    /**
     * Sends a message to all of the connections
     *
     * @param msg - the messege to broadcast
     */
    @SuppressWarnings("unchecked")
    @Override
    public void broadcast(Object msg) {
        for (Map.Entry<Integer, ConnectionHandler<T>> entry : connectionHandlerHashMap.entrySet()) {
            ((ConnectionHandler<T>) entry).send((T) msg);
        }
    }


    /**
     * Find the connection by the connectionId given, close it, and then remove it from the queue.
     *
     * @param connectionId - the Id to find&remove
     */
    @Override
    public void disconnect(int connectionId) {
        ConnectionHandler<T> handler = connectionHandlerHashMap.remove(connectionId);
        if (handler != null)
            try {
                handler.close();
            } catch (IOException e) {
                System.out.println("Can't close handler");
            }
    }


    public HashMap getConnections() {
        return connectionHandlerHashMap;
    }

}
