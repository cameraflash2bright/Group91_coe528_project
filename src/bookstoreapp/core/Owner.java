package bookstoreapp.core;

/**
 * Overview:
 * Owner represents the single administrator of the bookstore system.
 * This class follows the Singleton design pattern, meaning at most one
 * Owner object can exist during program execution. The owner's login
 * credentials are fixed as username "admin" and password "admin".
 *
 * Abstraction Function:
 * AF(instance) =
 *     the unique bookstore owner account with username = "admin"
 *     and password = "admin".
 * If instance == null, then no Owner object has been created yet.
 *
 * Representation Invariant:
 * RI:
 *     1. instance is either null or refers to the only Owner object.
 *     2. username != null
 *     3. password != null
 *     4. username.equals("admin")
 *     5. password.equals("admin")
 *
 *
 */
public class Owner {
    private static Owner instance;

    private String username = "admin";
    private String password = "admin";

    private Owner(){}

    /**
     * Requires: None
     * Modifies: Owner.instance (only on the first call)
     * Effects: Returns the single instance of Owner, creating it if necessary.
     */
    public static Owner getInstance(){
        if (instance == null){
            instance = new Owner();
        }
        return instance;
    }

    /**
     * Requires: None
     * Modifies: None
     * Effects: Returns the username of the owner.
     */
    public String getUsername(){
        return username;
    }

    /**
     * Requires: None
     * Modifies: None
     * Effects: Returns the password of the owner.
     */
    public String getPassword(){
        return password;
    }
}