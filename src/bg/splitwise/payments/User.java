package bg.splitwise.payments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class User {
    private String username;
    private String password;
    private Map<String, HashSet<Payment>> moneyOwedByMe;
    private Map<String, HashSet<Payment>> moneyOwedToMe;
    private ArrayList<Payment> paymentHistory;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.moneyOwedByMe = new HashMap<>();
        this.moneyOwedToMe = new HashMap<>();
        this.paymentHistory = new ArrayList<>();
    }

    public ArrayList<Payment> getPaymentHistory() {
        return paymentHistory;
    }

    public void getFriendPaid(String username, double amount, String reason) {
        HashSet<Payment> payments = moneyOwedToMe.get(username);

        String status = "friend";
        for (Payment payment : payments) {
            if (payment.getStatus().equals(status) && payment.getAmount() == amount
                    && payment.getReason().equals(reason)) {
                payments.remove(payment);

                break;
            }
        }

        moneyOwedToMe.put(username, payments);
    }

    public void payFriend(String username, double amount, String reason) {
        HashSet<Payment> payments = moneyOwedByMe.get(username);

        String status = "friend";
        for (Payment payment : payments) {
            if (payment.getStatus().equals(status) && payment.getAmount() == amount
                    && payment.getReason().equals(reason)) {
                payments.remove(payment);
                paymentHistory.add(payment);

                break;
            }
        }

        moneyOwedByMe.put(username, payments);
    }

    public void getGroupPaid(String username, String groupName, double amount, String reason) {
        HashSet<Payment> payments = moneyOwedToMe.get(username);

        for (Payment payment : payments) {
            if (payment.getStatus().equals(groupName) && payment.getAmount() == amount
                    && payment.getReason().equals(reason)) {
                payments.remove(payment);

                break;
            }
        }

        moneyOwedToMe.put(username, payments);
    }

    public void payGroup(String username, String groupName, double amount, String reason) {
        HashSet<Payment> payments = moneyOwedByMe.get(username);

        for (Payment payment : payments) {
            if (payment.getStatus().equals(groupName) && payment.getAmount() == amount
                    && payment.getReason().equals(reason)) {
                payments.remove(payment);
                paymentHistory.add(payment);

                break;
            }
        }

        moneyOwedByMe.put(username, payments);
    }

    public void addPaymentOwedByMe(String creditor, Payment newPayment) {
        if (!moneyOwedByMe.containsKey(creditor)) {
            HashSet<Payment> payments = new HashSet<>();
            payments.add(newPayment);
            moneyOwedByMe.put(creditor, payments);

        } else {
            HashSet<Payment> payments = moneyOwedByMe.get(creditor);
            payments.add(newPayment);
            moneyOwedByMe.put(creditor, payments);

        }
    }

    public void addPaymentOwedToMe(String creditor, Payment newPayment) {
        if (!moneyOwedToMe.containsKey(creditor)) {
            HashSet<Payment> payments = new HashSet<>();
            payments.add(newPayment);
            moneyOwedToMe.put(creditor, payments);

        } else {
            HashSet<Payment> payments = moneyOwedToMe.get(creditor);
            payments.add(newPayment);
            moneyOwedToMe.put(creditor, payments);

        }
    }

    private double getFriendSumOwedToMe(String friend) {
        if (moneyOwedToMe.containsKey(friend)) {
            HashSet<Payment> payments = moneyOwedToMe.get(friend);

            double sum = 0.0;
            String status = "friend";
            for (Payment payment : payments) {
                if (payment.getStatus().equals(status)) {
                    sum += payment.getAmount();
                }
            }

            return sum;
        }

        return 0.0;
    }

    private double getFriendSumOwedByMe(String friend) {
        if (moneyOwedByMe.containsKey(friend)) {
            HashSet<Payment> payments = moneyOwedByMe.get(friend);

            double sum = 0.0;
            String status = "friend";
            for (Payment payment : payments) {
                if (!payment.getStatus().equals(status)) {
                    sum += payment.getAmount();
                }
            }

            return sum;
        }

        return 0.0;
    }

    private double getGroupSumOwedToMe(String friend) {
        if (moneyOwedToMe.containsKey(friend)) {
            HashSet<Payment> payments = moneyOwedToMe.get(friend);

            double sum = 0.0;
            String status = "friend";
            for (Payment payment : payments) {
                if (!payment.getStatus().equals(status)) {
                    sum += payment.getAmount();
                }
            }

            return sum;
        }

        return 0.0;
    }

    private double getGroupSumOwedByMe(String friend) {
        if (moneyOwedByMe.containsKey(friend)) {
            HashSet<Payment> payments = moneyOwedByMe.get(friend);

            double sum = 0.0;
            String status = "friend";
            for (Payment payment : payments) {
                if (!payment.getStatus().equals(status)) {
                    sum += payment.getAmount();
                }
            }

            return sum;
        }

        return 0.0;
    }

    public double getMyFriendObligationTo(String friend) {
        double sumIOwe = getFriendSumOwedByMe(friend);
        double sumFriendOwes = getFriendSumOwedToMe(friend);

        return sumIOwe - sumFriendOwes;
    }

    public double getMyGroupObligationTo(String friend) {
        double sumIOwe = getGroupSumOwedByMe(friend);
        double sumFriendOwes = getGroupSumOwedToMe(friend);

        return sumIOwe - sumFriendOwes;
    }

    public String getUsername() {
        return username;
    }

    public boolean hasPassword(String password) {
        return this.password.equals(password);
    }
}
