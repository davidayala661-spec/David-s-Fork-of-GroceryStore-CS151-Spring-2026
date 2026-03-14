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
}