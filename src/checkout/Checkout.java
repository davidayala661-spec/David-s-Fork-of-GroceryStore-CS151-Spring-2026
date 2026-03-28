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

public final class Checkout {

    private static final double TAX_RATE = 0.095;
    private static long nextTransactionId = 1;

    private Checkout() {
    }

    public static void printReceipt(Customer customer, Inventory inventory) {
        printReceipt(customer, inventory, null);
    }

    public static void printReceipt(Customer customer, Inventory inventory, List<Aisles> aisles) {
        List<String> rawItems = customer.getCart().getItemsSnapshot();
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
            lineDetails.append(String.format("  %-24s  x%-3d  @ $%-7.2f  = $%.2f%n",
                    name, qty, unitPrice, lineTotal));
        }

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
