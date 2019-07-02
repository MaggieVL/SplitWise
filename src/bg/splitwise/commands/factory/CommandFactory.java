package bg.splitwise.commands.factory;

import java.util.HashMap;

import bg.splitwise.commands.factory.money.transfers.Payed;
import bg.splitwise.commands.factory.money.transfers.PayedGroup;
import bg.splitwise.commands.factory.money.transfers.Split;
import bg.splitwise.commands.factory.money.transfers.SplitGroup;
import bg.splitwise.commands.factory.users.interactions.AddFriend;
import bg.splitwise.commands.factory.users.interactions.CreateGroup;
import bg.splitwise.commands.factory.users.interactions.GetStatus;
import bg.splitwise.commands.factory.users.interactions.ShowHistory;
import bg.splitwise.commands.factory.users.registration.Login;
import bg.splitwise.commands.factory.users.registration.Register;

public class CommandFactory {
    private static HashMap<String, Command> commands;

    static {
        commands = new HashMap<>();

        commands.put("register", new Register());
        commands.put("login", new Login());
        commands.put("add-friend", new AddFriend());
        commands.put("create-group", new CreateGroup());
        commands.put("show-history", new ShowHistory());
        commands.put("split-group", new SplitGroup());
        commands.put("payed", new Payed());
        commands.put("payed-group", new PayedGroup());
        commands.put("get-status", new GetStatus());
        commands.put("split", new Split());
    }

    public Command getCommand(String commandLine) {
        commandLine = commandLine.trim();
        String[] commandLineContents = commandLine.split(" ");

        return commands.get(commandLineContents[0]);
    }

    public String getContent(String commandLine) {
        commandLine = commandLine.trim();
        String[] commandLineContents = commandLine.split(" ");

        int length = commandLineContents[0].length();

        if (commandLineContents[0].equals("get-status") || commandLineContents[0].equals("show-history")) {
            return null;
        }

        return commandLine.substring(length + 1);

    }
}
