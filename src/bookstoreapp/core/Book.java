package bookstoreapp.core;

/**
 * Overview:
 * Book represents a single book in the bookstore inventory.
 * Each book has a name and a price.
 *
 * Abstraction Function:
 * AF(b) =
 *     a bookstore book item with title b.name and selling price b.price.
 *
 * Representation Invariant:
 * RI:
 *     1. name != null
 *     2. !name.trim().isEmpty()
 *     3. price >= 0
 *     4. price is finite (not NaN and not infinite)
 */
public class Book {
    private final String name;
    private final double price;

    /**
     * Requires: name is non-null and non-empty, and price is a finite
     *           non-negative number.
     * Modifies: this
     * Effects: Creates a new Book with the given name and price.
     *
     * @param name the title/name of the book
     * @param price the selling price of the book
     * @throws IllegalArgumentException if name is null or empty, or if price
     *         is negative, NaN, or infinite
     */
    public Book(String name, double price) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Book name cannot be empty.");
        }
        if (Double.isNaN(price) || Double.isInfinite(price) || price < 0) {
            throw new IllegalArgumentException("Book price must be a finite non-negative number.");
        }
        this.name = name.trim();
        this.price = price;
    }

    /**
     * Requires: None
     * Modifies: None
     * Effects: Returns the name of this book.
     *
     * @return the name of this book
     */
    public String getName() {
        return name;
    }

    /**
     * Requires: None
     * Modifies: None
     * Effects: Returns the price of this book.
     *
     * @return the price of this book
     */
    public double getPrice() {
        return price;
    }

    /**
     * Requires: None
     * Modifies: None
     * Effects: Returns a string representation of this book containing
     *          its name and price formatted to two decimal places.
     *
     * @return a string in the form "name - $price"
     */
    @Override
    public String toString() {
        return name + " - $" + String.format("%.2f", price);
    }
}