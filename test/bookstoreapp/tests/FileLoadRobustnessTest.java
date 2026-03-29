package bookstoreapp.tests;

import bookstoreapp.core.BookStore;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import static org.junit.Assert.*;

public class FileLoadRobustnessTest {

    @Test
    public void skipsMalformedBookAndCustomerLines() throws Exception {
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "bookstore-tests-" + System.nanoTime());
        tempDir.mkdir();

        File books = new File(tempDir, "books.txt");
        File customers = new File(tempDir, "customers.txt");

        PrintWriter bookWriter = new PrintWriter(new FileWriter(books));
        bookWriter.println("Valid Book,19.99");
        bookWriter.println("BrokenBookLine");
        bookWriter.println("BadPrice,abc");
        bookWriter.println("Negative,-2.0");
        bookWriter.close();

        PrintWriter customerWriter = new PrintWriter(new FileWriter(customers));
        customerWriter.println("alice,pw,250");
        customerWriter.println("brokencustomerline");
        customerWriter.println("bob,pw,notANumber");
        customerWriter.println("charlie,pw,-10");
        customerWriter.close();

        BookStore store = new BookStore();
        store.loadData(books.getAbsolutePath(), customers.getAbsolutePath());

        assertEquals(1, store.getBooks().size());
        assertEquals("Valid Book", store.getBooks().get(0).getName());

        assertEquals(1, store.getCustomers().size());
        assertEquals("alice", store.getCustomers().get(0).getUsername());
        assertEquals(250, store.getCustomers().get(0).getPoints());
    }

    @Test
    public void saveAndReloadRoundTripWorks() throws Exception {
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "bookstore-roundtrip-" + System.nanoTime());
        tempDir.mkdir();

        File books = new File(tempDir, "books.txt");
        File customers = new File(tempDir, "customers.txt");

        BookStore original = new BookStore();
        original.addBook("Algorithms", 89.50);
        original.addCustomer("dana", "pw");
        original.getCustomers().get(0).addPoints(1200);
        original.saveData(books.getAbsolutePath(), customers.getAbsolutePath());

        BookStore reloaded = new BookStore();
        reloaded.loadData(books.getAbsolutePath(), customers.getAbsolutePath());

        assertEquals(1, reloaded.getBooks().size());
        assertEquals(1, reloaded.getCustomers().size());
        assertEquals("Gold", reloaded.getCustomers().get(0).getStatus().getStatus());
        assertEquals(1200, reloaded.getCustomers().get(0).getPoints());
    }
}