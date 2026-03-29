import bookstoreapp.core.Customer;
import bookstoreapp.core.Owner;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Overview:
 * LoginPanel provides the login interface for the bookstore application.
 * It allows a user to enter a username and password, validates the input,
 * and attempts to authenticate the user through the bookstore backend.
 * Depending on the credentials entered, the panel routes the user to either
 * the owner screen or the customer screen.
 */
public class LoginPanel extends JPanel {

    /**
     * Requires: app != null
     * Modifies: this, displayed GUI state
     * Effects: Creates the login panel, initializes the username and password
     *          input fields, creates the login button, and attaches a listener
     *          that validates input, attempts authentication, and switches to
     *          the appropriate screen if login succeeds.
     *
     * @param app the main bookstore GUI controller used to access the
     *        shared bookstore backend and screen navigation
     */
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

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter both username and password.");
                return;
            }

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