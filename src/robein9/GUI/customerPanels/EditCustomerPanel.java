/**
 * Description
 * This is the panel where the user can handle a specific customer. The user can delete the customer,
 * change the name and create and display accounts
 *
 * @author Robert Einer, robein-9
 */


package robein9.GUI.customerPanels;
import robein9.BankLogic;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

public class EditCustomerPanel extends JPanel implements ActionListener {

    private BankLogic bank;
    private JPanel parentPanel, buttonPanel, upperComponentsPanel;
    private CardLayout cardLayout;
    private String pNo, firstName, lastName;
    private JLabel panelLabel, resultLabel, tableLabel;
    private  JButton changeNameBtn, deleteCustomerBtn, getAccountsBtn, createAccountBtn;
    private JScrollPane scrollPane;
    private String[] columnNames = {"Account number", "Balance", "Account type", "Interest rate"};
    private GetAllCustomersPanel getAllCustomersPanel;
    private EditAccountPanel editAccountPanel;
    private String[][] data;
    private JTable table;
    private DefaultTableModel tableModel;
    private final int NUM_OF_COLUMNS = 4;

    public EditCustomerPanel(BankLogic bank, JPanel parentPanel, EditAccountPanel editAccountPanel ) {
        this.parentPanel = parentPanel;
        this.editAccountPanel = editAccountPanel;
        initiateInstanceVariables(bank);
        buildPanel();
    }

