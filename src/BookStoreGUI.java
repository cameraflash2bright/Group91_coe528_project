import bookstoreapp.core.BookStore;
import bookstoreapp.core.Customer;

import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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

    public BookStoreGUI() {
        store = new BookStore();
        store.loadData();

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
            @Override
            public void windowClosing(WindowEvent e) {
                store.saveData();
                frame.dispose();
                System.exit(0);
            }
        });
        frame.setVisible(true);

        showScreen("login");
    }

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

    public BookStore getStore() {
        return store;
    }

    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
        customerStartPanel.setCurrentCustomer(customer);
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void logout() {
        currentCustomer = null;
        customerStartPanel.clearSession();
        showScreen("login");
    }

    public void saveStore() {
        store.saveData();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BookStoreGUI::new);
    }
}
