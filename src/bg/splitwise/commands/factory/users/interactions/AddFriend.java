package bg.splitwise.commands.factory.users.interactions;

import java.io.PrintWriter;

import bg.splitwise.commands.factory.Command;
import bg.splitwise.server.Server;

public class AddFriend implements Command {

    @Override
    public void execute(String clientUsername, String content, Server SplitWise, PrintWriter writer) {
        String[] tokens = content.split(" ");

        int index = 0;
        if (tokens.length == 1) {
            String friend = tokens[index];

            if (!SplitWise.containsUser(clientUsername)) {
                writer.println("=> you must register first");

            } else if (!SplitWise.containsUser(friend)) {
                writer.println("=> " + friend + " is not a registered user");

            } else if (SplitWise.areFriends(clientUsername, friend)) {
                writer.println("=> friend already in friend list");

            } else if (clientUsername.equals(friend)) {
                writer.println("=> you cannot add yourself as a friend");

            } else {

                SplitWise.addFriend(clientUsername, friend);
                SplitWise.addFriend(friend, clientUsername);

                String path = "SplitWise\\Server\\" + clientUsername + "\\friendList.txt";
                SplitWise.addUsernameToFile(path, friend);

                path = "SplitWise\\Server\\" + friend + "\\friendList.txt";
                SplitWise.addUsernameToFile(path, clientUsername);

                writer.println("=> " + friend + " added successfully to friend list");
            }
        } else {
            writer.println("=> invalid input");
        }

    }

}
