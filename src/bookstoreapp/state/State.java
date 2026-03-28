/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookstoreapp.state;
import bookstoreapp.core.Customer;
/**
 *
 * @author justin grewal 501165447
 */
public interface State {
    /**
     * Checks customers point balance and updates membership state if necessary
     * Requires: a valid customer object
     * Modifies: context(the customers state)
     * Effects:Changes the customers State to Silver or Gold based on the amount of accumulated points
     *
     */
    void checkStatus(Customer context);
    /**
     * gets the string rep of the state
     * Requires: none
     * Modifies" none
     * Effects: Returns "Silver" or "Gold" 
     *
     */
    String getStatus();
}