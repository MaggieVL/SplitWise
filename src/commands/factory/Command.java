package commands.factory;

import bg.splitwise.Server;

public interface Command {
    public void execute(String content, Server SplitWise);
}
