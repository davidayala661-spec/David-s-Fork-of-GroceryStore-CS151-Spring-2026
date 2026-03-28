package products;

import exceptions.InvalidPriceException;
import exceptions.InvalidProductException;
import exceptions.InvalidQuantityException;

public class Products {
    private String name;
    private double price;
    private int quantity;
    private int id;

    public Products(String name, double price, int quantity, int id)
            throws InvalidProductException, InvalidPriceException, InvalidQuantityException {

        if (name == null || name.trim().isEmpty()) {
            throw new InvalidProductException("Product name cannot be empty.");
        }

        if (price < 0) {
            throw new InvalidPriceException("Price cannot be negative.");
        }

        if (quantity < 0) {
            throw new InvalidQuantityException("Quantity cannot be negative.");
        }
        if (id <= 0) {
            throw new InvalidProductException("Product ID must be greater than 0.");
        }

        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getID() {
        return id;
    }

    public void setName(String name) {
        // Reject null OR blank
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidProductException("Product name cannot be empty.");
        }

        // Otherwise update normally
        this.name = name.trim();
    }

    public void setPrice(double price) throws InvalidPriceException {
        if (price < 0) {
            throw new InvalidPriceException("Price cannot be negative.");
        }
        this.price = price;
    }

    public void setQuantity(int quantity) throws InvalidQuantityException {
        if (quantity < 0) {
            throw new InvalidQuantityException("Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }

    // used by Stocker
    public void stockToShelf(int amount) throws InvalidQuantityException {
        if (amount <= 0) {
            throw new InvalidQuantityException("Stock amount must be positive.");
        }
        this.quantity += amount;
    }

    @Override
    public String toString() {
        return "Product: " + name + ", ID: " + id + ", Price: $" + price + ", Quantity: " + quantity;
    }
}