/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookstoreapp.core;
import java.io.*;
import java.util.ArrayList;
/**
 *
 * @author 
 */
public class BookStore {

private ArrayList<Book> books;

private ArrayList<Customer> customers;

public BookStore(){
    
    this.books = new ArrayList<>();
    
    this.customers = new ArrayList<>();
}

// GETTERS FOR GUI
public ArrayList<Book> getBooks() { 
    return books; 
}
  public ArrayList<Customer> getCustomers() { 
      return customers; 
  }
  //LIST MANAGEMENT METHODS

    public void addBook(String name, double price) {
        books.add(new Book(name, price));
    }

    public void deleteBook(Book book) {
        books.remove(book);
    }

    public void addCustomer(String username, String password) {
        customers.add(new Customer(username, password));
    }

    public void deleteCustomer(Customer customer) {
        customers.remove(customer);
    }
    
    //AUTHENTICATION 

    /**
     * Authenticates a user based on credentials.
     * Returns the Owner instance, a Customer instance, or null.
     */
    public Object authenticate(String username, String password) {
        
        Owner owner = Owner.getInstance();
        
        if (owner.getUsername().equals(username) && owner.getPassword().equals(password)) {
            return owner;
        }

        for (Customer c : customers) {
            if (c.getUsername().equals(username) && c.getPassword().equals(password)) {
                return c;
            }
        }
        
        return null; // Login failed
    }
    
    //PURCHASE LOGIC
    /**
     * Processes a customer purchase, applying points if requested.
     */
    
    public double processPurchase(Customer customer, ArrayList<Book> cart, boolean usePoints) {
        
        double totalCost = 0;
        
        for (Book b : cart) {
            totalCost += b.getPrice();
        }

        if (usePoints) {
            int pointsAvailable = customer.getPoints();
            double maxDiscount = pointsAvailable / 100.0; // 100 points = $1 deduction

            if (maxDiscount >= totalCost) {
                // Points fully cover the cost
                int pointsToDeduct = (int) (totalCost * 100);
                customer.deductPoints(pointsToDeduct);
                return 0.0; // Final cost is 0
            } else {
                // Points partially cover the cost
                double finalCost = totalCost - maxDiscount;
                customer.deductPoints(pointsAvailable); // Drains points to 0
                customer.addPoints((int) (finalCost * 10)); // Earn points on the remaining cash balance paid
                return finalCost;
            }
        } else {
            // Not using points, just pay total cost and earn points
            customer.addPoints((int) (totalCost * 10));
            return totalCost;
        }
    }
    
    // FILE HANDLING

    /**
     * Saves all current books and customers to text files.
     */
    public void saveData() {
       
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("books.txt"))) {
            for (Book b : books) {
                bw.write(b.getName() + "," + b.getPrice());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("customers.txt"))) {
            for (Customer c : customers) {
                bw.write(c.getUsername() + "," + c.getPassword() + "," + c.getPoints());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }

    /**
     * Loads books and customers from text files into memory.
     */
    public void loadData() {
        
        books.clear();
        customers.clear();

        try (BufferedReader br = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    addBook(parts[0], Double.parseDouble(parts[1]));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("books.txt not found. Starting with empty inventory.");
        } catch (IOException e) {
            System.out.println("Error reading books: " + e.getMessage());
        }

        try (BufferedReader br = new BufferedReader(new FileReader("customers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    Customer c = new Customer(parts[0], parts[1]);
                    c.addPoints(Integer.parseInt(parts[2])); // Add saved points, which auto-updates state
                    customers.add(c);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("customers.txt not found. Starting with empty customer list.");
        } catch (IOException e) {
            System.out.println("Error reading customers: " + e.getMessage());
        }
    }
}

