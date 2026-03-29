package bookstoreapp.state;

import bookstoreapp.core.Customer;

/**
 * Overview:
 * State represents a customer's membership level in the bookstore loyalty
 * system. Concrete implementations such as SilverState and GoldState define
 * how a customer's membership status is interpreted and when transitions to
 * another state should occur.
 *
 * Interface Invariant / Behavioral Contract:
 * 1. Any implementation of State must represent a valid membership level.
 * 2. getStatus() must return a non-null string describing the membership level.
 * 3. checkStatus(context) must leave the customer in a valid state consistent
 *    with the customer's current number of points.
 *
 *
 */
public interface State {

    /**
     * Requires: context != null
     * Modifies: context
     * Effects: Examines the given customer's current points and updates the
     *          customer's membership state if a transition is required.
     *
     * @param context the customer whose membership status is being checked
     * @throws IllegalArgumentException if context is invalid, if enforced by
     *         the implementing class
     */
    void checkStatus(Customer context);

    /**
     * Requires: None
     * Modifies: None
     * Effects: Returns the string representation of this membership state.
     *
     * @return the name of the state, such as "Silver" or "Gold"
     */
    String getStatus();
}