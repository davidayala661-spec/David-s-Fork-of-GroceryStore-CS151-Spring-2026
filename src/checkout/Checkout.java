package checkout;

import aisles.Aisles;
import customers.Customer;
import customers.VIPCustomer;
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

    public static void printReceipt(Customer customer, Inventory inventory) {
        printReceipt(customer, inventory, null);
    }

    // Prints receipt without aisle data (default case)
    // Main checkout method that generates receipt with optional aisle support
    public static void printReceipt(Customer customer, Inventory inventory, List<Aisles> aisles) {
        List<String> rawItems = customer.getCart().getItemsSnapshot(); // get items from Customer cart

        if (rawItems.isEmpty()) {
            System.out.println("Cart is empty. Nothing to checkout.");
            return;
        }

        long transactionId = nextTransactionId++;
        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

        for (Map.Entry<String, Integer> entry : lineCounts.entrySet()) {
            String name = entry.getKey();
            int qty = entry.getValue();
            double unitPrice = resolveUnitPrice(name, inventory, aisles);
            double lineTotal = unitPrice * qty;
            subtotal += lineTotal;

            lineDetails.append(String.format(
                    "  %-24s  x%-3d  @ $%-7.2f  = $%.2f%n",
                    name, qty, unitPrice, lineTotal));
        }

        double discountRate = clampDiscount(customer.getDiscountRate());
        double discountAmount = subtotal * discountRate;
        double afterDiscount = subtotal - discountAmount;
        double tax = afterDiscount * TAX_RATE;
        double total = afterDiscount + tax;

        StringBuilder receipt = new StringBuilder();
        receipt.append(System.lineSeparator());
        receipt.append("========================================").append(System.lineSeparator());
        receipt.append("            GROCERY STORE RECEIPT").append(System.lineSeparator());
        receipt.append("========================================").append(System.lineSeparator());
        receipt.append("Transaction ID:  ").append(transactionId).append(System.lineSeparator());
        receipt.append("Customer ID:     ").append(customer.getCustomerId()).append(System.lineSeparator());
        receipt.append("Customer Name:   ").append(customer.getFullName()).append(System.lineSeparator());
        receipt.append("Date / Time:     ").append(timestamp.format(timeFormat)).append(System.lineSeparator());
        receipt.append("----------------------------------------").append(System.lineSeparator());
        receipt.append("ITEMS").append(System.lineSeparator());
        receipt.append(lineDetails);
        receipt.append("----------------------------------------").append(System.lineSeparator());
        receipt.append(String.format("Subtotal (items):     $%.2f%n", subtotal));

        if (discountRate > 0) {
            receipt.append(String.format("VIP discount (%.1f%%): -$%.2f%n",
                    discountRate * 100, discountAmount));
        } else {
            receipt.append("Discount:             $0.00").append(System.lineSeparator());
        }

        receipt.append(String.format("After discount:       $%.2f%n", afterDiscount));
        receipt.append(String.format("Tax (9.5%%):           $%.2f%n", tax));
        receipt.append("----------------------------------------").append(System.lineSeparator());
        receipt.append(String.format("TOTAL:                $%.2f%n", total));
        receipt.append("========================================").append(System.lineSeparator());

        System.out.println(receipt);

        customer.addPurchaseRecord(receipt.toString());

        if (customer instanceof VIPCustomer vipCustomer) {
            int pointsEarned = (int) total;
            vipCustomer.addPoints(pointsEarned);
            System.out.println("VIP points earned: " + pointsEarned);
            System.out.println("Total VIP points: " + vipCustomer.getPoints());
        }

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

    // Helper method to find a product by name across all aisles
    private static Products findProductByName(String name, List<Aisles> aislesList) {
        if (name == null || aislesList == null) return null;

        for (Aisles aisle : aislesList) {
            for (List<Products> shelfProducts : aisle.getDirectShelves().values()) {
                for (Products product : shelfProducts) {

                    // trim + case-insensitive comparison
                    if (product.getName().trim().equalsIgnoreCase(name.trim())) {
                        return product;
                    }
                }
            }
        }
        return null;
    }
}
