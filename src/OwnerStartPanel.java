import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OwnerStartPanel extends JPanel {
    public OwnerStartPanel(BookStoreGUI app) {
        add(new JLabel("Owner Start Screen"));

        JButton booksButton = new JButton("Books");
        booksButton.addActionListener(e -> app.showScreen("ownerBooks"));
        add(booksButton);

        JButton customersButton = new JButton("Customers");
        customersButton.addActionListener(e -> app.showScreen("ownerCustomers"));
        add(customersButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> app.logout());
        add(logoutButton);
    }
}
