import bookstoreapp.core.Book;

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
 * OwnerBooksPanel provides the owner interface for viewing, adding,
 * and deleting books in the bookstore inventory. It displays all books
 * in a table and allows the owner to create new book entries or remove
 * existing ones from the inventory.
 */
public class OwnerBooksPanel extends JPanel {
    private DefaultTableModel model;
    private JTable table;
    private BookStoreGUI app;

    /**
     * Requires: app != null
     * Modifies: this
     * Effects: Creates the owner book management panel, initializes the
     *          table used to display books, sets up the input fields and
     *          buttons, and attaches listeners for adding, deleting, and
     *          navigating back from the books screen.
     *
     * @param app the main bookstore GUI controller used to access the
     *        shared bookstore backend and screen navigation
     */
    public OwnerBooksPanel(BookStoreGUI app) {
        this.app = app;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Owner Books Screen", JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Book Name", "Book Price"}, 0) {
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
        JTextField nameField = new JTextField(10);
        JTextField priceField = new JTextField(10);
        JButton addButton = new JButton("Add");

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
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
            String name = nameField.getText().trim();
            String priceText = priceField.getText().trim();

            if (name.isEmpty() || priceText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter both name and price.");
                return;
            }

            try {
                double price = Double.parseDouble(priceText);
                app.getStore().addBook(name, price);
                app.saveStore();
                refreshTable();
                nameField.setText("");
                priceField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Price must be a number.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a book to delete.");
                return;
            }

            try {
                Book selectedBook = app.getStore().getBooks().get(selectedRow);
                app.getStore().deleteBook(selectedBook);
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
     * Effects: Clears the book table and repopulates it with the current
     *          list of books from the bookstore inventory. Each row shows
     *          a book's name and price.
     */
    public void refreshTable() {
        model.setRowCount(0);
        for (Book book : app.getStore().getBooks()) {
            model.addRow(new Object[]{book.getName(), book.getPrice()});
        }
    }
}