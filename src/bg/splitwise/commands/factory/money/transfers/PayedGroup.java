package bg.splitwise.commands.factory.money.transfers;

import java.io.PrintWriter;

import bg.splitwise.commands.factory.Command;
import bg.splitwise.payments.User;
import bg.splitwise.server.Server;

public class PayedGroup implements Command {

    @Override
    public void execute(String clientUsername, String content, Server SplitWise, PrintWriter writer) {
        String[] tokens = content.split(" ");

        int index = 0;
        if (tokens.length >= 4) {
            double amount = Double.parseDouble(tokens[index++]);
            String username = tokens[index++];
            String groupName = tokens[index];

            index = tokens[0].length() + username.length() + groupName.length() + 3;
            String reason = content.substring(index);

            User creditor = SplitWise.getRegisteredUser(clientUsername);
            creditor.getGroupPaid(username, groupName, amount, reason);
            SplitWise.addRegisteredUser(creditor);

            User debtor = SplitWise.getRegisteredUser(username);
            debtor.payGroup(username, groupName, amount, reason);
            SplitWise.addRegisteredUser(debtor);
            
            writer.println("=> you payed group successfully");
        }
    }

}
