package bg.splitwise.commands.factory;

import java.io.PrintWriter;

import bg.splitwise.server.Server;

public interface Command {
    public void execute(String clientUsername, String content, Server SplitWise, PrintWriter writer);
}
