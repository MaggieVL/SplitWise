import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

// class Group extended by Friends
// add factory for the commands 
// add data for groups, friends etc to files
// group files and directories better
// create exceptions
// use more lambda functions
//make a separate function for writing to files

public class Server {

    private static final int PORT = 8080;

    private static ConcurrentHashMap<String, String> registeredUsers = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Socket> onlineUsers = new ConcurrentHashMap<>();
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

                if (command.equals("connect")) {
                    String username = splitLine[index];
                    onlineUsers.put(username, socket);

                    if (registeredUsers.containsKey(username)) {
                        writer.println("=> you must login now");
                    } else {
                        writer.println("=> you are connected as a guest, you must register to use SplitWise");
                    }

                    ClientConnectionHandler runnable = new ClientConnectionHandler(socket, username);
                    new Thread(runnable).start();
                }
            }

        } catch (IOException e) {
            System.out.println("another server is running on port 8080");
        }
    }

    public static void register(String username, String password, PrintWriter writer) {

        if (!registeredUsers.containsKey(username)) {
            registeredUsers.put(username, password);

            try {
                // create Server directory
                String path = new String("SplitWise\\Server");
                Files.createDirectories(Paths.get(path));

                // create user directory
                path = "SplitWise\\Server\\" + username;
                Files.createDirectories(Paths.get(path));

            } catch (IOException e) {
                e.printStackTrace();
            }

            // add user to file
            File file = new File("SplitWise\\Server\\registeredUsers.txt");
            try (FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fw)) {

                if (!file.exists()) {
                    file.createNewFile();
                }

                bufferedWriter.write("username: " + username + " ");
                bufferedWriter.write("password: " + password);
                bufferedWriter.newLine();

            } catch (IOException e) {
                e.printStackTrace();
            }

            // thread safe ArrayList<String>
            // Collection<String> friendList = Collections.synchronizedCollection(new
            // ArrayList<>());
            friendLists.put(username, new ArrayList<>());

            System.out.println(username + " registered");
            writer.println("=> registered successfully");

        } else {
            writer.println("=> username already in database");
        }

    }

    public static void login(String username, String password, PrintWriter writer) {
        if (registeredUsers.containsKey(username) && registeredUsers.get(username).equals(password)) {
            System.out.println(username + " logged in");
            writer.println("=> successfully logged in");

            // notifications
        } else {
            writer.println("=> wrong username or password (or you are not registered)");

        }
    }

    @SuppressWarnings({ "unlikely-arg-type", "unused" })
    private static boolean hasFriend(String username, String friend) {
        return friendLists.get(username).stream().equals(friend);
    }

    public static void addFriendTo(String username, String friend, PrintWriter writer) {
        // add to map
        // add to file
        // check if registered

        if (!registeredUsers.containsKey(username)) {
            writer.println("=> you must register first");

        } else if (!registeredUsers.containsKey(friend)) {
            writer.println("=> " + friend + " is not a registered user");

        } else if (hasFriend(username, friend)) {
            writer.println("=> friend already in friend list");

        } else if (username.equals(friend)) {
            writer.println("=> you cannot add yourself as a friend");
        } else {

            // add friend to username map
            ArrayList<String> friendList1 = friendLists.get(username);
            friendList1.add(friend);
            friendLists.put(username, friendList1);

            // add username to friend map
            ArrayList<String> friendList2 = friendLists.get(friend);
            friendList2.add(username);
            friendLists.put(friend, friendList2);

            // add friend to username file
            File usernameFile = new File("SplitWise\\Server\\" + username + "\\friendList.txt");
            try (FileWriter fw = new FileWriter(usernameFile.getAbsoluteFile(), true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fw)) {

                if (!usernameFile.exists()) {
                    usernameFile.createNewFile();
                }

                bufferedWriter.write("username: " + friend);
                bufferedWriter.newLine();

            } catch (IOException e) {
                // e.printStackTrace();
                e.getMessage();
            }

            // add username to friend file
            File friendFile = new File("SplitWise\\Server\\" + friend + "\\friendList.txt");
            try (FileWriter fw = new FileWriter(friendFile.getAbsoluteFile(), true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fw)) {

                if (!friendFile.exists()) {
                    friendFile.createNewFile();
                }

                bufferedWriter.write("username: " + username);
                bufferedWriter.newLine();

            } catch (IOException e) {
                e.getMessage();
            }

            writer.println("=> " + friend + " added successfully to friend list");
        }
    }

    public static void createGroup(String name, ArrayList<String> members, PrintWriter writer) {
        if (groups.containsKey(name)) {
            writer.println("=> group name already used");

        } else {
            // add to map
            groups.put(name, members);

            System.out.println("in server: ");
            System.out.println("name: " + name);
            for (int i = 0; i < members.size(); i++) {
                System.out.println("member[" + i + "]= " + members.get(i));
            }

            System.out.print("members==null?: ");
            System.out.println(members == null);

            // add directory
            String path = new String("SplitWise\\Server\\Groups");
            try {
                Files.createDirectories(Paths.get(path));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // add to file
            File file = new File("SplitWise\\Server\\Groups\\" + name + "Group.txt");
            try (FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fw)) {

                if (!file.exists()) {
                    file.createNewFile();
                }

                for (String member : members) {
                    bufferedWriter.write("username: " + member);
                    bufferedWriter.newLine();
                }

            } catch (IOException e) {
                e.getMessage();
            }

            writer.println("=> group " + name + " created successfully");
        }
    }
}
