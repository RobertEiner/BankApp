/**
 * Description
 * This class interacts with the customer and the account class to perform functionality
 * that is common for a bank application. This includes retrieving information about the
 * customer and their accounts, editing that information, adding and removing customers
 * as well as depositing and withdrawing money.
 *
 * @author Robert Einer, robein-9
 */

package robein9;

// Imports
import java.io.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

public class BankLogic {

    // Instance variables
    private final List<Customer> customers;
    final private int FIRST_INDEX = 0;
    final private String SAV_ACC_TYPE = "Sparkonto";
    final private String CRED_ACC_TYPE = "Kreditkonto";

    /**
     * Constructor
     */
    public BankLogic() {
        this.customers = new LinkedList<>();
    }

    /**
     * This method will get all the customers in the bank.
     * @return a list with personnummer, first name and last name of all customers
     */
    public List<String> getAllCustomers(){
        List<String> presOfCustomers = new ArrayList<>();
        for(Customer currCustomer : customers) {
            presOfCustomers.add(currCustomer.getPNo() + " " + currCustomer.getFirstName() + " " + currCustomer.getLastName());
        }
        return presOfCustomers;
    }

    /**
     * This method will create a new customer and add it to the bank.
     * @param name first name of customer
     * @param surname last name of customer
     * @param pNo personnummer of customer
     * @return true if customer was successfully created, false otherwise.
     */
    public boolean createCustomer(String name, String surname, String pNo){
        if(this.containsCustomer(pNo)) {
            return false;
        }
        Customer newCustomer = new Customer(name, surname, pNo);
        customers.add(newCustomer);
        return true;
    }

    /**
     * This method will get the information about a specific customer
     * @param pNo personnummer of the customer to get info about
     * @return a list with the information about the customer
     */
    public List<String> getCustomer(String pNo){
        if(!this.containsCustomer(pNo)) {
            return null;
        }
        List<String> customerInfo = new ArrayList<>();
        Customer tempCustomer = this.findCustomer(pNo);
        String nameAndPNo = tempCustomer.getPNo() + " " + tempCustomer.getFirstName() + " " + tempCustomer.getLastName();
        List<Account> accounts = tempCustomer.getAccounts();

        NumberFormat percentFormat = NumberFormat.getPercentInstance(new Locale("sv","SE"));
        percentFormat.setMaximumFractionDigits(1);

        // simply add name and personnummer to the first index of the list
        customerInfo.add(0, nameAndPNo);
        for (int i = 0; i < tempCustomer.getNumOfAccounts(); i++) {
            Account tempAccount = accounts.get(i);
            customerInfo.add(this.getAccount(tempCustomer.getPNo(), tempAccount.getAccountNumber()));
        }
        return customerInfo;
    }

    /**
     * This method will change the name of the customer
     * @param name first name to change
     * @param surname last name to change
     * @param pNo personnummer of the customer to change the name
     * @return true if name was changed successfully, false otherwise.
     */
    public boolean changeCustomerName(String name, String surname, String pNo) {
        if(!containsCustomer(pNo) || (Objects.equals(name, "") && Objects.equals(surname, ""))) {
            return false;
        }
        Customer tempCustomer = findCustomer(pNo);
        if(!name.equals("") && surname.equals("")) {
            tempCustomer.setFirstName(name);
        } else if(name.equals("") && !surname.equals("")) {
            tempCustomer.setLastName(surname);
        } else {
            tempCustomer.setFirstName(name);
            tempCustomer.setLastName(surname);
        }
        return true;
    }

    /**
     * This method will delete a customer from the bank.
     * @param pNo personnummer of the customer to be deleted
     * @return a list with information about the customer and their accounts
     */
    public List<String> deleteCustomer(String pNo) {
        if(!containsCustomer(pNo)) {
            return null;
        }
        List<String> deletedCustomer = new ArrayList<>();
        Customer tempCustomer = findCustomer(pNo);
        int numOfAccounts = tempCustomer.getNumOfAccounts();

        String nameAndPNo = tempCustomer.getPNo() + " " + tempCustomer.getFirstName() + " " + tempCustomer.getLastName();
        deletedCustomer.add(0, nameAndPNo);
        for(int i = 0; i < numOfAccounts; i++) {
            deletedCustomer.add(this.closeAccount(tempCustomer.getPNo(), findAccount(tempCustomer)));
        }
        customers.remove(tempCustomer);
        return deletedCustomer;
    }

