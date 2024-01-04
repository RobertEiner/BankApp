/**
 * Description
 * This panel is the starting point for handling customers, with the option to display all customers
 * in the bank and to navigate to them.
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

public class GetAllCustomersPanel extends JPanel implements ActionListener {
    private JPanel inputpanel;
    private BankLogic bank;
    private JScrollPane scrollPane;
    private EditCustomerPanel editCustomerPanel;
    private JButton getCustomers;
    private JLabel titleLabel, resultLabel, tableLabel;
    private  String[] columnNames = {"Personal number",
            "Last Name",
            "First Name",
            };
    private String[][] data;
    private JTable table;
    private DefaultTableModel tableModel;
    private CardLayout cardLayout;
    private final int NUM_OF_COLUMNS = 3;

    public GetAllCustomersPanel(BankLogic bank, JPanel inputPanel, EditCustomerPanel editCustomerPanel) {
        this.inputpanel = inputPanel;
        this.editCustomerPanel = editCustomerPanel;
        initiateInstanceVariables(bank);
        buildPanel();
    }

    public void initiateInstanceVariables(BankLogic bank) {
        this.bank = bank;
         cardLayout = (CardLayout) inputpanel.getLayout();

        getCustomers = new JButton("Show customers");
        titleLabel = new JLabel("Show all customers in the bank");
        resultLabel = new JLabel("");
        tableLabel = new JLabel("Click a customer in the table below for more options and information about that customer.");
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

    // Build the panel
    public void buildPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(titleLabel);
        this.add(getCustomers);
        this.add(tableLabel);
        //this.add(customerTextArea);
        getCustomers.addActionListener(this);
        getCustomers.setPreferredSize(new Dimension(50, 30));
        scrollPane.setPreferredSize(new Dimension(100, 10));
        //table.setPreferredSize(new Dimension(100, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        tableLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        this.add(scrollPane);
        this.add(resultLabel);

        table.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.rowAtPoint(e.getPoint());
                if (selectedRow != -1) {
                    // Row is clicked, retrieve data and perform action
                    String pNo = (String) table.getValueAt(selectedRow, 0);
                    String name = (String) table.getValueAt(selectedRow, 1);
                    String lastName = (String) table.getValueAt(selectedRow, 2);

                    openNewPanel(pNo, name, lastName);

                }
            }
        });
    }

    // Action handler
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == getCustomers) {
           List<String> customers = bank.getAllCustomers();
            System.out.println("Customers " + customers);
           if(customers.isEmpty()) {
               resultLabel.setText("Currently there are no customers in the bank");
           } else {
               this.data = new String[customers.size()][NUM_OF_COLUMNS];
               printCustomers(customers);
           }
        }
    }

    // Print the customers to the JTable
    public void printCustomers(List<String> customers) {
        populateData(customers);
        updateTableModel();
    }

    // Add customer information to the data array
    private void populateData(List<String> customers) {
        for (int i = 0; i < customers.size(); i++) {
            // get substrings of first name, last name and pno for each customer
            String[] subStrings = customers.get(i).split(" ");
            // add substrings to data
            System.arraycopy(subStrings, 0, data[i], 0, subStrings.length);
        }
    }

    // Clear the table
    public void clearCustomers() {
        while(tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        this.resultLabel.setText("");
    }

    // Update the table with the information from customers
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

    // Open the EditCustomerPanel to edit a specific customer
    private void openNewPanel(String pNo, String firstName, String lastName) {
        editCustomerPanel.clearTable();
        editCustomerPanel.setInformation(pNo, firstName, lastName);
        cardLayout.show(inputpanel, "EditCustomerPanel");

    }


}
