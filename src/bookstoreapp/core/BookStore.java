package bookstoreapp.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Overview: BookStore represents the core backend of the bookstore system. It
 * maintains the current inventory of books and the list of registered
 * customers, supports authentication, processes purchases, and handles
 * loading/saving bookstore data from files.
 *
 * Abstraction Function: AF(bs) = a bookstore system whose current inventory is
 * bs.books and whose registered customer accounts are bs.customers. The store
 * supports: - adding and removing books from inventory - adding and removing
 * customers - authenticating either the single owner account or a customer
 * account - processing customer purchases and loyalty point updates -
 * persisting and restoring store data using text files
 *
 * Representation Invariant: RI: 1. books != null 2. customers != null 3. every
 * element of books is non-null 4. every element of customers is non-null 5. no
 * customer in customers has username "admin" 6. no two customers in customers
 * have the same username 7. every Book in books satisfies the Book class
 * invariant 8. every Customer in customers satisfies the Customer class
 * invariant
 */
public class BookStore {

    private static final String DEFAULT_BOOKS_FILE = "books.txt";
    private static final String DEFAULT_CUSTOMERS_FILE = "customers.txt";

    private final ArrayList<Book> books;
    private final ArrayList<Customer> customers;

    /**
     * Requires: None Modifies: this Effects: Creates a new empty bookstore with
     * no books in inventory and no registered customers.
     */
    public BookStore() {
        this.books = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    /**
     * Requires: None Modifies: None Effects: Returns the list of books
     * currently in the bookstore inventory.
     */
    public ArrayList<Book> getBooks() {
        return books;
    }

    /**
     * Requires: None Modifies: None Effects: Returns the list of customers
     * currently registered in the bookstore.
     */
    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    /**
     * Requires: name is valid and price is a valid non-negative value.
     * Modifies: this.books Effects: Creates a new Book with the given name and
     * price and adds it to the bookstore inventory.
     */
    public void addBook(String name, double price) {
        books.add(new Book(name, price));
    }

    /**
     * Requires: book != null and book is contained in this.books. Modifies:
     * this.books Effects: Removes the given book from the bookstore inventory.
     * Throws IllegalArgumentException if book is null or not found.
     */
    public void deleteBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book to delete cannot be null.");
        }
        if (!books.remove(book)) {
            throw new IllegalArgumentException("Selected book was not found in inventory.");
        }
    }

    /**
     * Requires: username and password are valid, username is not reserved for
     * the owner, and no existing customer has the same username. Modifies:
     * this.customers Effects: Creates a new Customer with the given username
     * and password and adds it to the list of registered customers. Throws
     * IllegalArgumentException if the username is invalid, reserved, or already
     * exists.
     */
    public void addCustomer(String username, String password) {
        if (Owner.getInstance().getUsername().equals(username)) {
            throw new IllegalArgumentException("That username is reserved for the owner.");
        }
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) {
                throw new IllegalArgumentException("That username already exists.");
            }
        }
        customers.add(new Customer(username, password));
    }

    /**
     * Requires: customer != null and customer is contained in this.customers.
     * Modifies: this.customers Effects: Removes the given customer from the
     * list of registered customers. Throws IllegalArgumentException if customer
     * is null or not found.
     */
    public void deleteCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer to delete cannot be null.");
        }
        if (!customers.remove(customer)) {
            throw new IllegalArgumentException("Selected customer was not found.");
        }
    }

    /**
     * Requires: None Modifies: None Effects: If the given username and password
     * match the owner account, returns the Owner object. If they match a
     * registered customer, returns that Customer object. Otherwise returns
     * null.
     */
    public Object authenticate(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        String normalizedUsername = username.trim();
        String normalizedPassword = password.trim();
        if (normalizedUsername.isEmpty() || normalizedPassword.isEmpty()) {
            return null;
        }

        Owner owner = Owner.getInstance();
        if (owner.getUsername().equals(normalizedUsername) && owner.getPassword().equals(normalizedPassword)) {
            return owner;
        }

        for (Customer c : customers) {
            if (c.getUsername().equals(normalizedUsername) && c.getPassword().equals(normalizedPassword)) {
                return c;
            }
        }

        return null;
    }

    /**
     * Requires: customer != null, cart != null, cart is non-empty, and every
     * book in cart is non-null and has a valid non-negative price. Modifies:
     * customer Effects: Computes the total purchase cost for the given cart. If
     * usePoints is false, awards loyalty points based on the total cost and
     * returns the full total. If usePoints is true, applies the customer's
     * available points as a discount, updates the customer's points/status, and
     * returns the final amount owed. Throws IllegalArgumentException if the
     * inputs are invalid.
     */
    public double processPurchase(Customer customer, ArrayList<Book> cart, boolean usePoints) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null.");
        }
        if (cart.isEmpty()) {
            throw new IllegalArgumentException("Cart cannot be empty.");
        }

        double totalCost = 0.0;
        for (Book b : cart) {
            if (b == null) {
                throw new IllegalArgumentException("Cart cannot contain null books.");
            }
            if (Double.isNaN(b.getPrice()) || Double.isInfinite(b.getPrice()) || b.getPrice() < 0) {
                throw new IllegalArgumentException("Cart contains a book with an invalid price.");
            }
            totalCost += b.getPrice();
        }

        if (usePoints) {
            int pointsAvailable = customer.getPoints();
            double maxDiscount = pointsAvailable / 100.0;

            if (maxDiscount >= totalCost) {
                int pointsToDeduct = (int) Math.round(totalCost * 100);
                customer.deductPoints(pointsToDeduct);
                return 0.0;
            }

            double finalCost = totalCost - maxDiscount;
            customer.deductPoints(pointsAvailable);
            customer.addPoints((int) (finalCost * 10));
            return finalCost;
        }

        customer.addPoints((int) (totalCost * 10));
        return totalCost;
    }

    /**
     * Requires: None Modifies: External files used for persistent storage.
     * Effects: Saves the current books and customers to the default books and
     * customers files.
     */
    public void saveData() {
        saveData(DEFAULT_BOOKS_FILE, DEFAULT_CUSTOMERS_FILE);
    }

    /**
     * Requires: booksFile and customersFile are valid non-empty file paths.
     * Modifies: The files identified by booksFile and customersFile. Effects:
     * Writes all books to booksFile and all customers to customersFile. Throws
     * IllegalArgumentException if a file path is invalid and
     * IllegalStateException if a file cannot be written.
     */
    public void saveData(String booksFile, String customersFile) {
        validateFilePath(booksFile, "books file");
        validateFilePath(customersFile, "customers file");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(booksFile))) {
            for (Book b : books) {
                bw.write(b.getName() + "," + b.getPrice());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save books to '" + booksFile + "'.", e);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(customersFile))) {
            for (Customer c : customers) {
                bw.write(c.getUsername() + "," + c.getPassword() + "," + c.getPoints());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save customers to '" + customersFile + "'.", e);
        }
    }

    /**
     * Requires: None Modifies: this.books, this.customers Effects: Clears the
     * current bookstore data and reloads books and customers from the default
     * books and customers files.
     */
    public void loadData() {
        loadData(DEFAULT_BOOKS_FILE, DEFAULT_CUSTOMERS_FILE);
    }

    /**
     * Requires: booksFile and customersFile are valid non-empty file paths.
     * Modifies: this.books, this.customers Effects: Clears the current
     * bookstore data and reloads books and customers from the specified files.
     * Malformed lines are skipped rather than causing the application to crash.
     */
    public void loadData(String booksFile, String customersFile) {
        validateFilePath(booksFile, "books file");
        validateFilePath(customersFile, "customers file");

        books.clear();
        customers.clear();

        loadBooksFromFile(booksFile);
        loadCustomersFromFile(customersFile);
    }

    /**
     * Requires: booksFile is a valid non-empty file path. Modifies: this.books
     * Effects: Reads book records from the given file and adds valid books to
     * the bookstore inventory. Malformed or invalid records are skipped.
     */
    private void loadBooksFromFile(String booksFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(booksFile))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", 2);
                if (parts.length != 2) {
                    warn("Skipping malformed book record on line " + lineNumber + ".");
                    continue;
                }

                try {
                    addBook(parts[0], Double.parseDouble(parts[1].trim()));
                } catch (IllegalArgumentException ex) {
                    warn("Skipping invalid book record on line " + lineNumber + ": " + ex.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            warn(booksFile + " not found. Starting with empty inventory.");
        } catch (IOException e) {
            warn("Error reading books from '" + booksFile + "': " + e.getMessage());
        }
    }

    /**
     * Requires: customersFile is a valid non-empty file path. Modifies:
     * this.customers Effects: Reads customer records from the given file and
     * adds valid customers to the bookstore. Malformed or invalid records are
     * skipped.
     */
    private void loadCustomersFromFile(String customersFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(customersFile))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", 3);
                if (parts.length != 3) {
                    warn("Skipping malformed customer record on line " + lineNumber + ".");
                    continue;
                }

                try {
                    int points = Integer.parseInt(parts[2].trim());
                    if (points < 0) {
                        throw new IllegalArgumentException("Saved points cannot be negative.");
                    }
                    Customer c = new Customer(parts[0], parts[1]);
                    c.addPoints(points);
                    customers.add(c);
                } catch (IllegalArgumentException ex) {
                    warn("Skipping invalid customer record on line " + lineNumber + ": " + ex.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            warn(customersFile + " not found. Starting with empty customer list.");
        } catch (IOException e) {
            warn("Error reading customers from '" + customersFile + "': " + e.getMessage());
        }
    }

    /**
     * Requires: None Modifies: None Effects: Verifies that the given file path
     * is non-null and non-empty. Throws IllegalArgumentException if the path is
     * invalid.
     */
    private void validateFilePath(String path, String label) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("The " + label + " path cannot be empty.");
        }
    }

    /**
     * Requires: None Modifies: Standard error output Effects: Prints the given
     * warning message to the error stream.
     */
    private void warn(String message) {
        System.err.println("[BookStore] " + message);
    }

}
