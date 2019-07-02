package bg.splitwise.commands.factory.users.interactions;

import java.io.PrintWriter;
import java.util.HashSet;

import bg.splitwise.commands.factory.Command;
import bg.splitwise.payments.User;
import bg.splitwise.server.Server;

public class GetStatus implements Command {

    @Override
    public void execute(String clientUsername, String content, Server SplitWise, PrintWriter writer) {
        
        writer.println("Friends:");

        User current=SplitWise.getRegisteredUser(clientUsername);
        HashSet<String> friends = SplitWise.getFriendList(clientUsername);
        for (String friend : friends) {
            double obligation=current.getMyFriendObligationTo(friend);
             
            if(obligation>0) {
                writer.println("You owe "+friend+" "+Double.toString(obligation));
            }else if(obligation<0) {
                writer.println(friend+" owes you "+Double.toString((-obligation)));
            }
        }
        writer.println();
        
        writer.println("Groups:");
        for (String friend : friends) {
            double obligation=current.getMyGroupObligationTo(friend);
             
            if(obligation>0) {
                writer.println("You owe "+friend+" "+Double.toString(obligation));
            }else if(obligation<0) {
                writer.println(friend+" owes you "+Double.toString((-obligation)));
            }
        }
        writer.println();
    }

}
