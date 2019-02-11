import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import utilities.Pair;

public class Server {

    private static final int PORT = 8080;

    private static ConcurrentHashMap<String, Pair<String, Socket>> users = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, ArrayList<String>> friendLists = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, ArrayList<String>> groups = new ConcurrentHashMap<>();
    
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.printf("server is running on localhost:%d%n", PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client " + socket.getInetAddress() + " connected to server");

                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line = reader.readLine();
                String[] splitLine = line.split(" ");

                int index = 0;
                String command = splitLine[index++];
                System.out.println("command: "+command);
                if (command.equals("register")) {

                    if (splitLine.length == 3) {
                        String username = splitLine[index++];
                        System.out.println("username: " + username);
                        String password = splitLine[index];
                        System.out.println("password: " + password);

                        if (!users.containsKey(username)) {
                            users.put(username, new Pair<String, Socket>(password, socket));
                            // add to file
                            writer.println("Registered successfully");
                            System.out.println(username + " registered");
                        } else {
                            writer.println("Username already taken");
                            socket.close();
                        }

                    } else {
                        writer.println("Invalid input");
                    }
                    
                    continue;
                } else if (command.equals("login")) {

                    if (splitLine.length == 3) {
                        String username = splitLine[index++];
                        String password = splitLine[index];
                        System.out.println("username: "+username+" password: "+password);
                        System.out.println("users.containsKey(username): "+users.containsKey(username));
                        System.out.println("users.get(username).getFirst().equals(password): "+users.get(username).getFirst().equals(password));
                        System.out.println("users.get(username).getFirst(): "+users.get(username).getFirst());
                        
                        if (users.containsKey(username) && users.get(username).getFirst().equals(password)) {

                            writer.println(username + " logged in");

                            ClientConnectionHandler runnable = new ClientConnectionHandler(socket, username);
                            new Thread(runnable).start();

                        } else {
                            writer.println("Wrong username or password (or you are not registered)");
                        }
                    } else {
                        writer.println("Invalid input");
                    }
                    continue;
                }
            }

        } catch (IOException e) {
            System.out.println("another server is running on port 8080");
        }
    }

}
