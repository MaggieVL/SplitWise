package bg.splitwise;

public class Debt {
    private double amount;
    private String creditor; //no need to have it

    public Debt(double amount, String creditor) {
        this.amount = amount;
        this.creditor = creditor;
    }

    public void increaseDebt(double sum) {
        amount += sum;
    }

    public void decreaseDebt(double sum) {
        amount -= sum;
    }
    
    public void printDebtDetails() {
        
    }
}
