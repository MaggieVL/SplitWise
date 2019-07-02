package bg.splitwise.commands.factory.money.transfers;

import java.io.PrintWriter;

import bg.splitwise.commands.factory.Command;
import bg.splitwise.payments.Payment;
import bg.splitwise.payments.User;
import bg.splitwise.server.Server;

public class Split implements Command {
    
    @Override
    public void execute(String clientUsername, String content, Server SplitWise, PrintWriter writer) {
        String[] tokens = content.split(" ");

        int index = 0;
        if (tokens.length >= 3) {
            double amount = Double.parseDouble(tokens[index++]);
            String username = tokens[index];
            index = tokens[0].length() + username.length() + 2;
            String reason = content.substring(index);

            amount = amount / 2;
            if (!SplitWise.areFriends(clientUsername, username)) {
                writer.println("=> you can split sums only with friends");

            } else {
                
                Payment newPayment = new Payment(amount, reason, "friend");

                User creditor = SplitWise.getRegisteredUser(clientUsername);
                creditor.addPaymentOwedToMe(username, newPayment);
                SplitWise.addRegisteredUser(creditor);

                User debtor = SplitWise.getRegisteredUser(username);
                debtor.addPaymentOwedByMe(clientUsername, newPayment);
                SplitWise.addRegisteredUser(debtor);

                writer.println("=> amount split successfully");

            }
        } else {
            writer.println("=> invalid input");
        }

    }

}
