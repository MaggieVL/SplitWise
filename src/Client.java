import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/*
    connect localhost 8080 Maggie
    register Maggie parolka
    login Maggie parolka
    
    connect localhost 8080 Tanya
    register Tanya stefko
    login Tanya stefko

    connect localhost 8080 Toni 
    register Toni Plamen
    login Toni Plamen
*/

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

                if ("connect".equals(command)) {
                    if (splitLine.length == 4) {
                        String host = splitLine[index++];
                        int port = Integer.parseInt(splitLine[index++]);
                        String username = splitLine[index++];

                        connect(host, port, username);

                    } else {
                        System.out.println("=> invalid input");
                        System.out.println();
                    }

                } else if ("disconnect".equals(command)) {
                    writer.println("disconnect");
                    writer.close();
                    break;

                } else {
                    writer.println(line);
                    continue;

                }

            }
        }
    }

    private void connect(String host, int port, String username) {
        try {
            Socket socket = new Socket(host, port);
            System.out.println("=> connected successfully");

            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("connect " + username);

            ServerConnectionHandler runnable = new ServerConnectionHandler(socket);
            new Thread(runnable).start();

        } catch (IOException e) {
            System.err.println("=> cannot connect to server on localhost:8080, make sure that the server is started");
            System.out.println();
        }

    }
}
