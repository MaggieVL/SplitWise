package bg.splitwise.commands.factory.users.interactions;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;

import bg.splitwise.commands.factory.Command;
import bg.splitwise.server.Server;

public class CreateGroup implements Command {

    @Override
    public void execute(String clientUsername, String content, Server SplitWise, PrintWriter writer) {
        String[] tokens = content.split(" ");

        int index = 0;
        if (tokens.length >= 4) {
            String name = tokens[index++];
            HashSet<String> members = new HashSet<>();

            for (int i = index; i < tokens.length; i++) {
                members.add(tokens[i]);
            }

            if (SplitWise.containsGroup(name)) {
                writer.println("=> group name already used");

            } else {
                SplitWise.addGroup(name, members);

                try {
                    String path = new String("SplitWise\\Server\\Groups");
                    Files.createDirectories(Paths.get(path));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("could not create a Groups directory");
                }

                String path = "SplitWise\\Server\\Groups\\" + name + "Group.txt";
                for (String member : members) {
                    SplitWise.addUsernameToFile(path, member); // or addUsernamesToFile(String path, HashSet<String>
                                                               // users);
                }

                writer.println("=> group " + name + " created successfully");
            }

        } else {
            writer.println("=> invalid input - groups must contain at least 3 people");
        }

    }

}
