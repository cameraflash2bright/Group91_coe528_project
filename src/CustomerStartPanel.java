import bookstoreapp.core.Book;
import bookstoreapp.core.Customer;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Overview:
 * CustomerStartPanel provides the main customer interface for the bookstore
 * application. It allows the currently logged-in customer to view available
 * books, add and remove books from a shopping cart, complete purchases,
 * redeem loyalty points during checkout, and log out of the system.
 */
public class CustomerStartPanel extends JPanel {
    private BookStoreGUI app;
    private Customer currentCustomer;

    private JLabel infoLabel;
    private DefaultListModel<String> storeModel;
    private DefaultListModel<String> cartModel;
    private JList<String> storeList;
    private JList<String> cartList;
    private ArrayList<Book> cart;

    /**
     * Requires: app != null
     * Modifies: this
     * Effects: Creates the customer start panel, initializes all GUI
     *          components, sets up the store and cart displays, and
     *          attaches button listeners for cart management, checkout,
     *          and logout actions.
     *
     * @param app the main bookstore GUI controller used to access the
     *        shared bookstore backend and screen navigation
     */
    public CustomerStartPanel(BookStoreGUI app) {
        this.app = app;
        this.cart = new ArrayList<>();

        setLayout(new BorderLayout());

        infoLabel = new JLabel("Customer Start Screen", JLabel.CENTER);
        add(infoLabel, BorderLayout.NORTH);

        storeModel = new DefaultListModel<>();
        cartModel = new DefaultListModel<>();
        storeList = new JList<>(storeModel);
        cartList = new JList<>(cartModel);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        centerPanel.add(new JScrollPane(storeList));
        centerPanel.add(new JScrollPane(cartList));
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton addToCartButton = new JButton("Add to Cart");
        JButton removeFromCartButton = new JButton("Remove from Cart");
        JButton buyButton = new JButton("Buy");
        JButton redeemBuyButton = new JButton("Redeem Points and Buy");
        JButton logoutButton = new JButton("Logout");

        bottomPanel.add(addToCartButton);
        bottomPanel.add(removeFromCartButton);
        bottomPanel.add(buyButton);
        bottomPanel.add(redeemBuyButton);
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);

        addToCartButton.addActionListener(e -> {
            int selectedIndex = storeList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "Select a book to add.");
                return;
            }

            Book selectedBook = app.getStore().getBooks().get(selectedIndex);
            cart.add(selectedBook);
            cartModel.addElement(selectedBook.getName() + " - $" + selectedBook.getPrice());
        });

        removeFromCartButton.addActionListener(e -> {
            int selectedIndex = cartList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "Select a cart item to remove.");
                return;
            }

            cart.remove(selectedIndex);
            cartModel.remove(selectedIndex);
        });

        buyButton.addActionListener(e -> checkout(false));
        redeemBuyButton.addActionListener(e -> checkout(true));
        logoutButton.addActionListener(e -> app.logout());
    }

    /**
     * Requires: customer != null
     * Modifies: this.currentCustomer, this.cart, this.cartModel, displayed GUI state
     * Effects: Sets the currently logged-in customer for this panel,
     *          clears any previous cart contents, and refreshes the
     *          displayed customer information and book list.
     *
     * @param customer the customer who is now using this panel
     */
    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
        clearSession();
        refreshView();
    }

    /**
     * Requires: None
     * Modifies: this.cart, this.cartModel
     * Effects: Clears all books from the current shopping cart and removes
     *          all cart entries from the visible cart list.
     */
    public void clearSession() {
        cart.clear();
        cartModel.clear();
    }

    /**
     * Requires: None
     * Modifies: displayed GUI state
     * Effects: Refreshes the visible book list and the displayed customer
     *          information for the current session.
     */
    public void refreshView() {
        refreshBookList();
        refreshInfo();
    }

    /**
     * Requires: app != null and app.getStore() != null
     * Modifies: this.storeModel
     * Effects: Clears the displayed store list and repopulates it with
     *          the books currently available in the bookstore inventory.
     */
    private void refreshBookList() {
        storeModel.clear();
        for (Book book : app.getStore().getBooks()) {
            storeModel.addElement(book.getName() + " - $" + book.getPrice());
        }
    }

    /**
     * Requires: None
     * Modifies: this.infoLabel
     * Effects: Updates the information label to show the current customer's
     *          username, loyalty points, and membership status. If no
     *          customer is logged in, displays the default screen title.
     */
    private void refreshInfo() {
        if (currentCustomer == null) {
            infoLabel.setText("Customer Start Screen");
            return;
        }

        infoLabel.setText(
            "Welcome, " + currentCustomer.getUsername()
            + " | Points: " + currentCustomer.getPoints()
            + " | Status: " + currentCustomer.getStatus().getStatus()
        );
    }

    /**
     * Requires: currentCustomer != null and the cart contains at least one
     *           valid book.
     * Modifies: currentCustomer, persistent store data, this.cart,
     *           this.cartModel, displayed GUI state
     * Effects: Attempts to complete a purchase for the current cart.
     *          If usePoints is true, applies the customer's available
     *          points toward the purchase. If checkout succeeds, saves
     *          the bookstore data, displays the final cost, clears the
     *          cart, and refreshes the displayed customer information.
     *          If checkout fails, shows an error dialog.
     *
     * @param usePoints true if the customer's points should be redeemed
     *        during checkout; false otherwise
     */
    private void checkout(boolean usePoints) {
        if (currentCustomer == null) {
            JOptionPane.showMessageDialog(this, "No customer is logged in.");
            return;
        }

        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.");
            return;
        }

        try {
            double finalCost = app.getStore().processPurchase(currentCustomer, new ArrayList<>(cart), usePoints);
            app.saveStore();

            JOptionPane.showMessageDialog(
                this,
                "Purchase completed. Final cost: $" + String.format("%.2f", finalCost)
            );

            clearSession();
            refreshInfo();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}