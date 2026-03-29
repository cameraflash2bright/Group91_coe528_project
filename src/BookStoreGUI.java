import bookstoreapp.core.BookStore;
import bookstoreapp.core.Customer;

import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Overview:
 * BookStoreGUI is the main controller class for the bookstore application's
 * graphical user interface. It creates the main application window, manages
 * screen navigation using a CardLayout, holds the shared bookstore backend,
 * tracks the currently logged-in customer, and coordinates interactions
 * between the GUI panels and the backend.
 */
public class BookStoreGUI {
    private JFrame frame;
    private JPanel cards;
    private CardLayout cardLayout;

    private BookStore store;
    private Customer currentCustomer;

    private LoginPanel loginPanel;
    private OwnerStartPanel ownerStartPanel;
    private CustomerStartPanel customerStartPanel;
    private OwnerBooksPanel ownerBooksPanel;
    private OwnerCustomerPanel ownerCustomerPanel;

    /**
     * Requires: None
     * Modifies: this, displayed GUI state, persistent store state
     * Effects: Creates the main bookstore GUI, initializes the shared
     *          bookstore backend, attempts to load saved bookstore data,
     *          creates all application panels, adds them to the card layout,
     *          configures the main application window, and displays the
     *          login screen.
     */
    public BookStoreGUI() {
        store = new BookStore();
        try {
            store.loadData();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
        }

        frame = new JFrame("BookStore App");
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this);
        ownerStartPanel = new OwnerStartPanel(this);
        customerStartPanel = new CustomerStartPanel(this);
        ownerBooksPanel = new OwnerBooksPanel(this);
        ownerCustomerPanel = new OwnerCustomerPanel(this);

        cards.add(loginPanel, "login");
        cards.add(ownerStartPanel, "ownerStart");
        cards.add(customerStartPanel, "customerStart");
        cards.add(ownerBooksPanel, "ownerBooks");
        cards.add(ownerCustomerPanel, "ownerCustomers");

        frame.add(cards);
        frame.setSize(900, 650);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            /**
             * Requires: None
             * Modifies: persistent store state, displayed GUI state
             * Effects: Saves the bookstore data, closes the application window,
             *          and terminates the program when the user attempts to
             *          close the window.
             *
             * @param e the window event generated when the user closes the window
             */
            @Override
            public void windowClosing(WindowEvent e) {
                saveStore();
                frame.dispose();
                System.exit(0);
            }
        });
        frame.setVisible(true);

        showScreen("login");
    }

    /**
     * Requires: name != null
     * Modifies: displayed GUI state
     * Effects: Refreshes the appropriate panel if necessary and switches
     *          the visible screen to the panel associated with the given
     *          card name.
     *
     * @param name the name of the screen to display
     */
    public void showScreen(String name) {
        if ("ownerBooks".equals(name)) {
            ownerBooksPanel.refreshTable();
        } else if ("ownerCustomers".equals(name)) {
            ownerCustomerPanel.refreshTable();
        } else if ("customerStart".equals(name)) {
            customerStartPanel.refreshView();
        }
        cardLayout.show(cards, name);
    }

    /**
     * Requires: None
     * Modifies: None
     * Effects: Returns the shared bookstore backend used by the GUI.
     *
     * @return the bookstore backend object
     */
    public BookStore getStore() {
        return store;
    }

    /**
     * Requires: customer != null
     * Modifies: this.currentCustomer, displayed GUI state
     * Effects: Sets the currently logged-in customer and updates the
     *          customer start panel to reflect the new active customer.
     *
     * @param customer the customer who has successfully logged in
     */
    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
        customerStartPanel.setCurrentCustomer(customer);
    }

    /**
     * Requires: None
     * Modifies: None
     * Effects: Returns the currently logged-in customer, or null if no
     *          customer is currently logged in.
     *
     * @return the current customer, or null if none is logged in
     */
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    /**
     * Requires: None
     * Modifies: this.currentCustomer, displayed GUI state
     * Effects: Logs out the current customer, clears the customer session,
     *          and returns the application to the login screen.
     */
    public void logout() {
        currentCustomer = null;
        customerStartPanel.clearSession();
        showScreen("login");
    }

    /**
     * Requires: None
     * Modifies: persistent store state
     * Effects: Attempts to save the bookstore data. Returns true if saving
     *          succeeds. If saving fails, displays an error dialog and
     *          returns false.
     *
     * @return true if the bookstore data was saved successfully; false otherwise
     */
    public boolean saveStore() {
        try {
            store.saveData();
            return true;
        } catch (IllegalStateException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Requires: None
     * Modifies: displayed GUI state
     * Effects: Launches the bookstore GUI application on the Swing event
     *          dispatch thread.
     *
     * @param args command-line arguments, not used
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BookStoreGUI::new);
    }
}