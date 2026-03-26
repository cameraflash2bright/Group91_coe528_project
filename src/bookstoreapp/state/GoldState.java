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
public class GoldState implements State {

    @Override
    public void checkStatus(Customer context) {
        if (context.getPoints() < 1000) {
            context.setStatus(new SilverState());
        }
    }

    @Override
    public String getStatus() {
        return "Gold";
    }
}