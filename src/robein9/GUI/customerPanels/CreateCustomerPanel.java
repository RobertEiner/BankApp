/**
 * Description
 * This panel has one purpose, to create a new customer and add it to the bank. Personal number, first name
 * and last name are necessary.
 *
 * @author Robert Einer, robein-9
 */

package robein9.GUI.customerPanels;
import robein9.BankLogic;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateCustomerPanel extends JPanel implements ActionListener {
    private BankLogic bank;
    private JButton createCustomer;
    private JLabel label, resultLabel;
    private JTextField pNo, firstName, lastName;
    private JPanel panel = new JPanel();
    private JPanel invalidInputPanel = new JPanel();

    public CreateCustomerPanel(BankLogic bank) {
        initiateInstanceVariables(bank);
        buildPanel();
    }

    public void initiateInstanceVariables(BankLogic bank) {
        this.bank = bank;

        pNo = new JTextField(20);
        firstName = new JTextField(20);
        lastName = new JTextField(20);

        createCustomer = new JButton("Create");
        label = new JLabel("Create customer");
        resultLabel = new JLabel("");
    }

    // Build the panel
    public void buildPanel() {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        invalidInputPanel.setLayout(new BoxLayout(invalidInputPanel, BoxLayout.Y_AXIS));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 4;
        gbc.insets = new Insets(0, 0, 10, 0);
        this.add(panel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        this.add(invalidInputPanel, gbc);
        pNo.setMaximumSize(new Dimension(300, 40));
        firstName.setMaximumSize(new Dimension(300, 40));
        lastName.setMaximumSize(new Dimension(300, 40));
        //this.setLayout( new BoxLayout(this, BoxLayout.Y_AXIS));
        pNo.setBorder(BorderFactory.createTitledBorder("Personal number"));
        firstName.setBorder(BorderFactory.createTitledBorder("First name"));
        lastName.setBorder(BorderFactory.createTitledBorder("Last name"));

        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 10,0));
        panel.add(label);
        panel.add(pNo);
        panel.add(firstName);
        panel.add(lastName);
        panel.add(createCustomer);
        invalidInputPanel.add(resultLabel);

        createCustomer.addActionListener(this);
    }

    // Action handler
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == createCustomer) {
            addCustomer();
        }
    }

    // This method will add a new customer to the bank
    private void addCustomer() {
        resultLabel.setText("");
        if(validateInput()) {
            boolean customerCreated = bank.createCustomer(firstName.getText(), lastName.getText(), pNo.getText());
            if(customerCreated) {
                resultLabel.setText("Customer created successfully!");
                clearFields();
            } else {
                resultLabel.setText("Customer could not be created, try again.");
                clearFields();
            }
        } else {
            resultLabel.setText("Invalid input. Input fields cannot be empty, personal number must be digits!");
            panel.revalidate();
        }
    }

    // Clear all input fields after creating a customer
    public void clearFields() {
        pNo.setText("");
        firstName.setText("");
        lastName.setText("");
    }

    // Validate that personal number is only digits and first and last name are not empty
    public boolean validateInput() {
        for(int i = 0; i < pNo.getText().length(); i++) {
            if(!Character.isDigit(pNo.getText().charAt(i))) {
                return false;
            }
        }
        return !pNo.getText().equals("") && !firstName.getText().equals("") && !lastName.getText().equals("");
    }


    public void clearResult() {
        resultLabel.setText("");
    }


}
