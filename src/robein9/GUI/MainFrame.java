/**
 * Description
 * This is the main frame of the GUI. This JFrame has a card layout that holds the panels
 * in the customerPanels package, where all the bank functionality is located.
 *
 * @author Robert Einer, robein-9
 */


package robein9.GUI;

import robein9.BankLogic;
import robein9.GUI.customerPanels.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {

    private BankLogic bank = new BankLogic();
    private CardLayout cardLayout = new CardLayout();

    //menu related
    private JMenuBar menuBar;
    private JMenu customerMenu,  systemMenu;
    private JMenuItem homeMenuItem, createCustomerMenuItem, showCustomersMenuItem, loadBankMenuItem, saveBankMenuItem, exitTab;

    private GetAllCustomersPanel getAllCustomersPanel;
    private CreateCustomerPanel createCustomerPanel;
    private EditCustomerPanel editCustomerPanel;
    private EditAccountPanel editAccountPanel;
    private  HomePanel homePanel;
    private JPanel mainCardPanel;

    public MainFrame() {
        initiateInstanceVariables();
        buildFrame();
    }

    public void initiateInstanceVariables() {
        // main card layout
        mainCardPanel = new JPanel(cardLayout);

        // panels
        homePanel = new HomePanel();
        editAccountPanel = new EditAccountPanel(bank, mainCardPanel);
        editCustomerPanel = new EditCustomerPanel(bank, mainCardPanel, editAccountPanel);
        createCustomerPanel = new CreateCustomerPanel(bank);
        getAllCustomersPanel = new GetAllCustomersPanel(bank, mainCardPanel, editCustomerPanel);
        editAccountPanel.setEditCustomerPanel(editCustomerPanel);
        editCustomerPanel.setGetAllCustomersPanel(getAllCustomersPanel);

        // menu bar
        menuBar = new JMenuBar();

        // Menu tabs
        customerMenu = new JMenu("Customer");
        systemMenu = new JMenu("System");

        // menu items
        homeMenuItem = new JMenuItem("Home");
        createCustomerMenuItem = new JMenuItem("Create new customer");
        showCustomersMenuItem = new JMenuItem("Handle customers");
        exitTab = new JMenuItem("Exit");
        saveBankMenuItem = new JMenuItem("Save bank");
        loadBankMenuItem = new JMenuItem("Load bank");
    }

    // Build the frame
    public void buildFrame() {
        // setup of main frame
        this.setSize(700, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.setContentPane(mainCardPanel);
        this.setJMenuBar(menuBar);
        this.setTitle("Bank");

        setLocationRelativeTo(null);
        // add sub panels to main panel
        mainCardPanel.add(homePanel, "HomePanel");
        mainCardPanel.add(createCustomerPanel, "CreateCustomerPanel");
        mainCardPanel.add(getAllCustomersPanel, "GetAllCustomersPanel");
        mainCardPanel.add(editCustomerPanel, "EditCustomerPanel");
        mainCardPanel.add(editAccountPanel, "EditAccountPanel");

        // add tabs to menu
        customerMenu.add(createCustomerMenuItem);
        customerMenu.add(showCustomersMenuItem);

        systemMenu.add(homeMenuItem);
        systemMenu.add(loadBankMenuItem);
        systemMenu.add(saveBankMenuItem);
        systemMenu.add(exitTab);

        // add listener to menu tabs
        homeMenuItem.addActionListener(this);
        exitTab.addActionListener(this);
        createCustomerMenuItem.addActionListener(this);
        showCustomersMenuItem.addActionListener(this);
        saveBankMenuItem.addActionListener(this);
        loadBankMenuItem.addActionListener(this);

        menuBar.add(customerMenu);
        menuBar.add(systemMenu);

        setVisible(true);
    }

    // Action handler
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == homeMenuItem) {
            cardLayout.show(mainCardPanel, "HomePanel");
        }
        if(e.getSource() == createCustomerMenuItem) {
            createCustomerPanel.clearResult();
            cardLayout.show(mainCardPanel, "CreateCustomerPanel");
        }
        if(e.getSource() == showCustomersMenuItem) {
          getAllCustomersPanel.clearCustomers();
          cardLayout.show(mainCardPanel, "GetAllCustomersPanel");
        }
        if(e.getSource() == saveBankMenuItem) {
            try {
                saveBankToFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                showPane("There was a problem saving, try again.", "Save bank");
                System.out.println("Problem saving the file");
            }
        }
        if(e.getSource() == loadBankMenuItem) {
            try {
                loadBankFromFile();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
                showPane("There are currently no customers to be loaded", "Load bank");
                System.out.println("Problem loading the file");
            }
        }
        if(e.getSource() == exitTab) {
            System.exit(0);
        }
    }

    // Method to save all the customers in the bank to a file
    private void saveBankToFile() throws IOException {
        if(!bank.writeCustomersToFile()) {
            showPane("There are currently no customers to be saved", "Save bank");
        } else {
            showPane("Customers saved successfully!", "Save bank");
        }
    }

    // Method to load all the customers in the bank from a file
    private void loadBankFromFile() throws IOException, ClassNotFoundException {
        if(!bank.loadCustomerFromFile()) {
            showPane("There are currently no customers to be loaded", "Load bank");
        }else {
            showPane("Customers loaded successfully!", "Load bank");
        }
    }

    // The pain to be shown when the user tries to save/load to/from a file
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

    public static void main(String[] args) {
        MainFrame main = new MainFrame();
    }
}
