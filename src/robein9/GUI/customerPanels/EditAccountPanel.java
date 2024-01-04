/**
 * Description
 * This is the panel where the user can handle specific accounts of a customer. Deposits and withdrawals can be made
 * the user can view the transactions of the account, and account can be deleted.
 *
 * @author Robert Einer, robein-9
 */

package robein9.GUI.customerPanels;
import robein9.BankLogic;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class EditAccountPanel extends JPanel implements ActionListener {
    private BankLogic bank;

    private  JPanel parentPanel, upperComponentsPanel;
    private CardLayout cardLayout;
    private String accountNumber, pNo;
    private JLabel panelLabel, resultLabel;
    private JButton withdrawBtn, depositBtn, deleteAccountBtn, getTransactionsBtn, saveTransactionsBtn;
    private JScrollPane scrollPane;
    private String[] transactionColumnNames = {"Date", "Amount", "Balance"};
    private EditCustomerPanel editCustomerPanel;
    private String[][] data;
    private JTable table;
    private DefaultTableModel tableModel;
    private final int NUM_OF_COLUMNS = 4;
    public EditAccountPanel(BankLogic bank, JPanel parentPanel) {
        this.parentPanel = parentPanel;

        initiateInstanceVariables(bank);
        buildPanel();
    }

    public void initiateInstanceVariables(BankLogic bank) {
        this.bank = bank;
        cardLayout = (CardLayout) parentPanel.getLayout();
        upperComponentsPanel = new JPanel(new FlowLayout());
        panelLabel = new JLabel("");
        resultLabel = new JLabel("");
        depositBtn = new JButton("Deposit");
        withdrawBtn = new JButton("Withdraw");
        getTransactionsBtn = new JButton("Show transactions");
        deleteAccountBtn = new JButton("Delete account");
        saveTransactionsBtn = new JButton("Save transactions");
        tableModel = new DefaultTableModel(data, transactionColumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells are non-editable
                return false;
            }
        };
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
    }

    public void buildPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        FlowLayout flowLayout = new FlowLayout();
        JPanel buttonPanel = new JPanel(flowLayout);

        this.add(upperComponentsPanel);
        upperComponentsPanel.add(panelLabel);
        upperComponentsPanel.add(getTransactionsBtn);
        this.add(resultLabel);
        this.add(scrollPane);
        this.add(buttonPanel);
        buttonPanel.add(depositBtn);
        buttonPanel.add(withdrawBtn);
        buttonPanel.add(deleteAccountBtn);
        buttonPanel.add(saveTransactionsBtn);

        getTransactionsBtn.addActionListener(this);
        depositBtn.addActionListener(this);
        withdrawBtn.addActionListener(this);
        deleteAccountBtn.addActionListener(this);
        saveTransactionsBtn.addActionListener(this);
        scrollPane.setPreferredSize(new Dimension(300, 150));
        panelLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 15));
        Font font = panelLabel.getFont();
        panelLabel.setFont(new Font(font.getFontName(), font.getStyle(), 17));
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        table.setPreferredScrollableViewportSize(new Dimension(200, 150));
    }

    // Action handler
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == depositBtn) {
            depositMoney();
        }
        if(e.getSource() == withdrawBtn) {
            withdrawMoney();
        }
        if(e.getSource() == deleteAccountBtn) {
            deleteAccount();
        }
        if(e.getSource() == getTransactionsBtn) {
            getTransactions();
        }
        if(e.getSource() == saveTransactionsBtn) {
            saveTransactions();
        }
    }

    // This method will handle depositing money to an account. An optionPane will be created for this
    private void depositMoney() {
        JPanel depositPanel = new JPanel();
        depositPanel.setLayout(new BoxLayout(depositPanel, BoxLayout.Y_AXIS));
        depositPanel.setPreferredSize(new Dimension(200, 100));
        JTextField amount = new JTextField();
        amount.setMaximumSize(new Dimension(Short.MAX_VALUE, amount.getPreferredSize().height));

        JLabel depositResultLabel = new JLabel("");
        depositPanel.add(new JLabel("Enter amount to deposit"));
        depositPanel.add(amount);
        depositPanel.add(depositResultLabel);
        JButton paneDepositBtn = new JButton("Deposit");
        JButton closeOptionPaneBtn = new JButton("Close");
        // paneResultLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Action listener to close pane
        closeOptionPaneBtn.addActionListener(e -> JOptionPane.getRootFrame().dispose());
        // Action listener to deposit money
        paneDepositBtn.addActionListener(e -> {
            String amountToDeposit = amount.getText();

            if(!validateInput(amountToDeposit)) {
                depositResultLabel.setText("Please enter a valid amount");
            } else {
                if(bank.deposit(pNo, Integer.parseInt(accountNumber), Integer.parseInt(amountToDeposit))) {
                    depositResultLabel.setText("Money was successfully deposited.");
                    amount.setText("");
                    editCustomerPanel.clearTable();
                } else {
                    depositResultLabel.setText("Deposit didn't go through, try again.");
                }
            }
        });

        JButton[] buttons = {paneDepositBtn, closeOptionPaneBtn};
        JOptionPane.showOptionDialog(
                null,
                depositPanel,
                "Deposit money",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                buttons,
                null);
    }

    // This method will withdraw money from an account. An optionPane will be created for this
    private void withdrawMoney() {
        JPanel withdrawPanel = new JPanel();
        withdrawPanel.setLayout(new BoxLayout(withdrawPanel, BoxLayout.Y_AXIS));
        withdrawPanel.setPreferredSize(new Dimension(200, 100));
        JTextField amount = new JTextField();
        amount.setMaximumSize(new Dimension(Short.MAX_VALUE, amount.getPreferredSize().height));

        JLabel withdrawResultLabel = new JLabel("");
        withdrawPanel.add(new JLabel("Enter amount to withdraw"));
        withdrawPanel.add(amount);
        withdrawPanel.add(withdrawResultLabel);
        JButton paneWithdrawBtn = new JButton("Withdraw");
        JButton closeOptionPaneBtn = new JButton("Close");
        // paneResultLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // action listener to close pane
        closeOptionPaneBtn.addActionListener(e -> JOptionPane.getRootFrame().dispose());
        // Action listener to withdraw money
        paneWithdrawBtn.addActionListener(e -> {
            String amountToWithdraw = amount.getText();
            if(!validateInput(amountToWithdraw)) {
                withdrawResultLabel.setText("Please enter a valid amount");
            } else {
                if(bank.withdraw(pNo, Integer.parseInt(accountNumber), Integer.parseInt(amountToWithdraw))) {
                    withdrawResultLabel.setText("Money was successfully withdrawn.");
                    amount.setText("");
                    editCustomerPanel.clearTable();
                } else {
                    withdrawResultLabel.setText("Withdrawal didn't go through, try again.");
                }
            }
        });

        JButton[] buttons = {paneWithdrawBtn, closeOptionPaneBtn};
        JOptionPane.showOptionDialog(
                null,
                withdrawPanel,
                "Withdraw money",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                buttons,
                null);
    }

    // This method will delete an account from the customer. An optionPane will be created for this
    private void deleteAccount() {
        // create panel and components
        JPanel deleteAccountPanel = new JPanel();
        deleteAccountPanel.setLayout(new BoxLayout(deleteAccountPanel, BoxLayout.Y_AXIS));
        deleteAccountPanel.setPreferredSize(new Dimension(400, 100));

        JLabel paneResultLabel = new JLabel("");
        deleteAccountPanel.add(new JLabel("Are you sure you wish to delete account " + accountNumber  + "?"));
        JButton paneDeleteAccountBtn = new JButton("Delete");
        JButton noBtn = new JButton("No");

        deleteAccountPanel.add(paneResultLabel);
        noBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.getRootFrame().dispose();
            }
        });
        paneDeleteAccountBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String deletedAccount = bank.closeAccount(pNo, Integer.parseInt(accountNumber));

                if(deletedAccount == null) {
                    paneResultLabel.setText("Account doesn't exist");
                } else {
                    clearTable();
                    JOptionPane.getRootFrame().dispose();
                    showDeletedAccount(deletedAccount);



                }
            }
        });
        JButton[] buttons = {paneDeleteAccountBtn, noBtn};
        //newPanel.add(changeNameButton);
        JOptionPane.showOptionDialog(
                null,
                deleteAccountPanel,
                "Delete Customer",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                buttons,
                null);
    }

    // Get the transactions of a specific account
    private void getTransactions() {
        resultLabel.setText("");
        if(!populateTable())  {
            resultLabel.setText("This account has no transactions.");
        } else {
            updateTableModel();
        }
    }

    // Save transactions to a file, show a pane upon success/error
    private void saveTransactions() {
        try {
            bank.saveTransactionsToFile(pNo, Integer.parseInt(accountNumber));
            showPane("Transactions were saved successfully!", "Save Transactions");
        } catch (IOException e) {
            showPane("Transactions couldn't be saved, try again.", "Error saving transactions");
            e.printStackTrace();
        }
    }

    private void showPane(String message, String title) {
        JLabel titleLabel = new JLabel(message);
        JPanel noCustomersPane = new JPanel();
        noCustomersPane.setLayout(new BoxLayout(noCustomersPane, BoxLayout.Y_AXIS));
        noCustomersPane.setPreferredSize(new Dimension(300, 50));
        JButton okButton = new JButton("Ok");
        okButton.addActionListener(e -> JOptionPane.getRootFrame().dispose());
        noCustomersPane.add(titleLabel);
        JButton[] buttons = { okButton};
        JOptionPane.showOptionDialog(
                null,
                noCustomersPane,
                title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                buttons,
                null);

    }

    // Add transactions information to the table
    public boolean populateTable() {
        ArrayList<String> transactions = bank.getTransactions(pNo, Integer.parseInt(accountNumber));
        if(transactions.isEmpty()) {
            return false;
        }
        this.data = new String[transactions.size()][transactionColumnNames.length];

        for (int i = 0; i < transactions.size(); i++) {
            String transaction = transactions.get(i).replace(" Saldo:", "");
            String[] subStrings = transaction.split("\\s");
            String dateAndTime = subStrings[0] + " " + subStrings[1];
            subStrings[1] = dateAndTime;
            String[] modifiedTransactions = new String[transactionColumnNames.length];

            for (int j = 0; j < transactionColumnNames.length; j++) {
                modifiedTransactions[j] = subStrings[j + 1];
            }
            // add substrings to data
            System.arraycopy(modifiedTransactions, 0, data[i], 0, transactionColumnNames.length);
        }
        return true;

    }

    // Update the table to display the transactions of the account
    private void updateTableModel() {
        // First we clear the table
        while(tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        // Add rows from the data array to the table model
        for (String[] rowData : data) {
            tableModel.addRow(rowData);
        }

    }

    // Validate user input
    private boolean validateInput(String amount) {
        if(amount.equals("")) {
            return false;
        }
        for(int i = 0; i < amount.length(); i++) {
            if(!Character.isDigit(amount.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // Set information about account to beh able to display it on the screen
    public void setInformation(String accountNumber, String pNo) {
        this.accountNumber = accountNumber;
        this.pNo = pNo;
        panelLabel.setText("Account: " + accountNumber);
    }

    // Initialize editCustomerPanel, has to be done after the EditAccountPanel is initialized in MainFrame due
    // to dependencies between the two instances
    public void setEditCustomerPanel(EditCustomerPanel editCustomerPanel) {
        this.editCustomerPanel = editCustomerPanel;
    }

    // Clear the JTable
    public void clearTable() {
        while(tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
    }

    // Display the deleted account information. A new optionPane is created for this purpose
    private void showDeletedAccount(String deletedAccount) {
        JPanel showDeletedAccount = new JPanel();
        showDeletedAccount.setLayout(new BoxLayout(showDeletedAccount, BoxLayout.Y_AXIS));
        showDeletedAccount.setPreferredSize(new Dimension(400, 100));
        JButton okBtn = new JButton("Ok");
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.getRootFrame().dispose();
                editCustomerPanel.clearTable();
                cardLayout.show(parentPanel, "EditCustomerPanel");
            }
        });
        showDeletedAccount.add(new JLabel("Deleted account information:"));
        JLabel[] accountInfo = { new JLabel("Account number: "), new JLabel("Balance: "), new JLabel("Account type: "), new JLabel("Interest: ")};
        showDeletedAccount.add(okBtn);
        JButton[] buttons = { okBtn };
        String[] substrings = deletedAccount.split(" ");
        for(int i = 0; i < 4; i++) {
          accountInfo[i].setText(accountInfo[i].getText() + " " + substrings[i]);
          showDeletedAccount.add(accountInfo[i]);
        }

        JOptionPane.showOptionDialog(
                null,
                showDeletedAccount,
                "Delete account",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                buttons,
                null);


    }


}
