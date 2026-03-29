package bookstoreapp.state;

import bookstoreapp.core.Customer;

/**
 * Overview:
 * GoldState represents the Gold membership level in the bookstore
 * loyalty system. A customer in this state remains Gold while they
 * have at least 1000 points. If their points fall below 1000,
 * their membership is downgraded to Silver.
 *
 * 
 */
public class GoldState implements State {

    /**
     * Requires: context != null
     * Modifies: context
     * Effects: Checks the given customer's points. If the customer has
     *          fewer than 1000 points, updates the customer's state to Silver.
     *          Otherwise, the customer's state remains Gold.
     *
     * @param context the customer whose membership state is being checked
     */
    @Override
    public void checkStatus(Customer context) {
        if (context.getPoints() < 1000) {
            context.setStatus(new SilverState());
        }
    }

    /**
     * Requires: None
     * Modifies: None
     * Effects: Returns the string representation of this state.
     *
     * @return "Gold"
     */
    @Override
    public String getStatus() {
        return "Gold";
    }
}