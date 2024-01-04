/**
 * Description
 * This class is a subclass of the Account class. It represents a savings account in the bank
 * where the user can deposit and save their money. The customer is allowed one free withdrawal
 * each year.
 *
 * @author Robert Einer, robein-9
 */


package robein9;

import java.math.BigDecimal;

public class CreditAccount extends Account {

    // Instance variables
    private BigDecimal interestRate = new BigDecimal("0.5");

    //Constants
    private final BigDecimal ZERO = new BigDecimal("0");
    private final BigDecimal CREDIT_LIMIT = new BigDecimal("5000");
    private final BigDecimal DEBT_INTEREST_RATE = new BigDecimal("7");
    private final BigDecimal INTEREST_RATE = new BigDecimal("0.5");
    private final BigDecimal PERCENTAGE = new BigDecimal("100");

    /**
     * Constructor for credit account
     */
    public CreditAccount(String accountType) {
        super(accountType);
    }

    /**
     * Calculate the interest of the credit account
     * @return the interest
     */
    @Override
    public BigDecimal calculateInterest() {
        this.getInterestRate();
        return (super.getBalance().multiply(interestRate)).divide(PERCENTAGE);
    }

    /**
     * Gets the interest rate of the credit account, if balance is negative, the
     * debt interest rate is set and returned. If balance is positive, interest rate is set
     * to normal rate.
     * @return the interest rate
     */
    @Override
    public BigDecimal getInterestRate() {
        if(super.getBalance().compareTo(ZERO) < 0) {
            setInterestRate(DEBT_INTEREST_RATE);
        } else {
            setInterestRate(INTEREST_RATE);
        }
        return interestRate;
    }

    /**
     * Withdraws money form the credit account
     * @param amount is the amount to withdraw
     * @return
     */
    @Override
    public boolean withdrawMoney(BigDecimal amount) {
       if(amount.compareTo(ZERO) > 0) {
           if(!(super.getBalance().subtract(amount).compareTo(CREDIT_LIMIT.negate()) < 0) )  {
               super.setBalance(amount);
               super.addTransaction(amount.negate());
               return true;
           } else {
               return false;
           }
        } else {
            return false;
        }

    }

    /**
     * Private method to set the value of the interest rate
     * to either 0.5% or 7%
     * @param interestRate the rate to set it to
     */
    private void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }




}
