import bookstoreapp.core.Customer;
import bookstoreapp.core.Owner;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;

public class LoginPanel extends JPanel {

    public LoginPanel(BookStoreGUI app) {
        add(new JLabel("Username:"));
        JTextField usernameField = new JTextField(12);
        add(usernameField);

        add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField(12);
        add(passwordField);

        JButton loginButton = new JButton("Login");
        add(loginButton);

        JLabel messageLabel = new JLabel("");
        add(messageLabel);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            Object user = app.getStore().authenticate(username, password);

            if (user instanceof Owner) {
                usernameField.setText("");
                passwordField.setText("");
                app.showScreen("ownerStart");
            } else if (user instanceof Customer) {
                usernameField.setText("");
                passwordField.setText("");
                app.setCurrentCustomer((Customer) user);
                app.showScreen("customerStart");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid login");
            }
        });
    }
}
