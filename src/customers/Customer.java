package customers;

import cart.ShoppingCart;

public abstract class Customer {

    protected int customerId;
    protected String firstName;
    protected String lastName;
    protected ShoppingCart cart;

    public Customer(int customerId, String firstName, String lastName) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cart = new ShoppingCart();
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void displayCustomerInfo() {
        System.out.println("Customer ID: " + customerId);
        System.out.println("Name: " + firstName + " " + lastName);
    }

    @Override
    public String toString() {
        return "Customer ID: " + customerId +
               ", Name: " + firstName + " " + lastName;
    }
}