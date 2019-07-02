package bg.splitwise.commands.factory.users.registration;

import java.io.PrintWriter;

import bg.splitwise.commands.factory.Command;
import bg.splitwise.server.Server;

public class Login implements Command {

    @Override
    public void execute(String clientUsername, String content, Server SplitWise, PrintWriter writer) {
        String[] tokens = content.split(" ");

        int index = 0;
        if (tokens.length == 2) {
            String username = tokens[index++];
            String password = tokens[index];

            if (SplitWise.containsUser(username) && SplitWise.containsAccount(username, password)) {
                System.out.println(username + " logged in");
                writer.println("=> successfully logged in");

                // notifications
            } else {
                writer.println("=> wrong username or password (or you are not registered)");

            }

        } else {
            writer.println("=> invalid input");
        }

    }

}
