import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
                String response = reader.readLine();

                if (response.equals("disconnect")) {
                    System.out.println("Client " + username + " sends exit...");
                    System.out.println("Closing this connection.");

                    socket.close();
                    writer.close();
                    reader.close();

                    System.out.println("Connection closed");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
