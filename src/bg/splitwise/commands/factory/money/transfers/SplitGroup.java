package bg.splitwise.commands.factory.money.transfers;

import java.io.PrintWriter;
import java.util.HashSet;

import bg.splitwise.commands.factory.Command;
import bg.splitwise.payments.Payment;
import bg.splitwise.payments.User;
import bg.splitwise.server.Server;

public class SplitGroup implements Command {

    @Override
    public void execute(String clientUsername, String content, Server SplitWise, PrintWriter writer) {
        String[] tokens = content.split(" ");

        int index = 0;
        if (tokens.length >= 3) {
            double amount = Double.parseDouble(tokens[index++]);
            String groupName = tokens[index];
            index = tokens[0].length() + groupName.length() + 2;
            String reason = content.substring(index);

            HashSet<String> group = SplitWise.getGroup(groupName);
            amount = amount / (double) group.size();

            Payment newPayment = new Payment(amount, reason, groupName);
            User creditor = SplitWise.getRegisteredUser(clientUsername);

            for (String member : group) {
                User debtor = SplitWise.getRegisteredUser(member);
                debtor.addPaymentOwedByMe(clientUsername, newPayment);
                SplitWise.addRegisteredUser(debtor);

                creditor.addPaymentOwedToMe(member, newPayment);
            }

            SplitWise.addRegisteredUser(creditor);
            writer.println("=> amount split successfully");
        } else {
            writer.println("=> invalid input");
        }

    }

}
