package bg.splitwise.commands.factory.money.transfers;

import java.io.PrintWriter;

import bg.splitwise.commands.factory.Command;
import bg.splitwise.payments.User;
import bg.splitwise.server.Server;

public class Payed implements Command {

    @Override
    public void execute(String clientUsername, String content, Server SplitWise, PrintWriter writer) {

        String[] tokens = content.split(" ");

        int index = 0;
        if (tokens.length >= 3) {
            double amount = Double.parseDouble(tokens[index++]);
            String username = tokens[index];

            index = tokens[0].length() + username.length() + 2;
            String reason = content.substring(index);

            User creditor = SplitWise.getRegisteredUser(clientUsername);
            creditor.getFriendPaid(username, amount, reason);
            SplitWise.addRegisteredUser(creditor);

            User debtor = SplitWise.getRegisteredUser(username);
            debtor.payFriend(clientUsername, amount, reason);
            SplitWise.addRegisteredUser(debtor);

            writer.println("=> friend payed you successfully");
        } else {
            writer.println("=> invalid input");
        }
    }

}
