package bg.splitwise.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import bg.splitwise.commands.factory.Command;
import bg.splitwise.commands.factory.CommandFactory;

public class ClientConnectionHandler implements Runnable {

    private CommandFactory factory;
    private Server SplitWise;
    private PrintWriter writer;
    private BufferedReader reader;
    private Socket socket;
    private String username;

    public ClientConnectionHandler(Socket socket, String username, Server SplitWise) {
        try {
            this.socket = socket;
            this.username = username;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.factory = new CommandFactory();
            this.SplitWise = SplitWise;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String line = reader.readLine();
                String[] splitLine = line.split(" ");

                if ("disconnect".equals(splitLine[0])) {
                    System.out.println("=> client " + username + " wants to disconnect...");
                    System.out.println("=> closing this connection.");

                    socket.close();
                    writer.close();
                    reader.close();

                    System.out.println("=> connection closed");
                    break;

                }

                Command command = factory.getCommand(line);
                String content = factory.getContent(line);

                command.execute(username, content, SplitWise, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
