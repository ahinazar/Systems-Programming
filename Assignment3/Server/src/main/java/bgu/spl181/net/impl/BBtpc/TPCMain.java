package bgu.spl181.net.impl.BBtpc;

import bgu.spl181.net.api.MessageEncoderDecoder;
import bgu.spl181.net.api.bidi.BidiMessagingProtocol;
import bgu.spl181.net.impl.BBProtocol;
import bgu.spl181.net.impl.EncoderDecoder;
import bgu.spl181.net.impl.MoviesDB;
import bgu.spl181.net.impl.UsersDB;
import bgu.spl181.net.srv.Server;
import com.exampleMovies.ExampleMovies;
import com.exampleMovies.Movie;
import com.exampleUsers.ExampleUsers;
import com.exampleUsers.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class TPCMain {
    private static ExampleMovies exampleMovies;
    private static ExampleUsers exampleUsers;
    private static MoviesDB MovieDB;
    private static UsersDB UserDB;
    private static ConcurrentHashMap<String, Movie> concurrentHashMapMovies = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, User> concurrentHashMapUsers = new ConcurrentHashMap<>();


    public static void main(String[] args) {
        initObjsFromDB();

        Supplier<MessageEncoderDecoder<String>> encdecFactory = new Supplier<MessageEncoderDecoder<String>>() {

            @Override
            public MessageEncoderDecoder<String> get() {
                // TODO Auto-generated method stub
                return new EncoderDecoder();
            }
        };

        Supplier<BidiMessagingProtocol<String>> protocolFactory = new Supplier<BidiMessagingProtocol<String>>() {

            @Override
            public BidiMessagingProtocol<String> get() {
                // TODO Auto-generated method stub
                return (BidiMessagingProtocol<String>) new BBProtocol(MovieDB, UserDB);
            }
        };

        int port = 7777;
        Server<String> srv = Server.threadPerClient(port, protocolFactory, encdecFactory);
        srv.serve();
    }

    private static void initObjsFromDB() {
        String workingDir = System.getProperty("user.dir");
        File f1 = new File(workingDir + "/Database/Movies.json");
        File f2 = new File(workingDir + "/Database/Users.json");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(f1));
            Gson gsonMovies = new GsonBuilder().create();
            exampleMovies = gsonMovies.fromJson(reader, ExampleMovies.class);

            reader = new BufferedReader(new FileReader(f2));
            Gson gsonUsers = new GsonBuilder().create();
            exampleUsers = gsonUsers.fromJson(reader, ExampleUsers.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Movie movietoadd : exampleMovies.getMovies()) {
            concurrentHashMapMovies.put(movietoadd.getName().toLowerCase(), movietoadd);
        }
        for (User usertoadd : exampleUsers.getUsers()) {
            concurrentHashMapUsers.put(usertoadd.getUsername().toLowerCase(), usertoadd);
        }
        MovieDB = new MoviesDB(concurrentHashMapMovies);
        UserDB = new UsersDB(concurrentHashMapUsers);
    }
}