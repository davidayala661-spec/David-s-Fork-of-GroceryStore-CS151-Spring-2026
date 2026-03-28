package cart;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    private ArrayList<String> items;
    private static final int MAX_ITEMS = 100;

    public ShoppingCart() {
        items = new ArrayList<>();
    }

    public void addItem(String item) {
        if (items.size() >= MAX_ITEMS) {
            System.out.println("Cart is full. Cannot add more than 100 items.");
            return;
        }

        items.add(item);
        System.out.println(item + " added to cart.");
    }

    public void removeItem(String item) {
        if (items.remove(item)) {
            System.out.println(item + " removed from cart.");
        } else {
            System.out.println("Item not found in cart.");
        }
    }

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

    public int getTotalItems() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public List<String> getItemsSnapshot() {
        return new ArrayList<>(items);
    }

    public void clearCart() {
        items.clear();
        System.out.println("Cart cleared.");
    }

    @Override
    public String toString() {
        return "ShoppingCart with " + items.size() + " item(s)";
    }
}