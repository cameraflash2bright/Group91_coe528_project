/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookstoreapp.core;

/**
 *
 * @author titan
 */
public class Book {
    private String name;
    private double price;
    
    public Book(String name, double price){
        this.price = price;
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public double getPrice(){
        return price;
    }
}
