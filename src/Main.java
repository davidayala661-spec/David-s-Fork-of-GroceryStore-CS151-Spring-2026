import customers.RegularCustomer;
import customers.VIPCustomer;
import inventory.Inventory;
import products.Products;
import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // =========================
        // CUSTOMER SETUP
        // =========================
        RegularCustomer customer1 = new RegularCustomer(101, "Vishal", "Raichur");
        VIPCustomer customer2 = new VIPCustomer(102, "John", "Doe", 0.10);

        customer1.getCart().addItem("Milk");
        customer1.getCart().addItem("Bread");
        customer2.getCart().addItem("Eggs");
        customer2.addPoints(50);

        // =========================
        // INVENTORY TEST SETUP
        // =========================
        Inventory inventory = new Inventory();

        Products apples = new Products("Apples", 1.99, 20, 1001);
        Products milk = new Products("Milk", 3.49, 8, 1002);
        Products chips = new Products("Chips", 2.99, 5, 1003);
        Products rice = new Products("Rice", 10.99, 50, 1004);

        System.out.println("=================================");
        System.out.println("   Welcome to the Grocery Store  ");
        System.out.println("=================================");

        // =========================
        // INVENTORY NORMAL TEST CASES
        // =========================
        System.out.println("\n========== INVENTORY TESTS ==========");

        try {
            inventory.addProduct("Produce", apples);
            inventory.addProduct("Dairy", milk);
            inventory.addProduct("Snacks", chips);
            inventory.addProduct("Supplies", rice);
            System.out.println("Added 4 products successfully.");
        } catch (Exception e) {
            System.out.println("Error while adding products: " + e.getMessage());
        }

        System.out.println("\n--- Print Inventory ---");
        inventory.printInventory();

        System.out.println("\n--- Get Product (valid) ---");
        Products foundProduct = inventory.getProduct("Produce", 1001);
        if (foundProduct != null) {
            System.out.println("Found: " + foundProduct);
        } else {
            System.out.println("Product not found.");
        }

        System.out.println("\n--- Find Product by ID (valid) ---");
        Products searchedProduct = inventory.findProduct(1002);
        if (searchedProduct != null) {
            System.out.println("Found: " + searchedProduct);
        } else {
            System.out.println("Product not found.");
        }

        System.out.println("\n--- Search By Name: 'i' ---");
        List<Products> nameMatches = inventory.searchByName("i");
        for (Products p : nameMatches) {
            System.out.println(p);
        }

        System.out.println("\n--- Low Stock Products (< 10) ---");
        List<Products> lowStock = inventory.listLowStock(10);
        for (Products p : lowStock) {
            System.out.println(p);
        }

        System.out.println("\n--- Restock Milk by 10 ---");
        try {
            inventory.restockProduct("Dairy", 1002, 10);
            System.out.println("Milk restocked successfully.");
        } catch (Exception e) {
            System.out.println("Restock error: " + e.getMessage());
        }
        inventory.printInventory();

        System.out.println("\n--- Decrease Apples stock by 5 ---");
        try {
            inventory.decreaseStock("Produce", 1001, 5);
            System.out.println("Apples stock decreased successfully.");
        } catch (Exception e) {
            System.out.println("Decrease stock error: " + e.getMessage());
        }
        inventory.printInventory();

        System.out.println("\n--- Remove Chips from Snacks ---");
        try {
            inventory.removeProduct("Snacks", 1003);
            System.out.println("Chips removed successfully.");
        } catch (Exception e) {
            System.out.println("Remove product error: " + e.getMessage());
        }
        inventory.printInventory();

        // =========================
        // INVENTORY EDGE CASES
        // =========================
        System.out.println("\n========== INVENTORY EDGE CASES ==========");

        System.out.println("\n--- Edge Case: Add duplicate product ID in same section ---");
        try {
            Products duplicateApples = new Products("Green Apples", 2.49, 15, 1001);
            inventory.addProduct("Produce", duplicateApples);
        } catch (Exception e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n--- Edge Case: Get product from invalid section ---");
        Products invalidSectionProduct = inventory.getProduct("Frozen", 9999);
        if (invalidSectionProduct == null) {
            System.out.println("Expected result: product not found in invalid section.");
        }

        System.out.println("\n--- Edge Case: Find product with invalid ID ---");
        Products invalidIdProduct = inventory.findProduct(9999);
        if (invalidIdProduct == null) {
            System.out.println("Expected result: product ID 9999 not found.");
        }

        System.out.println("\n--- Edge Case: Restock missing product ---");
        try {
            inventory.restockProduct("Produce", 9999, 5);
        } catch (Exception e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n--- Edge Case: Decrease stock more than available ---");
        try {
            inventory.decreaseStock("Produce", 1001, 999);
        } catch (Exception e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n--- Edge Case: Remove missing product ---");
        try {
            inventory.removeProduct("Snacks", 9999);
        } catch (Exception e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n--- Edge Case: Search by name with no matches ---");
        List<Products> noMatches = inventory.searchByName("zzz");
        if (noMatches.isEmpty()) {
            System.out.println("Expected result: no products matched.");
        }

        System.out.println("\n--- Edge Case: Low stock threshold 3 ---");
        List<Products> veryLowStock = inventory.listLowStock(3);
        if (veryLowStock.isEmpty()) {
            System.out.println("No products below threshold 3.");
        } else {
            for (Products p : veryLowStock) {
                System.out.println(p);
            }
        }

        // Uncomment this only if your Inventory capacity logic is finished and working
        /*
        System.out.println("\n--- Edge Case: Exceed max inventory capacity ---");
        try {
            for (int i = 2000; i < 2105; i++) {
                inventory.addProduct("Overflow", new Products("Item" + i, 1.00, 1, i));
            }
        } catch (Exception e) {
            System.out.println("Expected capacity error: " + e.getMessage());
        }
        */

        // =========================
        // MENU
        // =========================
        int choice = -1;

        while (choice != 7) {
            System.out.println("\n===== Grocery Store Menu =====");
            System.out.println("1. View Regular Customer Info");
            System.out.println("2. View Regular Customer Cart");
            System.out.println("3. Add Item to Regular Customer Cart");
            System.out.println("4. Remove Item from Regular Customer Cart");
            System.out.println("5. Clear Regular Customer Cart");
            System.out.println("6. View VIP Customer Benefits");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    customer1.displayCustomerInfo();
                    break;

                case 2:
                    customer1.getCart().viewCart();
                    System.out.println("Total items: " + customer1.getCart().getTotalItems());
                    break;

                case 3:
                    System.out.print("Enter item to add: ");
                    String addItem = scanner.nextLine();
                    customer1.getCart().addItem(addItem);
                    break;

                case 4:
                    System.out.print("Enter item to remove: ");
                    String removeItem = scanner.nextLine();
                    customer1.getCart().removeItem(removeItem);
                    break;

                case 5:
                    customer1.getCart().clearCart();
                    break;

                case 6:
                    customer2.displayCustomerInfo();
                    customer2.viewVIPBenefits();
                    customer2.getCart().viewCart();
                    break;

                case 7:
                    System.out.println("Thank you for using the Grocery Store System!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}