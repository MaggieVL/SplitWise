import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    // private BufferedReader reader;
    private PrintWriter writer;

    public static void main(String[] args) throws IOException {
        new Client().run();
    }

    public void run() throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String line = scanner.nextLine();

                int index = 0;

                String[] splitLine = line.split(" ");
                String command = splitLine[index++];

                if ("register".equals(command)) {
                    if (splitLine.length == 5) {
                        String host = splitLine[index++];
                        int port = Integer.parseInt(splitLine[index++]);
                        String username = splitLine[index++];
                        String password = splitLine[index];

                        register(host, port, username, password);
                    } else {
                        System.out.println("Invalid input");
                    }
                    continue;
                }

                if (writer != null) {
                    if ("disconnect".equals(command)) {

                        writer.println("disconnect");
                        writer.close();
                        break;
                    } else {// a server command is received

                        writer.println(line);
                        continue;
                    }
                }else {
                    System.out.println("You must register first");
                }
            }
        }
    }

    private void register(String host, int port, String username, String password) {
        try {// Server.containsUser() or:
            Socket socket = new Socket(host, port);
            writer = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("username: " + username);
            System.out.println("password: " + password);
            writer.println("register " + username + " " + password);

            ServerConnectionHandler runnable = new ServerConnectionHandler(socket);
            new Thread(runnable).start();

        } catch (IOException e) {
            System.out.println("=> cannot connect to server on localhost:8080, make sure that the server is started");
        }
    }
}
