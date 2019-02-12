package bg.splitwise;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientConnectionHandler implements Runnable {

    private PrintWriter writer;
    private BufferedReader reader;
    private Socket socket;
    private String username;

    // Constructor
    public ClientConnectionHandler(Socket socket, String username) {
        try {
            this.socket = socket;
            this.username = username;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // commands
        try {
            while (true) {
                String line = reader.readLine();
                String[] splitLine = line.split(" ");

                int index = 0;
                String command = splitLine[index++];
                if ("disconnect".equals(command)) {
                    System.out.println("=> client " + username + " wants to disconnect...");
                    System.out.println("=> closing this connection.");

                    socket.close();
                    writer.close();
                    reader.close();

                    System.out.println("=> connection closed");
                    break;

                } else if ("register".equals(command)) {
                    if (splitLine.length == 3) {
                        String username = splitLine[index++];
                        String password = splitLine[index];

                        Server.register(username, password, writer);

                    } else {
                        writer.println("=> invalid input");
                    }

                } else if ("login".equals(command)) {
                    if (splitLine.length == 3) {
                        String username = splitLine[index++];
                        String password = splitLine[index];

                        Server.login(username, password, writer);
                    } else {
                        writer.println("=> invalid input");
                    }

                } else if ("add-friend".equals(command)) {
                    if (splitLine.length == 2) {
                        String friendUsername = splitLine[index];

                        Server.addFriendTo(username, friendUsername, writer);
                    } else {
                        writer.println("=> invalid input");
                    }
                } else if ("create-group".equals(command)) {
                    System.out.println("in thread: ");
                    System.out.println("splitLine.length: " + splitLine.length);

                    if (splitLine.length >= 5) {
                        String name = splitLine[index++];
                        ArrayList<String> members = new ArrayList<>();

                        for (int i = index; i < splitLine.length; i++) {
                            members.add(splitLine[i]);
                            System.out.println("member[" + i + "]= " + splitLine[i]);
                        }

                        Server.createGroup(name, members, writer);
                    } else {
                        writer.println("=> invalid input - groups must contain at least 3 people");
                    }
                } else if ("split".equals(command)) {
                    if (splitLine.length >= 4) {
                        double amount=Double.parseDouble(splitLine[index++]);
                        String username=splitLine[index];
                        index=command.length()+splitLine[1].length()+username.length()+3;
                        String reasonForPayment=line.substring(index);
                        
                        Server.split(amount, username, this.username, reasonForPayment, writer);
                    } else {
                        writer.println("=> invalid input");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
