/**
 * Description
 * This class represents an account that a customer owns. The major functionality includes
 * methods for depositing and withdrawing money from the account and for calculating interest.
 *
 * @author Robert Einer, robein-9
 */

package robein9;

// Imports
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class Account implements Serializable {

    // Instance variables
    private BigDecimal balance;
    private final int accountNumber;
    private final String accountType;
    private final List<String> transactions = new LinkedList<>();

    // Class variable
    private static int lastAssignedAccountNumber = 1000;

    /**
     * Constructor for the account class
     */
    public Account(String accountType) {
        this.balance = new BigDecimal("0");
        this.accountType = accountType;
        lastAssignedAccountNumber++;
        this.accountNumber = lastAssignedAccountNumber;
    }

    /**
     * Getters for balance, interest rate, account number and account type.
     */

    public BigDecimal getBalance() {
        return this.balance;
    }
    public abstract BigDecimal getInterestRate();
    public int getAccountNumber() {
        return this.accountNumber;
    }
    public String getAccountType() {
        return this.accountType;
    }
    public List<String> getTransactions() {
        return this.transactions;
    }
    public int getLastAssignedAccountNumber() {
        return lastAssignedAccountNumber;
    }
    
    /**
     * Setters
     */
    // setBalance uses protected access modifier to only be accessible from subclasses
    protected void setBalance(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }
    public void setLastAssignedAccountNumber(int newAccNr) {
        lastAssignedAccountNumber = newAccNr;
    }

    /**
     * Method for calculating the interest
     * @return the interest as Big Decimal
     */
    public abstract BigDecimal calculateInterest();

    /**
     * This method will deposit money in the account
     * @param amount is the amount to add to the account
     * @return balance which is the updated balance
     */
    public BigDecimal depositMoney(BigDecimal amount) {
        this.balance = this.balance.add(amount);
        this.addTransaction(amount);
        return this.balance;
    }

    /**
     * This method will withdraw money from the account
     * @param amount is the amount to withdraw
     * @return true if withdrawal was successful, false otherwise.
     */
    public abstract boolean withdrawMoney(BigDecimal amount);


    /**
     * Saves each successful transaction in a list
     * @param amount the amount that was deposited/withdrawn in the transaction
     */
    public void addTransaction(BigDecimal amount) {
        NumberFormat percentFormat = NumberFormat.getPercentInstance(new Locale("sv","SE"));
        percentFormat.setMaximumFractionDigits(1);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());
        String newTransaction = strDate + " " + NumberFormat.getCurrencyInstance(new Locale("sv","SE")).format(amount) + " Saldo: " + NumberFormat.getCurrencyInstance(new Locale("sv","SE")).format(this.balance);
        transactions.add(newTransaction);
    }

}
