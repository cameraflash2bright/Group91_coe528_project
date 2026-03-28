import bookstoreapp.core.Book;
import bookstoreapp.core.Customer;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;

public class CustomerStartPanel extends JPanel {
    private BookStoreGUI app;
    private Customer currentCustomer;

    private JLabel infoLabel;
    private DefaultListModel<String> storeModel;
    private DefaultListModel<String> cartModel;
    private JList<String> storeList;
    private JList<String> cartList;
    private ArrayList<Book> cart;

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

    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
        clearSession();
        refreshView();
    }

    public void clearSession() {
        cart.clear();
        cartModel.clear();
    }

    public void refreshView() {
        refreshBookList();
        refreshInfo();
    }

    private void refreshBookList() {
        storeModel.clear();
        for (Book book : app.getStore().getBooks()) {
            storeModel.addElement(book.getName() + " - $" + book.getPrice());
        }
    }

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

    private void checkout(boolean usePoints) {
        if (currentCustomer == null) {
            JOptionPane.showMessageDialog(this, "No customer is logged in.");
            return;
        }

        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.");
            return;
        }

        double finalCost = app.getStore().processPurchase(currentCustomer, new ArrayList<>(cart), usePoints);
        app.saveStore();

        JOptionPane.showMessageDialog(
            this,
            "Purchase completed. Final cost: $" + String.format("%.2f", finalCost)
        );

        clearSession();
        refreshInfo();
    }
}