    /**
     * This method will create a new savings account for a customer
     * @param pNo personnummer of the customer where the account should be created
     * @return account number of the newly created account
     */
    public int createSavingsAccount(String pNo) {
        int accountNumber;
        if(!containsCustomer(pNo)) {
            return -1;
        }
        Customer tempCustomer = findCustomer(pNo);
        Account newSavingsAccount = new SavingsAccount(SAV_ACC_TYPE);
        tempCustomer.addAccount(newSavingsAccount);
        accountNumber = newSavingsAccount.getAccountNumber();
        return accountNumber;
    }

    /**
     * This method will create a new credit account for a customer
     * @param pNo personnummer of the customer where the account should be created
     * @return account number of the newly created account
     */
    public int createCreditAccount(String pNo) {
        if(!containsCustomer(pNo)) {
            return -1;
        }
        Customer tempCustomer = findCustomer(pNo);
        Account newCreditAccount = new CreditAccount(CRED_ACC_TYPE);
        tempCustomer.addAccount(newCreditAccount);
        return newCreditAccount.getAccountNumber();
    }


    /**
     * The method will get a specific account for a specific customer.
     * The account information is formatted.
     * @param pNo personnummer of the customer
     * @param accountId account number of the account
     * @return the information about the account
     */
    public String getAccount(String pNo, int accountId) {
        if(!containsCustomer(pNo) || !customerOwnsAccount(pNo, accountId)) {
            return null;
        }
        String accountInfo = "";
        BigDecimal interest;
        String balance;
        String accountType;
        NumberFormat percentFormat = NumberFormat.getPercentInstance(new Locale("sv","SE"));
        percentFormat.setMaximumFractionDigits(1);

        // find customer and get all of their accounts
        Customer tempCustomer = findCustomer(pNo);
        List<Account> accounts = tempCustomer.getAccounts();

        for(Account account : accounts) {
            if(account.getAccountNumber() == accountId) {
                interest = account.getInterestRate();
                String percentStr = percentFormat.format(interest.divide(new BigDecimal("100")));
                balance = NumberFormat.getCurrencyInstance(new Locale("sv","SE")).format(account.getBalance());
                accountType = account.getAccountType();
                accountId = account.getAccountNumber();
                accountInfo = accountId + " " + balance + " " + accountType + " " + percentStr;
            }
        }
        return accountInfo;
    }

    /**
     * This method will get all the transactions made in an account
     * @param pNo personal number of the customer that the account belongs to
     * @param accountId id of the account
     * @return a list with the transactions made
     */
    public ArrayList<String> getTransactions(String pNo, int accountId) {
        if(!containsCustomer(pNo)) {
            return null;
        }
        if(!customerOwnsAccount(pNo, accountId)) {
            return null;
        } else {
            Customer tempCustomer = findCustomer(pNo);
            return new ArrayList<>(tempCustomer.getTransactions(accountId));
        }
    }

    /**
     * This method will deposit money in a customer's bank account.
     * @param pNo personnummer of the customer
     * @param accountId account number of the account
     * @param amount the amount of money to deposit
     * @return true if money was deposited, false otherwise
     */
    public boolean deposit(String pNo, int accountId, int amount) {
        if(!containsCustomer(pNo)) {
            return false;
        }
        if(amount > 0) {
            Customer tempCustomer = findCustomer(pNo);
            return tempCustomer.depositMoney(accountId, amount);
        } else {
            return false;
        }
    }

    /**
     * This method will withdraw money from a customer's bank account.
     * @param pNo personnummer of the customer
     * @param accountId account number of the account
     * @param amount the amount of money to withdraw
     * @return true if money was withdrawn, false otherwise
     */
    public boolean withdraw(String pNo, int accountId, int amount) {
        if(!containsCustomer(pNo)) {
            return false;
        }
        Customer tempCustomer = findCustomer(pNo);
        return tempCustomer.withdrawMoney(accountId, amount);
    }

    /**
     * This method will close an account and delete it.
     * @param pNo personnummer of the customer
     * @param accountId account number of the customer
     * @return information about the deleted account
     */
    public String closeAccount(String pNo, int accountId) {
        if(!containsCustomer(pNo)) {
            return null;
        }
        Customer tempCustomer = findCustomer(pNo);
        return tempCustomer.removeAccount(accountId);
    }

