package bookstoreapp.state;

import bookstoreapp.core.Customer;

/**
 * Overview:
 * SilverState represents the Silver membership level in the bookstore
 * loyalty system. A customer in this state remains Silver while they
 * have fewer than 1000 points. If their points reach 1000 or more,
 * their membership is upgraded to Gold.
 *
 *
 */
public class SilverState implements State {

    /**
     * Requires: context != null
     * Modifies: context
     * Effects: Checks the given customer's points. If the customer has
     *          at least 1000 points, updates the customer's state to Gold.
     *          Otherwise, the customer's state remains Silver.
     *
     * @param context the customer whose membership state is being checked
     */
    @Override
    public void checkStatus(Customer context) {
        if (context.getPoints() >= 1000) {
            context.setStatus(new GoldState());
        }
    }

    /**
     * Requires: None
     * Modifies: None
     * Effects: Returns the string representation of this state.
     *
     * @return "Silver"
     */
    @Override
    public String getStatus() {
        return "Silver";
    }
}