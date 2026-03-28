/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookstoreapp.core;

/**
 *
 * @author justin grewal 501165447
 * this class will require the use of the singleton design method
 */
public class Owner {
    private static Owner instance;
    
    private String username = "admin";
    private String password = "admin";
    
    private Owner(){}
    /**
     * Requires: None
     * Modifies: this.instance (only on its first call)
     * Effects: Returns the single instance of Owner, creating if necessary 
     */
    public static Owner getInstance(){
        if (instance == null){
            instance = new Owner();
        }
        return instance;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
}