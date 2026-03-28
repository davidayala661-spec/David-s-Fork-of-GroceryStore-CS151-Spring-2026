package customers;

// Represents a regular customer in the system
public class RegularCustomer extends Customer {

    // Constructor for a regular customer
    public RegularCustomer(int customerId, String firstName, String lastName) {
        super(customerId, firstName, lastName);
    }

    // Allows a regular customer to sign up for VIP membership
    public void signUpForVIP() {
        System.out.println(firstName + " " + lastName + " signed up for VIP membership.");
    }
}