    // Method to save all the customers in the bank to a file
    public boolean writeCustomersToFile() throws IOException {
        int lastAssignedAccNr = 1000;
        // if there are at least one customer in the bank, save to file
        if(customers.size() > 0) {
            FileOutputStream fileOutputStream = new FileOutputStream("robein9_files/bank.dat");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            // write the size of list to the file
            objectOutputStream.writeInt(customers.size());
            //  get the last assigned account number
            for(int i = customers.size() - 1; i >= 0; i--) {
                if(customers.get(i).getNumOfAccounts() > 0) {
                    int numOfAccounts = customers.get(i).getNumOfAccounts();
                    lastAssignedAccNr = customers.get(i).getAccounts().get(numOfAccounts - 1).getLastAssignedAccountNumber();
                    break;
                }
            }
            objectOutputStream.writeInt(lastAssignedAccNr);
            for(int i = 0; i < customers.size(); i++) {
                objectOutputStream.writeObject(customers.get(i));
            }
            objectOutputStream.close();
            return true;

        }
        return false;
    }

    // Method to load all the customers in the bank from a file
    public boolean loadCustomerFromFile() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("robein9_files/bank.dat");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        // get size of customer list
        Object readCustomersSize = objectInputStream.readInt();
        int numOfCustomers = (int) readCustomersSize;
        // if the file wasn't empty, get all customers from the file
        if(numOfCustomers > 0) {
            // clear potential customers before loading from file
            customers.clear();
            // get last account number
            Object readAccNr = objectInputStream.readInt();
            int lastAssignedAccNr = (int) readAccNr;
            for(int i = 0; i < numOfCustomers; i++) {
                Object readObject = objectInputStream.readObject();
                customers.add((Customer) readObject);
            }
            // set last assigned account nr
            for(Customer tempCustomer : customers) {
                if(tempCustomer.getAccounts().size() != 0) {
                    tempCustomer.getAccounts().get(0).setLastAssignedAccountNumber(lastAssignedAccNr);
                }
            }
            objectInputStream.close();
            return true;
        }
        objectInputStream.close();
        return false;

    }

    // Save all transactions of an account to a file
    public void saveTransactionsToFile(String pNo, int accountNumber) throws IOException {
        // Get account balance
        Customer customer = this.findCustomer(pNo);
        BigDecimal balance = new BigDecimal("0");
        for(Account account : customer.getAccounts()) {
            if(account.getAccountNumber() == accountNumber) {
                balance = account.getBalance();
            }
        }
        // Write to file
        PrintWriter pw = new PrintWriter(new FileWriter("robein9_files/transactions.txt"));
        ArrayList<String> transactions = this.getTransactions(pNo, accountNumber);
        pw.println("Transactions saved: " + java.time.LocalDate.now());
        pw.println(transactions);
        pw.println("Current balance: " + balance + "kr");
        pw.close();
    }
    
    /**
     * Helper methods
     */

    // Checks if the there is a customer with the specified personal number in the
    // list of customers
    private boolean containsCustomer(String pNo) {
        for(Customer currCustomer : customers) {
            if(currCustomer.getPNo().equals(pNo)) {
                return true;
            }
        }
        return false;
    }

    // Searches the customers list and returns the customer with the matching
    // personal number
   public Customer findCustomer(String pNo) {
        Customer customer = null;
        for(Customer currCustomer : customers) {
            if(currCustomer.getPNo().equals(pNo)) {
                customer = currCustomer;
            }
        }
        return customer;
    }

    // Fetches the accounts of the specified customer and returns the account
    // at the first index of the list. This method is called when a customer is deleted,
    // once for each account that the customer has. The first index is used because each call to
    // closeAccount() will shift each element in the accounts list one index to the left.
   private int findAccount(Customer customer) {
        List<Account> copyOfAccounts = customer.getAccounts();
        return copyOfAccounts.get(FIRST_INDEX).getAccountNumber();
   }


    // checks if the specified customer is the owner of the account with the
    // specified account number.
   private boolean customerOwnsAccount(String pNo, int accountId) {
        Customer tempCustomer = findCustomer(pNo);
        List<Account> copyOfAccounts = tempCustomer.getAccounts();
        for(Account account : copyOfAccounts) {
            if(account.getAccountNumber() == accountId) {
                return true;
            }
        }
        return false;
   }
}
