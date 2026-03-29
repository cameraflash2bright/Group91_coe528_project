package bookstoreapp.tests;

import bookstoreapp.core.Customer;
import bookstoreapp.state.GoldState;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerStateTransitionTest {

    @Test
    public void newCustomerStartsAsSilverWithZeroPoints() {
        Customer customer = new Customer("newUser", "pw");

        assertEquals("Silver", customer.getStatus().getStatus());
        assertEquals(0, customer.getPoints());
    }

    @Test
    public void customerPromotesAndDemotesBasedOnPoints() {
        Customer customer = new Customer("sam", "pw");

        customer.addPoints(1000);
        assertEquals("Gold", customer.getStatus().getStatus());

        customer.deductPoints(1);
        assertEquals("Silver", customer.getStatus().getStatus());
    }

    @Test
    public void setStatusUpdatesToProvidedState() {
        Customer customer = new Customer("kim", "pw");

        customer.setStatus(new GoldState());

        assertEquals("Gold", customer.getStatus().getStatus());
    }

    @Test
    public void rejectsInvalidPointOperations() {
        Customer customer = new Customer("alex", "pw");

        assertThrows(IllegalArgumentException.class,
                () -> customer.addPoints(-1));
        assertThrows(IllegalArgumentException.class,
                () -> customer.deductPoints(-1));
        assertThrows(IllegalArgumentException.class,
                () -> customer.deductPoints(1));
    }

    @Test
    public void rejectsNullStatus() {
        Customer customer = new Customer("jamie", "pw");

        assertThrows(IllegalArgumentException.class,
                () -> customer.setStatus(null));
    }
}