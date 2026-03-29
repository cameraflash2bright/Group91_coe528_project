import bookstoreapp.core.Customer;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Overview:
 * OwnerCustomerPanel provides the owner interface for viewing, adding,
 * and deleting bookstore customers. It displays all registered customers
 * in a table and allows the owner to create new customer accounts or
 * remove existing ones from the bookstore system.
 */
public class OwnerCustomerPanel extends JPanel {
    private DefaultTableModel model;
    private JTable table;
    private BookStoreGUI app;

    /**
     * Requires: app != null
     * Modifies: this
     * Effects: Creates the owner customer management panel, initializes
     *          the table used to display customers, sets up the input
     *          fields and buttons, and attaches listeners for adding,
     *          deleting, and navigating back from the customer screen.
     *
     * @param app the main bookstore GUI controller used to access the
     *        shared bookstore backend and screen navigation
     */
    public OwnerCustomerPanel(BookStoreGUI app) {
        this.app = app;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Owner Customers Screen", JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Username", "Password", "Points", "Status"}, 0) {
            /**
             * Requires: None
             * Modifies: None
             * Effects: Returns false so that table cells cannot be edited
             *          directly by the user.
             *
             * @param row the row index of the cell
             * @param column the column index of the cell
             * @return false in all cases
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        JTextField usernameField = new JTextField(10);
        JTextField passwordField = new JTextField(10);
        JButton addButton = new JButton("Add");

        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);
        inputPanel.add(addButton);

        centerPanel.add(inputPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");

        bottomPanel.add(deleteButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter both username and password.");
                return;
            }

            try {
                app.getStore().addCustomer(username, password);
                app.saveStore();
                refreshTable();
                usernameField.setText("");
                passwordField.setText("");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a customer to delete.");
                return;
            }

            try {
                Customer selectedCustomer = app.getStore().getCustomers().get(selectedRow);
                app.getStore().deleteCustomer(selectedCustomer);
                app.saveStore();
                refreshTable();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        backButton.addActionListener(e -> app.showScreen("ownerStart"));
    }

    /**
     * Requires: app != null and app.getStore() != null
     * Modifies: this.model
     * Effects: Clears the customer table and repopulates it with the
     *          current list of registered customers from the bookstore.
     *          Each row shows a customer's username, password, points,
     *          and membership status.
     */
    public void refreshTable() {
        model.setRowCount(0);
        for (Customer customer : app.getStore().getCustomers()) {
            model.addRow(new Object[]{
                customer.getUsername(),
                customer.getPassword(),
                customer.getPoints(),
                customer.getStatus().getStatus()
            });
        }
    }
}