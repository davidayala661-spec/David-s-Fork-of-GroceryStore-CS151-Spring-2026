package checkout;

import aisles.Aisles;
import customers.Customer;
import inventory.Inventory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import products.Products;

// Handles checkout process including pricing, discounts, tax, and receipt generation
public final class Checkout {

    private static final double TAX_RATE = 0.095; // Sales tax rate applied to all purchases
    private static long nextTransactionId = 1;  // Keeps track of unique transaction IDs

    // Private constructor to prevent instantiation (utility class)
    private Checkout() {
    }

    // Prints receipt without aisle data (default case)
    public static void printReceipt(Customer customer, Inventory inventory) {
        printReceipt(customer, inventory, null);
    }

    // Main checkout method that generates receipt with optional aisle support
    public static void printReceipt(Customer customer, Inventory inventory, List<Aisles> aisles) {
        List<String> rawItems = customer.getCart().getItemsSnapshot(); // Get items from customer's cart (snapshot to avoid modification issues)
        if (rawItems.isEmpty()) {
            System.out.println("Cart is empty. Nothing to checkout.");
            return;
        }

        // Generate unique transaction ID and timestamp
        long transactionId = nextTransactionId++;
        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Group duplicate items and count quantities using a map
        Map<String, Integer> lineCounts = new LinkedHashMap<>();
        for (String item : rawItems) {
            String key = item == null ? "" : item.trim();
            if (key.isEmpty()) {
                continue;
            }
            lineCounts.merge(key, 1, Integer::sum);
        }

        if (lineCounts.isEmpty()) {
            System.out.println("Cart has no valid item names. Nothing to checkout.");
            return;
        }

        double subtotal = 0.0;
        StringBuilder lineDetails = new StringBuilder();

        // Calculate subtotal by iterating through each item and its quantity
        for (Map.Entry<String, Integer> entry : lineCounts.entrySet()) {
            String name = entry.getKey();
            int qty = entry.getValue();
            double unitPrice = resolveUnitPrice(name, inventory, aisles);
            double lineTotal = unitPrice * qty;
            subtotal += lineTotal;
            lineDetails.append(String.format("  %-24s  x%-3d  @ $%-7.2f  = $%.2f%n",
                    name, qty, unitPrice, lineTotal));
        }

        // Apply discount and tax to compute final total
        double discountRate = clampDiscount(customer.getDiscountRate());
        double discountAmount = subtotal * discountRate;
        double afterDiscount = subtotal - discountAmount;
        double tax = afterDiscount * TAX_RATE;
        double total = afterDiscount + tax;

        System.out.println();
        System.out.println("========================================");
        System.out.println("            GROCERY STORE RECEIPT");
        System.out.println("========================================");
        System.out.println("Transaction ID:  " + transactionId);
        System.out.println("Customer ID:     " + customer.getCustomerId());
        System.out.println("Date / Time:     " + timestamp.format(timeFormat));
        System.out.println("----------------------------------------");
        System.out.println("ITEMS");
        System.out.print(lineDetails);
        System.out.println("----------------------------------------");
        System.out.printf("Subtotal (items):     $%.2f%n", subtotal);
        if (discountRate > 0) {
            System.out.printf("VIP discount (%.1f%%): -$%.2f%n", discountRate * 100, discountAmount);
        } else {
            System.out.println("Discount:             $0.00");
        }
        System.out.printf("After discount:       $%.2f%n", afterDiscount);
        System.out.printf("Tax (9.5%%):          $%.2f%n", tax);
        System.out.println("----------------------------------------");
        System.out.printf("TOTAL:                $%.2f%n", total);
        System.out.println("========================================");
        System.out.println();

        // Clear cart after successful checkout
        customer.getCart().clearCart();
    }

    private static double clampDiscount(double rate) {
        if (rate < 0) {
            return 0;
        }
        if (rate > 1) {
            return 1;
        }
        return rate;
    }
    // Resolve item price from inventory or aisles
    private static double resolveUnitPrice(String name, Inventory inventory, List<Aisles> aisles) {
        Products fromInventory = inventory.findProductByExactName(name);
        if (fromInventory != null) {
            return fromInventory.getPrice();
        }
        if (aisles != null) {
            for (Aisles aisle : aisles) {
                for (Products product : aisle.getAllProducts()) {
                    if (product.getName().equalsIgnoreCase(name)) {
                        return product.getPrice();
                    }
                }
            }
        }
        return 0.0;
    }
}
