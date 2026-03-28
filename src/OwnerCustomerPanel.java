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

public class OwnerCustomerPanel extends JPanel {
    private DefaultTableModel model;
    private JTable table;
    private BookStoreGUI app;

    public OwnerCustomerPanel(BookStoreGUI app) {
        this.app = app;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Owner Customers Screen", JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Username", "Password", "Points", "Status"}, 0) {
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

            for (Customer customer : app.getStore().getCustomers()) {
                if (customer.getUsername().equals(username)) {
                    JOptionPane.showMessageDialog(this, "That username already exists.");
                    return;
                }
            }

            app.getStore().addCustomer(username, password);
            app.saveStore();
            refreshTable();
            usernameField.setText("");
            passwordField.setText("");
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a customer to delete.");
                return;
            }

            Customer selectedCustomer = app.getStore().getCustomers().get(selectedRow);
            app.getStore().deleteCustomer(selectedCustomer);
            app.saveStore();
            refreshTable();
        });

        backButton.addActionListener(e -> app.showScreen("ownerStart"));
    }

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
