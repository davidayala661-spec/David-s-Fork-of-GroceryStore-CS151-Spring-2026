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

    public void findItemInCart(String itemName) {
    if (itemName == null || itemName.trim().isEmpty()) {
        System.out.println("Please enter a valid item name.");
        return;
    }

    int count = 0;

    for (String item : items) {
        if (item != null && item.equalsIgnoreCase(itemName.trim())) {
            count++;
        }
    }

    if (count == 0) {
        System.out.println(itemName + " is not in the cart.");
    } else {
        System.out.println(itemName + " appears " + count + " time(s) in the cart.");
    }
}

public void viewCartSummary() {
    if (items.isEmpty()) {
        System.out.println("Cart is empty.");
        return;
    }

    int totalItems = items.size();
    int uniqueItems = 0;
    int duplicateItems = 0;

    ArrayList<String> checkedItems = new ArrayList<>();

    for (String item : items) {
        if (item == null || checkedItems.contains(item)) {
            continue;
        }

        checkedItems.add(item);
        uniqueItems++;

        int count = 0;
        for (String otherItem : items) {
            if (item.equalsIgnoreCase(otherItem)) {
                count++;
            }
        }

        if (count > 1) {
            duplicateItems += count - 1;
            System.out.println(item + " has " + count + " copies in the cart.");
        }
    }

    System.out.println("Cart summary:");
    System.out.println("Total items: " + totalItems);
    System.out.println("Unique items: " + uniqueItems);
    System.out.println("Duplicate items: " + duplicateItems);
}

    // Returns a string representation of the cart
    @Override
    public String toString() {
        return "ShoppingCart with " + items.size() + " item(s)";
    }
}
