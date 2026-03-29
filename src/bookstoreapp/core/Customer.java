package bookstoreapp.core;

import bookstoreapp.state.SilverState;
import bookstoreapp.state.State;

/**
 * Overview:
 * Customer represents a user of the bookstore system who can log in,
 * purchase books, accumulate loyalty points, and hold a membership
 * status such as Silver or Gold.
 *
 * Abstraction Function:
 * AF(c) =
 *     a bookstore customer account with username c.username,
 *     password c.password, loyalty points c.points, and membership
 *     status c.status.
 *
 * Representation Invariant:
 * RI:
 *     1. username != null and !username.trim().isEmpty()
 *     2. password != null and !password.trim().isEmpty()
 *     3. points >= 0
 *     4. status != null
 *     5. status is consistent with points:
 *        - if points < 1000, status should represent Silver
 *        - if points >= 1000, status should represent Gold
 */
public class Customer {
    private final String username;
    private final String password;
    private int points;
    private State status;

    /**
     * Requires: username and password are non-null and non-empty.
     * Modifies: this
     * Effects: Creates a new customer with the given username and password,
     *          0 loyalty points, and Silver status.
     */
    public Customer(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        this.username = username.trim();
        this.password = password;
        this.points = 0;
        this.status = new SilverState();
    }

    /**
     * Requires: None
     * Modifies: None
     * Effects: Returns the customer's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Requires: None
     * Modifies: None
     * Effects: Returns the customer's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Requires: None
     * Modifies: None
     * Effects: Returns the customer's current loyalty points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Requires: None
     * Modifies: None
     * Effects: Returns the customer's current membership status.
     */
    public State getStatus() {
        return status;
    }

    /**
     * Requires: status != null
     * Modifies: this.status
     * Effects: Sets the customer's membership status to the given state.
     */
    public void setStatus(State status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        this.status = status;
    }

    /**
     * Requires: points >= 0
     * Modifies: this.points, this.status
     * Effects: Adds the given number of points to the customer's total
     *          and updates the customer's membership status if needed.
     */
    public void addPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points to add cannot be negative.");
        }
        this.points += points;
        updateStatus();
    }

    /**
     * Requires: points >= 0 and points <= this.points
     * Modifies: this.points, this.status
     * Effects: Deducts the given number of points from the customer's total
     *          without allowing the balance to become negative, then updates
     *          the customer's membership status if needed.
     */
    public void deductPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points to deduct cannot be negative.");
        }
        if (points > this.points) {
            throw new IllegalArgumentException("Cannot deduct more points than the customer has.");
        }
        this.points -= points;
        updateStatus();
    }

    /**
     * Requires: None
     * Modifies: this.status
     * Effects: Updates the customer's membership status based on the
     *          current number of loyalty points.
     */
    public void updateStatus() {
        if (status == null) {
            status = new SilverState();
        }
        status.checkStatus(this);
    }

    /**
     * Requires: None
     * Modifies: None
     * Effects: Returns a string representation of the customer including
     *          username, current status, and loyalty points.
     */
    @Override
    public String toString() {
        return username + " (" + status.getStatus() + ", " + points + " pts)";
    }
}