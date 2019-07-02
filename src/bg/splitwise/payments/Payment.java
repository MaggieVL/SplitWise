package bg.splitwise.payments;


//redefine equals and hashCode()

public class Payment {
    private double amount;
    private String reason;
    private String status;
    
    public Payment(double amount, String reason, String status) {
        this.amount = amount;
        this.reason = reason;
        this.status = status;

    }

    public String getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public double getAmount() {
        return amount;
    }
}
