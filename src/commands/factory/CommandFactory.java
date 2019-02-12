package commands.factory;

import java.util.HashMap;

import commands.factory.money.transfers.Payed;
import commands.factory.money.transfers.PayedGroup;
import commands.factory.money.transfers.Split;
import commands.factory.money.transfers.SplitGroup;
import commands.factory.users.interactions.AddFriend;
import commands.factory.users.interactions.CreateGroup;
import commands.factory.users.interactions.GetStatus;
import commands.factory.users.interactions.showHistory;
import commands.factory.users.registration.Connect;
import commands.factory.users.registration.Login;
import commands.factory.users.registration.Register; 

public class CommandFactory {
    private static HashMap<String, Command> commands;

    static {
        commands = new HashMap<>();

        commands.put("connect", new Connect());
        commands.put("register", new Register());
        commands.put("login", new Login());
        commands.put("add-friend", new AddFriend());
        commands.put("create-group", new CreateGroup());
        commands.put("show-history", new showHistory());
        commands.put("split-group", new SplitGroup());
        commands.put("payed", new Payed());
        commands.put("payed-group", new PayedGroup());
        commands.put("get-status", new GetStatus());
    }

    public Command getCommand(String commandLine) {
        commandLine=commandLine.trim();
        String[] commandLineContents = commandLine.split(" ");

        return commands.get(commandLineContents[0]);
    }

    public String getContent(String commandLine) {
        commandLine=commandLine.trim();
        String[] commandLineContents = commandLine.split(" ");

        int len = commandLineContents[0].length();

        return commandLine.substring(len);

    }
}