    public void initiateInstanceVariables(BankLogic bank) {
        this.bank = bank;

        buttonPanel = new JPanel(new FlowLayout());
        upperComponentsPanel = new JPanel( new FlowLayout());
        cardLayout = (CardLayout) parentPanel.getLayout();
        panelLabel = new JLabel("");
        resultLabel = new JLabel("");
        tableLabel = new JLabel("Click an account in the table for more information.");
        changeNameBtn = new JButton("Change name");
        getAccountsBtn = new JButton("Show accounts");
        deleteCustomerBtn = new JButton("Delete customer");
        createAccountBtn = new JButton("Create account");
        tableModel = new DefaultTableModel(data, columnNames) {
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
        //this.setPreferredSize(new Dimension(100, 200));
        // add components
        this.add(upperComponentsPanel);
        upperComponentsPanel.add(panelLabel);
        upperComponentsPanel.add(getAccountsBtn);
        this.add(tableLabel);
        this.add(scrollPane);
        this.add(resultLabel);
        this.add(buttonPanel);
        buttonPanel.add(createAccountBtn);
        buttonPanel.add(changeNameBtn);
        buttonPanel.add(deleteCustomerBtn);
        // add action listeners to buttons
        getAccountsBtn.addActionListener(this);
        changeNameBtn.addActionListener(this);
        deleteCustomerBtn.addActionListener(this);
        createAccountBtn.addActionListener(this);
        //set size and margins
        scrollPane.setPreferredSize(new Dimension(30, 150));
        //table.setPreferredScrollableViewportSize(new Dimension(30, 300));
        // setting labels
        panelLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        Font font = panelLabel.getFont();
        panelLabel.setFont(new Font(font.getFontName(), font.getStyle(), 17));
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        tableLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        // add listener to table rows
        table.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.rowAtPoint(e.getPoint());
                if (selectedRow != -1) {
                    // Row is clicked, retrieve data
                    String accNumber = (String) table.getValueAt(selectedRow, 0);
                    openEditAccountPanel(accNumber, pNo);
                }
            }
        });
    }

    // label to display which customer is currently being shown
    public void setInformation(String pNo, String firstName, String lastName) {
        this.pNo = pNo;
        this.firstName = firstName;
        this.lastName = lastName;
        panelLabel.setText("Customer: " + firstName + " " + lastName + " " + pNo);
    }

    // Update the name of a customer
   private void updateName(String newFirstName, String newLastName) {
       panelLabel.setText("Customer: " + newFirstName + " " + newLastName + " " + pNo);
    }

    // action handler
    @Override
    public void actionPerformed(ActionEvent e) {
        resultLabel.setText("");
        if(e.getSource() == getAccountsBtn) {
            if(bank.findCustomer(pNo).getNumOfAccounts() > 0) {
                populateTable();
                updateTableModel();
            } else {
                resultLabel.setText(firstName + " " + lastName + " has no accounts.");
            }
        }
        if(e.getSource() == changeNameBtn) {
            changeCustomerName();
        }
        if(e.getSource() == deleteCustomerBtn) {
            deleteCustomer();
        }
        if(e.getSource() == createAccountBtn) {
            createAccount();
        }
    }

    // clear the JTable
    public void clearTable() {
        resultLabel.setText("");
        while(tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
    }

    // clear the table and then add all rows, necessary to not have duplicate rows in table
    private void updateTableModel() {
        // First we clear the table
        while(tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        // Add rows from the data array to the table model
        for (String[] rowData : data) {
            tableModel.addRow(rowData);
        }
        // remove name and personal number from the accounts-table
        tableModel.removeRow(0);
    }

    // add all customer account info to the data array
    public void populateTable() {
        List<String> customerAccounts = bank.getCustomer(pNo);
        this.data = new String[customerAccounts.size()][NUM_OF_COLUMNS];

        for (int i = 0; i < customerAccounts.size(); i++) {
            // get substrings of first name, last name and pno
            String[] subStrings = customerAccounts.get(i).split(" ");
            // add substrings to data
            System.arraycopy(subStrings, 0, data[i], 0, subStrings.length);
        }

    }

    // create a new account. An optionPane will open for this purpose
    private void createAccount() {
        JPanel createAccountPanel = new JPanel();
        createAccountPanel.setLayout(new BoxLayout(createAccountPanel, BoxLayout.Y_AXIS));
        createAccountPanel.setPreferredSize(new Dimension(200, 100));

        JButton createSavingsAccountBtn = new JButton("Create savings account");
        JButton createCreditAccountBtn = new JButton("Create credit account");
        JButton closeOptionPaneBtn = new JButton("Close");

        JLabel titleLabel = new JLabel("Choose which type of account to create:");
        JLabel paneResultLabel = new JLabel("");

        createAccountPanel.add(titleLabel);
        createAccountPanel.add(createSavingsAccountBtn);
        createAccountPanel.add(createCreditAccountBtn);
        createAccountPanel.add(paneResultLabel);
        // paneResultLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // action listener to close pane
        closeOptionPaneBtn.addActionListener(e -> JOptionPane.getRootFrame().dispose());

        // Actionlistener to create credit account
        createCreditAccountBtn.addActionListener(e -> {
            int accountNum =  createCustomerAccount("credit");
            paneResultLabel.setText("Credit account created. Account number: " + accountNum);

        });
        // Actionlistener to create savings account
         createSavingsAccountBtn.addActionListener(e -> {
             int accountNum = createCustomerAccount("savings");
             paneResultLabel.setText("Savings account created. Account number: " + accountNum);

         });


        JButton[] buttons = {createCreditAccountBtn, createSavingsAccountBtn, closeOptionPaneBtn};
        //newPanel.add(changeNameButton);
        JOptionPane.showOptionDialog(
                null,
                createAccountPanel,
                "Change Customer Name",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                buttons,
                null);
    }

    // Add account to the list of customer accounts
    private int createCustomerAccount(String type) {
        int accountNumber;
        if(type.equals("savings")) {
            accountNumber = bank.createSavingsAccount(this.pNo);
        } else {
            accountNumber = bank.createCreditAccount(this.pNo);
        }
       return accountNumber;
    }

    // Change the name of a customer. An optionsPane will open for this purpose
    private void changeCustomerName() {
        // create a new panel with components
        JPanel newPanel = new JPanel();
        newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));
        newPanel.setPreferredSize(new Dimension(200, 100));
        JTextField firstNameField = new JTextField(20);
        JTextField lastNameField = new JTextField(20);
        //set size of textfields
        firstNameField.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        lastNameField.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        // create border
        firstNameField.setBorder(BorderFactory.createTitledBorder("First name"));
        lastNameField.setBorder(BorderFactory.createTitledBorder("First name"));

        JLabel paneResultLabel = new JLabel("");
        newPanel.add(firstNameField);
        newPanel.add(lastNameField);
        newPanel.add(paneResultLabel);
        JButton paneChangeNameButton = new JButton("Change name");
        JButton closeOptionPaneBtn = new JButton("Close");
       // paneResultLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // action listener to close pane
        closeOptionPaneBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.getRootFrame().dispose();
            }
        });
        // Actionlistener to change name
        paneChangeNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newFirstName = firstNameField.getText();
                String newLastName = lastNameField.getText();

                if(!validateInput(newFirstName, newLastName)) {
                    paneResultLabel.setText("Both name fields can't be empty.");
                } else {
                    if(bank.changeCustomerName(newFirstName, newLastName, pNo)) {
                        updateName(newFirstName, newLastName);
                        paneResultLabel.setText("Name changed successfully.");
                        firstNameField.setText("");
                        lastNameField.setText("");

                    } else {
                        paneResultLabel.setText("Name couldn't be changed, try again..");
                    }
                }
            }
        });

        JButton[] buttons = {paneChangeNameButton, closeOptionPaneBtn};
        //newPanel.add(changeNameButton);
        JOptionPane.showOptionDialog(
                null,
                newPanel,
                "Change Customer Name",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                buttons,
                null);
    }

    // Delete a customer. An optionsPane will open for this purpose
    private void deleteCustomer() {
        JPanel newPanel = new JPanel();
        newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));
        newPanel.setPreferredSize(new Dimension(200, 100));

        JLabel paneResultLabel = new JLabel("");
        newPanel.add(new JLabel("Are you sure you wish to delete " + firstName + " " + lastName + "?"));
        JButton paneDeleteCustomerBtn = new JButton("Yes");
        JButton noBtn = new JButton("No");

        newPanel.add(paneResultLabel);
        // paneResultLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));


        noBtn.addActionListener(e -> JOptionPane.getRootFrame().dispose());

        paneDeleteCustomerBtn.addActionListener(e -> {
            JOptionPane.getRootFrame().dispose();
            displayDeletedAccounts(firstName, lastName);
            paneResultLabel.setText("customer deleted");

        });
        JButton[] buttons = {paneDeleteCustomerBtn, noBtn};
        //newPanel.add(changeNameButton);
        JOptionPane.showOptionDialog(
                null,
                newPanel,
                "Delete Customer",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                buttons,
                null);
    }

    // Display the deleted accounts of the deleted customer in a table
    private void displayDeletedAccounts(String firstName, String lastName) {
        JPanel deletedAccountsPanel = new JPanel();
        List<String> deletedAccounts =  bank.deleteCustomer(pNo);
        String[][] deletedAccountsData = new String[deletedAccounts.size()][NUM_OF_COLUMNS];
        String[] deletedAccountsColumnNames = {"Account number", "Balance", "Account type", "Interest"     };

        DefaultTableModel deleteAccTableModel = new DefaultTableModel(deletedAccountsData, deletedAccountsColumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells are non-editable
                return false;
            }
        };
        // components in option pane
        JTable deletedAccountsTable = new JTable(deleteAccTableModel);
        JScrollPane scrollPane = new JScrollPane(deletedAccountsTable);
        deletedAccountsPanel.setLayout(new BoxLayout(deletedAccountsPanel, BoxLayout.Y_AXIS));
        deletedAccountsPanel.setPreferredSize(new Dimension(400, 200));
        deletedAccountsPanel.add(new JLabel("Deleted Accounts for "  + firstName + " " + lastName));
        deletedAccountsPanel.add(scrollPane);
        JLabel paneResultLabel = new JLabel("");
        JButton okBtn = new JButton("Ok");
        deletedAccountsPanel.add(paneResultLabel);

        // populate data
        populateData(deletedAccounts, deletedAccountsData, deleteAccTableModel);
        // populate table with data
        for (String[] rowData : deletedAccountsData) {
            deleteAccTableModel.addRow(rowData);
        }
        //  remove first row, don't need to disply name in the list
        deleteAccTableModel.removeRow(0);

        okBtn.addActionListener(e -> {
            JOptionPane.getRootFrame().dispose();
            goBackToCustomersPanel();
        });
        JButton[] buttons = {okBtn};
        //newPanel.add(changeNameButton);
        JOptionPane.showOptionDialog(
                null,
                deletedAccountsPanel,
                "Delete Customer",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                buttons,
                null);
    }

    // validate user input
    private boolean validateInput(String firstName, String lastName) {
        return !firstName.equals("") || !lastName.equals("");
    }

    // Add deleted accounts to deletedAccountsData array, to display for the user
    private void populateData(List<String> deletedAccounts, String[][] deletedAccountsData, DefaultTableModel deletedAccTableModel) {
        while(deletedAccTableModel.getRowCount() > 0) {
            deletedAccTableModel.removeRow(0);
        }
        for (int i = 0; i < deletedAccounts.size(); i++) {
            // get substrings of first name, last name and pno
            String[] subStrings = deletedAccounts.get(i).split(" ");
            // add substrings to data
            System.arraycopy(subStrings, 0, deletedAccountsData[i], 0, subStrings.length);
        }
    }

    // Navigate to getAllCustomersPanel
    private void goBackToCustomersPanel() {
        getAllCustomersPanel.clearCustomers();
        cardLayout.show(parentPanel, "GetAllCustomersPanel");
    }
    // Navigate to EditAccountPanel
    private void openEditAccountPanel(String accountNumber, String pNo) {
        editAccountPanel.setInformation(accountNumber, pNo);
        editAccountPanel.clearTable();
        cardLayout.show(parentPanel, "EditAccountPanel");
    }
    // Initialize getAllCustomersPanel, has to be done after the EditCustomerPanel is initialized in MainFrame due
    // to dependencies between the two instances
    public void setGetAllCustomersPanel(GetAllCustomersPanel getAllCustomersPanel) {
        this.getAllCustomersPanel = getAllCustomersPanel;
    }

    public void setEditAccountPanel(EditAccountPanel editAccountPanel) {
        this.editAccountPanel = editAccountPanel;
    }


}
