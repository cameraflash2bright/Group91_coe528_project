package bookstoreapp.tests;

import bookstoreapp.core.Book;
import bookstoreapp.core.BookStore;
import bookstoreapp.core.Customer;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BookStoreValidationTest {

    @Test
    public void rejectsInvalidBookInput() {
        BookStore store = new BookStore();

        assertThrows(IllegalArgumentException.class,
                () -> store.addBook("", 10.0));
        assertThrows(IllegalArgumentException.class,
                () -> store.addBook("Valid", -5.0));
        assertThrows(IllegalArgumentException.class,
                () -> store.addBook("Bad NaN", Double.NaN));
    }

    @Test
    public void rejectsDuplicateUsernamesAndReservedAdmin() {
        BookStore store = new BookStore();
        store.addCustomer("alice", "pw");

        assertThrows(IllegalArgumentException.class,
                () -> store.addCustomer("alice", "other"));
        assertThrows(IllegalArgumentException.class,
                () -> store.addCustomer("admin", "other"));
    }

    @Test
    public void rejectsBadPurchaseInputs() {
        BookStore store = new BookStore();
        Customer customer = new Customer("bob", "pw");
        ArrayList<Book> cart = new ArrayList<>();
        cart.add(new Book("Clean Code", 20.0));

        assertThrows(IllegalArgumentException.class,
                () -> store.processPurchase(null, cart, false));
        assertThrows(IllegalArgumentException.class,
                () -> store.processPurchase(customer, null, false));
        assertThrows(IllegalArgumentException.class,
                () -> store.processPurchase(customer, new ArrayList<Book>(), false));

        ArrayList<Book> cartWithNull = new ArrayList<>();
        cartWithNull.add(null);
        assertThrows(IllegalArgumentException.class,
                () -> store.processPurchase(customer, cartWithNull, false));
    }

    @Test
    public void calculatesPurchaseAndPointsCorrectly() {
        BookStore store = new BookStore();
        Customer customer = new Customer("cathy", "pw");
        ArrayList<Book> cart = new ArrayList<>();
        cart.add(new Book("A", 50.0));
        cart.add(new Book("B", 25.0));

        double finalCost = store.processPurchase(customer, cart, false);
        assertEquals(75.0, finalCost, 0.0001);
        assertEquals(750, customer.getPoints());
        assertEquals("Silver", customer.getStatus().getStatus());

        customer.addPoints(300);
        assertEquals(1050, customer.getPoints());
        assertEquals("Gold", customer.getStatus().getStatus());

        ArrayList<Book> smallCart = new ArrayList<>();
        smallCart.add(new Book("C", 5.50));
        double discounted = store.processPurchase(customer, smallCart, true);

        assertEquals(0.0, discounted, 0.0001);
        assertEquals(500, customer.getPoints());
        assertEquals("Silver", customer.getStatus().getStatus());
    }

    @Test
    public void inventoryAndCustomerListsReflectSuccessfulAdds() {
        BookStore store = new BookStore();

        store.addBook("Algorithms", 89.50);
        store.addCustomer("dana", "pw");

        assertEquals(1, store.getBooks().size());
        assertEquals(1, store.getCustomers().size());
        assertEquals("Algorithms", store.getBooks().get(0).getName());
        assertEquals("dana", store.getCustomers().get(0).getUsername());
    }
}