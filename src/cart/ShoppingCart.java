package cart;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    // List to store item names in the cart
    private ArrayList<String> items;
    private static final int MAX_ITEMS = 100; // Maximum number of items allowed in the cart

    // Constructor initializes an empty shopping cart
    public ShoppingCart() {
        items = new ArrayList<>();
    }

    // Adds an item to the cart if it is not full
    public void addItem(String item) {
        if (items.size() >= MAX_ITEMS) {
            System.out.println("Cart is full. Cannot add more than 100 items.");
            return;
        }

        items.add(item);
        System.out.println(item + " added to cart.");
    }

    // Removes an item from the cart if it exists
    public void removeItem(String item) {
        if (items.remove(item)) {
            System.out.println(item + " removed from cart.");
        } else {
            System.out.println("Item not found in cart.");
        }
    }

    // Displays all items currently in the cart
    public void viewCart() {
        if (items.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        System.out.println("Items in cart:");
        for (String item : items) {
            System.out.println("- " + item);
        }
    }

    // Returns the total number of items in the cart
    public int getTotalItems() {
        return items.size();
    }

    // Checks if the cart is empty
    public boolean isEmpty() {
        return items.isEmpty();
    }

    // Returns a copy of the items list (to avoid modifying original list externally)
    public List<String> getItemsSnapshot() {
        return new ArrayList<>(items);
    }

    // Clears all items from the cart
    public void clearCart() {
        items.clear();
        System.out.println("Cart cleared.");
    }

    // Returns a string representation of the cart
    @Override
    public String toString() {
        return "ShoppingCart with " + items.size() + " item(s)";
    }
}
