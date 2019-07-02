package bg.splitwise.commands.factory.users.registration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import bg.splitwise.commands.factory.Command;
import bg.splitwise.payments.User;
import bg.splitwise.server.Server;

public class Register implements Command {

    @Override
    public void execute(String clientUsername, String content, Server SplitWise, PrintWriter writer) {
        String[] tokens = content.split(" ");

        int index = 0;
        if (tokens.length == 2) {
            String username = tokens[index++];
            String password = tokens[index];

            if (!SplitWise.containsUser(username)) {
                SplitWise.addRegisteredUser(new User(username, password));

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

                SplitWise.createFriendList(username);
                
                System.out.println(username + " registered");
                writer.println("=> registered successfully");

            } else {
                writer.println("=> username already in database");
            }

        } else {
            writer.println("=> invalid input");
        }

    }

}
