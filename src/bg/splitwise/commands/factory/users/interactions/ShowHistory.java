package bg.splitwise.commands.factory.users.interactions;

import java.io.PrintWriter;
import java.util.ArrayList;

import bg.splitwise.commands.factory.Command;
import bg.splitwise.payments.Payment;
import bg.splitwise.payments.User;
import bg.splitwise.server.Server;

public class ShowHistory implements Command {

    @Override
    public void execute(String clientUsername, String content, Server SplitWise, PrintWriter writer) {
        User current = SplitWise.getRegisteredUser(clientUsername);
        ArrayList<Payment> history = current.getPaymentHistory();

        for (Payment payment : history) {
            writer.println("You got payed "+payment.getAmount()+" because of "+payment.getReason());
        }

    }

}
