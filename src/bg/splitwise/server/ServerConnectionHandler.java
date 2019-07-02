package bg.splitwise.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerConnectionHandler implements Runnable {

    private Socket socket;

    public ServerConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            while (!socket.isClosed()) {

                System.out.println(reader.readLine());
                System.out.println();
            }

            System.out.println("client socket is closed, stop waiting for server messages");
            System.out.println();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println();
        }
    }
}
