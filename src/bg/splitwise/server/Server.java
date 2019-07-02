package bg.splitwise.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import bg.splitwise.payments.User;

public class Server {

    private static final int PORT = 8080;

    private static HashSet<User> registeredUsers = new HashSet<>();
    private static ConcurrentHashMap<String, HashSet<String>> friendLists = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, HashSet<String>> groups = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        new Server().run();
    }

    private void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.printf("server is running on localhost:%d%n", PORT);

            retrieveRegisteredUsers();
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client " + socket.getInetAddress() + " connected to server");

                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String line = reader.readLine();
                String[] splitLine = line.split(" ");

                int index = 0;
                String command = splitLine[index++];

                if (command.equals("connect")) {
                    String username = splitLine[index];

                    if (containsUser(username)) {
                        writer.println("=> you must login now");
                    } else {
                        writer.println("=> you are connected as a guest, you must register to use SplitWise");
                    }

                    ClientConnectionHandler runnable = new ClientConnectionHandler(socket, username, this);
                    new Thread(runnable).start();
                }
            }

        } catch (IOException e) {
            System.out.println("another server is running on port 8080");
        }

    }

    private void retrieveRegisteredUsers() {
        String path = new String("SplitWise\\Server");

        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e1) {
            e1.getMessage();
        }

        File file = new File("SplitWise\\Server\\registeredUsers.txt");

        if (file.exists()) {
            try (FileReader fr = new FileReader(file.getAbsoluteFile());
                    BufferedReader bufferedReader = new BufferedReader(fr)) {

                if (!file.exists()) {
                    file.createNewFile();

                } else {
                    String line = new String("");
                    
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] tokens = line.split(" ");

                        String username = tokens[1];
                        String password = tokens[3];

                        registeredUsers.add(new User(username, password));
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addRegisteredUser(User newUser) {
        registeredUsers.add(newUser);
    }

    public boolean containsUser(String username) {
        for (User user : registeredUsers) {
            if (user.getUsername().equals(username)) {
                System.out.println("user: " + user.getUsername());
                System.out.println("username: " + username);
                return true;
            }
        }

        return false;
    }

    public boolean containsAccount(String username, String password) {
        return getRegisteredUser(username).hasPassword(password);
    }

    public User getRegisteredUser(String username) {
        for (User user : registeredUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void createFriendList(String username) {
        friendLists.put(username, new HashSet<>());
    }

    public boolean areFriends(String username, String friend) {
        for (String friendUsername : friendLists.get(username)) {
            if (friendUsername.equals(friend)) {
                return true;
            }
        }

        return false;
    }

    public void addFriend(String username, String friend) {
        HashSet<String> friendList1 = friendLists.get(username);
        friendList1.add(friend);
        friendLists.put(username, friendList1);
    }

    public void addUsernameToFile(String path, String username) {
        File file = new File(path);
        try (FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                BufferedWriter bufferedWriter = new BufferedWriter(fw)) {

            if (!file.exists()) {
                file.createNewFile();
            }

            bufferedWriter.write("username: " + username);
            bufferedWriter.newLine();

        } catch (IOException e) {
            e.getMessage();
        }
    }

    public boolean containsGroup(String name) {
        return groups.containsKey(name);
    }

    public void addGroup(String name, HashSet<String> members) {
        groups.put(name, members);
    }

    public HashSet<String> getGroup(String groupName) {
        return groups.get(groupName);
    }

    public HashSet<String> getFriendList(String username) {
        return friendLists.get(username);
    }
}
