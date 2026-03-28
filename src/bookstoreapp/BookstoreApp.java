/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package bookstoreapp;

/**
 *
 * @author emilylam
 */


import bookstoreapp.core.Book;
import bookstoreapp.core.Customer;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Scanner;

public class BookstoreApp extends JFrame {

    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Customer> customers = new ArrayList<>();
    
    // GUI components for single-window management 
    private JPanel mainContainer;
    private CardLayout cardLayout;

    public BookstoreApp() {
        // 1. Load data from files immediately on startup
        loadData();

        // 2. Setup the Single-Window JFrame 
        setTitle("Bookstore App");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // 3. Initialize and add screens (Panels)
        // You will need to create these Panel classes separately
        // mainContainer.add(new LoginScreen(this), "LOGIN_SCREEN");
        // mainContainer.add(new OwnerStartScreen(this), "OWNER_START");
        
        add(mainContainer);

        // 4. Requirement: Save data to files when [x] is clicked 
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveData();
                System.exit(0);
            }
        });
    }

    // Helper to switch screens within the same window 
    public void showScreen(String screenName) {
        cardLayout.show(mainContainer, screenName);
    }

    private void loadData() {
        try {
            // Logic to read from books.txt and customers.txt 
            File bookFile = new File("books.txt");
            if (bookFile.exists()) {
                Scanner s = new Scanner(bookFile);
                while (s.hasNextLine()) {
                    // Parse and add to books list
                }
            }
        } catch (Exception e) {
            System.err.println("Could not load data: " + e.getMessage());
        }
    }

    private void saveData() {
        try (PrintWriter bookWriter = new PrintWriter(new FileWriter("books.txt"));
             PrintWriter custWriter = new PrintWriter(new FileWriter("customers.txt"))) {
            
            // Iterate through lists and overwrite files 
            for (Book b : books) {
                bookWriter.println(b.getName() + ", " + b.getPrice());
            }
            // Save customers (Username, Password, Points) 
            
        } catch (IOException e) {
            System.err.println("Could not save data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Ensure GUI runs on the Event Dispatch Thread 
        SwingUtilities.invokeLater(() -> {
            new BookstoreApp().setVisible(true);
        });
    }
}
