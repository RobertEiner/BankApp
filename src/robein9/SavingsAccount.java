/**
 * Description
 * This class is a subclass of the Account class. It represents a credit account in the bank
 * with a credit limit of 5000kr.
 *
 * @author Robert Einer, robein-9
 */

package robein9;

import java.math.BigDecimal;

public class SavingsAccount extends Account {

    // Instance variables
    private boolean firstWithdrawalMade = false;

    // Constants
    private final BigDecimal ZERO = new BigDecimal("0");
    private final BigDecimal INTEREST_RATE = new BigDecimal("1.2");
    private final BigDecimal PERCENTAGE = new BigDecimal("100");
    private final BigDecimal WITHDRAW_INTEREST = new BigDecimal("0.02");


    /**
     * Constructor for savings account
     */
    public SavingsAccount(String accountType) {
        super(accountType);
    }

    /**
     * Calculates the interest of the savings account
     * @return the interest
     */
    @Override
    public BigDecimal calculateInterest() {
        return (super.getBalance().multiply(INTEREST_RATE)).divide(PERCENTAGE);
    }

    /**
     * Get method for interest rate
     * @return the interest rate
     */
    @Override
    public BigDecimal getInterestRate() {
        return INTEREST_RATE;
    }

    /**
     * Withdraws the specified amount form the account. If it's not the first withdrawal made
     * an interest of 2% will be added to the amount.
     * @param amount is the amount to withdraw
     * @return true if withdrawal was successful, false otherwise
     */
    @Override
    public boolean withdrawMoney(BigDecimal amount) {
        BigDecimal balanceWithInterest = amount.add(amount.multiply(WITHDRAW_INTEREST));
        if(amount.compareTo(ZERO) > 0 && amount.compareTo(super.getBalance()) <= 0) {
            if(!firstWithdrawalMade) {
                super.setBalance(amount);
                super.addTransaction(amount.negate());
                firstWithdrawalMade = true;
            } else {
                if(balanceWithInterest.compareTo(super.getBalance()) <= 0) {
                    super.setBalance(balanceWithInterest);
                    super.addTransaction(balanceWithInterest.negate());
                } else {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }



}
