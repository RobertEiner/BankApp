/**
 * Description
 * This class represents a customer of the bank. The major funcitonality of this class is to
 * remove accounts from the user, and deposit and withdraw money.
 *
 * @author Robert Einer, robein-9
 */

package robein9;

// Imports
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Customer implements Serializable {

    // Instance variables
    private String firstName;
    private String lastName;
    private final String pNo;
    private List<Account> accounts;

    /**
     * Constructor
     */
    public Customer(String firstName, String lastName, String pNo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pNo = pNo;
        this.accounts = new ArrayList<>();
    }

    /**
     * Getters for first name, last name, personnummer, the amount of accounts the customer has
     * and the accounts of the customer.
     */
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }

    public String getPNo() {
        return this.pNo;
    }

    public List<String> getTransactions(int accountId) {
        for(Account account : this.accounts) {
            if(account.getAccountNumber() == accountId) {
                return account.getTransactions();
            }
        }
        return null;
    }

    // get accounts
    public List<Account> getAccounts() {
        return new ArrayList<>(this.accounts);
    }


    public int getNumOfAccounts() {
        return this.accounts.size();
    }


    /**
     * Setters for first and last name
     */
    public void setFirstName(String newFirstName) {
        this.firstName = newFirstName;
    }
    public void setLastName(String newLastName) {
        this.lastName = newLastName;
    }


    /**
     * This method adds a new account to the customer
     * @param newAccount the new account to add
     */
    public void addAccount(Account newAccount) {
        this.accounts.add(newAccount);
    }

    /**
     * This method will remove the account of the customer with the account number passed in as an argument.
     * @param accountId account number of the account to be removed
     * @return the deleted account information
     */
    public String removeAccount(int accountId) {
        String accountInfo = "";
        String interest;
        String balance = "";
        NumberFormat percentFormat = NumberFormat.getPercentInstance(new Locale("sv","SE"));
        percentFormat.setMaximumFractionDigits(1);
        List<Account> accountsToKeep = new ArrayList<>();

        for(int i = 0; i < accounts.size(); i++) {
            Account currAccount = accounts.get(i);
            if(currAccount.getAccountNumber() == accountId) {
                interest = NumberFormat.getCurrencyInstance(new Locale("sv","SE")).format(currAccount.calculateInterest());
                balance = NumberFormat.getCurrencyInstance(new Locale("sv","SE")).format(currAccount.getBalance());
                accountInfo = accountId + " " + balance + " " + currAccount.getAccountType() + " " + interest;
            } else {
                accountsToKeep.add(currAccount);
            }

        }
        this.accounts = accountsToKeep;
        return accountInfo;
    }

    /**
     * This method deposits money in the account that matches the account number sent in as a parameter.
     * @param accountId account number of the account
     * @param amount the amount of money to be deposited
     * @return true if deposit was successful, false otherwise.
     */
    public boolean depositMoney(int accountId, int amount) {
        for(Account account : accounts) {
            if(account.getAccountNumber() == accountId) {
                account.depositMoney(new BigDecimal(amount));
                return true;
            }
        }
        return false;
    }

    /**
     * This method withdraws money from the account that matches the account number sent in as a parameter.
     * @param accountId account number of the account
     * @param amount the amount of money to be withdrawn
     * @return true if withdrawal was successful, false otherwise.
     */
    public boolean withdrawMoney(int accountId, int amount) {
        for(Account account : accounts) {
            if(account.getAccountNumber() == accountId) {
                if(account.withdrawMoney(new BigDecimal(amount))) {
                    return true;
                }
            }
        }
        return false;
    }


    public String toString() {
        return this.firstName + " " + this.lastName + " " + this.pNo;
    }

}
