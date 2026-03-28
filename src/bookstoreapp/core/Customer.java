/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookstoreapp.core;
import bookstoreapp.state.State;
import bookstoreapp.state.SilverState;
/**
 *
 * @author justin grewal 501165447
 */
public class Customer {
    private String username;
    private String password;
    private int points;
    private State status;
    
    public Customer(String username, String password){
        this.username = username;
        this.password = password;
        this.points = 0;
        this.status = new SilverState();
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public int getPoints(){
        return points;
    }
    public State getStatus(){
        return status;
    }
    public void setStatus(State status){
        this.status = status;
    }
    
    /**
     * adds points and triggers a state check
     * Requires: points > 0
     * Modifies: this.points
     * Effects: Increases points and updates status if enough points are collected
     */
    public void addPoints(int points){
        this.points += points;
        updateStatus();
    }
    
    /**
     * deducts points and triggers state check
     * Requires: points >=0 and points <= this.points*/
     /** Modifies:this.points
     * Effects: Decreases points and updates status as needed
     */
    
    public void deductPoints(int points){
        this.points -= points;
        updateStatus();
    }
    
    /** Calls the current state to evaluate if state change is needed
     * Requires: none
     * Modifies: this.state(if needed)
     * Effects: delegates to the state pattern to handle promotion and demotion
     */
    public void updateStatus(){
        this.status.checkStatus(this);
    }
}