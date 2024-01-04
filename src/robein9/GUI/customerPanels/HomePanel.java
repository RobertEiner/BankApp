
/**
 * Description
 * This is the first panel that the user sees upon starting the application, a welcome screen.
 *
 * @author Robert Einer, robein-9
 */


package robein9.GUI.customerPanels;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    private JLabel topLabel, bottomLabel, iconLabel;
    private JPanel topPanel = new JPanel();
    private JPanel bottomPanel = new JPanel();
    private ImageIcon icon = new ImageIcon(getClass().getResource("../../robein9_files/bank.png"));

    public HomePanel() {
        initiateInstanceVariables();
        buildPanel();
    }

    public void initiateInstanceVariables() {
        topLabel = new JLabel("Welcome to the bank!");
        iconLabel = new JLabel();
        bottomLabel = new JLabel("You will find the menu on the top left of the window.");
        topPanel = new JPanel();
        bottomPanel = new JPanel();
    }

    // Build the panel
    public void buildPanel() {
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        this.add(topPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(bottomPanel, gbc);
        Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);

        iconLabel.setIcon(icon);
       // label.setBorder(BorderFactory.createEmptyBorder(0, 0, 10,0));
        topPanel.add(iconLabel);
        topPanel.add(topLabel);
        bottomPanel.add(bottomLabel);


    }



}
