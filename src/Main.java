import aisles.Aisles;
import customers.RegularCustomer;
import customers.VIPCustomer;
import data.StoreDataLoader;
import exceptions.CapacityExceededException;
import exceptions.DuplicateProductException;
import exceptions.InvalidPriceException;
import exceptions.InvalidProductException;
import exceptions.InvalidQuantityException;
import exceptions.InvalidSectionException;
import exceptions.NotFoundException;
import input.ConsoleInput;
import inventory.Inventory;
import java.util.List;
import java.util.Scanner;
import menu.StoreMenus;
import products.Products;
import shelf.Shelf;

public class Main {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Customer setup
        RegularCustomer customer1 = new RegularCustomer(101, "Vishal", "Raichur");
        VIPCustomer customer2 = new VIPCustomer(102, "John", "Doe", 0.10);

        customer1.getCart().addItem("Milk");
        customer1.getCart().addItem("Bread");
        customer2.getCart().addItem("Eggs");
        customer2.addPoints(50);

        // Inventory setup
        Inventory inventory = new Inventory();
        List<Aisles> aisles = StoreDataLoader.loadDefaultAisles();

        System.out.println("=================================");
        System.out.println("   Welcome to the Grocery Store  ");
        System.out.println("=================================");

        // Preload shelflist

        Shelf produceShelf;
        Shelf dairyShelf;
        Shelf snacksShelf;
        Shelf suppliesShelf;

        try {
            produceShelf = new Shelf("Produce");
            dairyShelf = new Shelf("Dairy");
            snacksShelf = new Shelf("Snacks");
            suppliesShelf = new Shelf("Supplies");
        } catch (InvalidSectionException e) {
            System.out.println("Shelf setup error: " + e.getMessage());
            scanner.close();
            return;
        }

        // Preload inventory and shelves with exception handling
        try {
            // inventory
            inventory.addProduct("Produce", new Products("Apples", 1.99, 20, 1001));
            inventory.addProduct("Dairy", new Products("Milk", 3.49, 8, 1002));
            inventory.addProduct("Snacks", new Products("Chips", 2.99, 5, 1003));
            inventory.addProduct("Supplies", new Products("Rice", 10.99, 50, 1004));
            inventory.addProduct("Produce", new Products("Mango", 1.99, 70, 1005));
            inventory.addProduct("Produce", new Products("Onion", 2.99, 100, 1006));
            System.out.println("Inventory preloaded successfully.");

            // Load shelves with products from inventory

            // produce
            Products apples = inventory.getProduct("Produce", 1001);
            Products mango = inventory.getProduct("Produce", 1005);
            Products onion = inventory.getProduct("Produce", 1006);

            produceShelf.addProduct(new Products(apples.getName(), apples.getPrice(), 2, apples.getID()));
            produceShelf.addProduct(new Products(mango.getName(), mango.getPrice(), 7, mango.getID()));
            produceShelf.addProduct(new Products(onion.getName(), onion.getPrice(), 10, onion.getID()));

            // dairy
            Products milk = inventory.getProduct("Dairy", 1002);
            dairyShelf.addProduct(new Products(milk.getName(), milk.getPrice(), 3, milk.getID()));

            // Snack
            Products chips = inventory.getProduct("Snacks", 1003);
            snacksShelf.addProduct(new Products(chips.getName(), chips.getPrice(),2, chips.getID()));

            // Supplies
            //            inventory.addProduct("Supplies", new Products("Rice", 10.99, 50, 1004));

            Products rice = inventory.getProduct("Supplies", 1004);
            suppliesShelf.addProduct(new Products(rice.getName(), rice.getPrice(),9, rice.getID()));


        } catch (CapacityExceededException | InvalidSectionException | InvalidProductException
                | DuplicateProductException e) {
            System.out.println("Setup error: " + e.getMessage());
        }   

        // Automatic inventory tests
        System.out.println("\n========== INVENTORY TESTS ==========");

        System.out.println("\n--- Print Inventory ---");
        inventory.printInventory();

        System.out.println("\n--- Find Product by ID (valid) ---");
        try {
            Products found = inventory.findProduct(1002);
            System.out.println("Found: " + found);
        } catch (NotFoundException e) {
            System.out.println("Find error: " + e.getMessage());
        }

        System.out.println("\n--- Search By Name: 'i' ---");
        List<Products> matches = inventory.searchByName("i");
        if (matches.isEmpty()) {
            System.out.println("No matching products found.");
        } else {
            for (Products product : matches) {
                System.out.println(product);
            }
        }

        System.out.println("\n--- Low Stock Products (< 10) ---");
        List<Products> lowStock = inventory.listLowStock(10);
        if (lowStock.isEmpty()) {
            System.out.println("No low-stock products.");
        } else {
            for (Products product : lowStock) {
                System.out.println(product);
            }
        }

        System.out.println("\n--- Restock Milk by 10 ---");
        try {
            inventory.restockProduct("Dairy", 1002, 10);
            System.out.println("Restock successful.");
        } catch (NotFoundException | InvalidQuantityException e) {
            System.out.println("Restock error: " + e.getMessage());
        }

        System.out.println("\n--- Decrease Apples stock by 5 ---");
        try {
            inventory.decreaseStock("Produce", 1001, 5);
            System.out.println("Decrease stock successful.");
        } catch (NotFoundException | InvalidQuantityException e) {
            System.out.println("Decrease stock error: " + e.getMessage());
        }

        System.out.println("\n--- Remove Chips from Snacks ---");
        try {
            inventory.removeProduct("Snacks", 1003);
            System.out.println("Remove product successful.");
        } catch (NotFoundException e) {
            System.out.println("Remove error: " + e.getMessage());
        }

        System.out.println("\n--- Updated Inventory ---");
        inventory.printInventory();

        System.out.println("\n========== INVENTORY EDGE CASES ==========");

        System.out.println("\n--- Edge Case: duplicate product ID ---");
        try {
            inventory.addProduct("Produce", new Products("Green Apples", 2.49, 15, 1001));
        } catch (CapacityExceededException | InvalidSectionException | InvalidProductException
                | DuplicateProductException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n--- Edge Case: invalid product ID ---");
        try {
            inventory.findProduct(9999);
        } catch (NotFoundException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n--- Edge Case: invalid restock quantity ---");
        try {
            inventory.restockProduct("Dairy", 1002, 0);
        } catch (NotFoundException | InvalidQuantityException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n--- Edge Case: insufficient stock ---");
        try {
            inventory.decreaseStock("Produce", 1001, 999);
        } catch (NotFoundException | InvalidQuantityException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n--- Edge Case: remove missing product ---");
        try {
            inventory.removeProduct("Snacks", 9999);
        } catch (NotFoundException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        int roleChoice = -1;
        while (roleChoice != 4) {
            System.out.println("\n===== Grocery Store =====");
            System.out.println("Who are you?");
            System.out.println("1. Regular customer");
            System.out.println("2. VIP customer");
            System.out.println("3. Employee");
            System.out.println("4. Exit");
            roleChoice = ConsoleInput.readInt(scanner, "Enter your choice: ");

            switch (roleChoice) {
                case 1:
                    StoreMenus.runRegularCustomerSession(scanner, customer1, inventory, aisles);
                    break;
                case 2:
                    StoreMenus.runVipCustomerSession(scanner, customer2, inventory, aisles, customer1);
                    break;
                case 3:
                    StoreMenus.runEmployeeSession(scanner, inventory, aisles, customer1, customer2,
                            produceShelf, dairyShelf, snacksShelf, suppliesShelf);
                    break;
                case 4:
                    System.out.println("Thank you for using the Grocery Store System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}
