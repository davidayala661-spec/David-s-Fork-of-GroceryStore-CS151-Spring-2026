import aisles.Aisles;
import customers.RegularCustomer;
import customers.VIPCustomer;
import data.StoreDataLoader;
import employee.*;
import exceptions.CapacityExceededException;
import exceptions.InvalidQuantityException;
import exceptions.NotFoundException;
import inventory.Inventory;
import java.util.List;
import java.util.Scanner;
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

        // Prelad shelflist

        Shelf produceShelf = new Shelf("Produce");
        Shelf dairyShelf = new Shelf("Dairy");
        Shelf snacksShelf = new Shelf("Snacks");
        Shelf suppliesShelf = new Shelf("Supplies");

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


        } catch (CapacityExceededException e) {
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
        } catch (CapacityExceededException e) {
            System.out.println("Capacity error: " + e.getMessage());
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

        // Menu
        int choice = -1;

        while (choice != 14) {
            System.out.println("\n===== Grocery Store Menu =====");
            System.out.println("1. View Regular Customer Info");
            System.out.println("2. View Regular Customer Cart");
            System.out.println("3. Add Item to Regular Customer Cart");
            System.out.println("4. Remove Item from Regular Customer Cart");
            System.out.println("5. Clear Regular Customer Cart");
            System.out.println("6. View VIP Customer Benefits");
            System.out.println("7. View Inventory");
            System.out.println("8. Find Product by ID");
            System.out.println("9. Restock Product");
            System.out.println("10. Decrease Product Stock");
            System.out.println("11. View Low Stock Products");
            System.out.println("12. View Aisles");
            System.out.println("13. Employee Menu");
            System.out.println("14. Exit");
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
                    String addItem = readLineInput(scanner, "Enter item to add: ");
                    customer1.getCart().addItem(addItem);
                    break;

                case 4:
                    String removeItem = readLineInput(scanner, "Enter item to remove: ");
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
                    inventory.printInventory();
                    break;

                case 8:
                    try {
                        int productId = readIntInput(scanner, "Enter product ID to find: ");

                        Products found = inventory.findProduct(productId);
                        System.out.println("Found: " + found);
                    } catch (NotFoundException e) {
                        System.out.println("Search error: " + e.getMessage());
                    }
                    break;

                case 9:
                    try {
                        String section = readLineInput(scanner, "Enter section: ");
                        int productId = readIntInput(scanner, "Enter product ID: ");
                        int quantity = readIntInput(scanner, "Enter quantity to restock: ");

                        inventory.restockProduct(section, productId, quantity);
                        System.out.println("Product restocked successfully.");
                    } catch (NotFoundException | InvalidQuantityException e) {
                        System.out.println("Restock error: " + e.getMessage());
                    }
                    break;

                case 10:
                    try {
                        String section = readLineInput(scanner, "Enter section: ");
                        int productId = readIntInput(scanner, "Enter product ID: ");
                        int quantity = readIntInput(scanner, "Enter quantity to decrease: ");

                        inventory.decreaseStock(section, productId, quantity);
                        System.out.println("Product stock decreased successfully.");
                    } catch (NotFoundException | InvalidQuantityException e) {
                        System.out.println("Decrease stock error: " + e.getMessage());
                    }
                    break;

                case 11:
                    int threshold = readIntInput(scanner, "Enter low-stock threshold: ");
                    List<Products> lowStockProducts = inventory.listLowStock(threshold);

                    if (lowStockProducts.isEmpty()) {
                        System.out.println("No low-stock products found.");
                    } else {
                        System.out.println("Low-stock products:");
                        for (Products product : lowStockProducts) {
                            System.out.println(product);
                        }
                    }
                    break;

                case 12:
                    boolean viewingAisles = true;
                    while (viewingAisles) {
                        System.out.println("\nAvailable aisles:");
                        for (Aisles aisle : aisles) {
                            System.out.println("- Aisle " + aisle.getAisleNumber() + " (" + aisle.getAisleType() + ")");
                        }
                        System.out.println("0. Back to main menu");

                        try {
                            int selectedAisleNumber = readIntInput(scanner, "Enter aisle number to view: ");

                            if (selectedAisleNumber == 0) {
                                viewingAisles = false;
                                continue;
                            }

                            Aisles selectedAisle = null;
                            for (Aisles aisle : aisles) {
                                if (aisle.getAisleNumber() == selectedAisleNumber) {
                                    selectedAisle = aisle;
                                    break;
                                }
                            }

                            if (selectedAisle == null) {
                                System.out.println("Aisle not found.");
                                continue;
                            }

                            boolean viewingSingleAisle = true;
                            while (viewingSingleAisle) {
                                selectedAisle.printAisle();
                                System.out.println("\n1. Buy from this aisle");
                                System.out.println("2. Back to aisle list");
                                int aisleAction = readIntInput(scanner, "Choose an option: ");

                                if (aisleAction == 2) {
                                    viewingSingleAisle = false;
                                    continue;
                                }

                                if (aisleAction != 1) {
                                    System.out.println("Invalid choice.");
                                    continue;
                                }

                                int productIdToBuy = readIntInput(scanner, "Enter product ID to buy: ");
                                int quantityToBuy = readIntInput(scanner, "Enter quantity to buy: ");

                                if (quantityToBuy <= 0) {
                                    System.out.println("Quantity must be greater than 0.");
                                    continue;
                                }

                                Products selectedProduct = null;
                                for (Products product : selectedAisle.getAllProducts()) {
                                    if (product.getID() == productIdToBuy) {
                                        selectedProduct = product;
                                        break;
                                    }
                                }

                                if (selectedProduct == null) {
                                    System.out.println("Product ID not found in this aisle.");
                                    continue;
                                }

                                if (selectedProduct.getQuantity() < quantityToBuy) {
                                    System.out.println("Not enough stock. Available: " + selectedProduct.getQuantity());
                                    continue;
                                }

                                selectedProduct.setQuantity(selectedProduct.getQuantity() - quantityToBuy);
                                for (int i = 0; i < quantityToBuy; i++) {
                                    customer1.getCart().addItem(selectedProduct.getName());
                                }

                                System.out.println("Added " + quantityToBuy + " x " + selectedProduct.getName() + " to cart.");
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid input.");
                        }
                    }
                    break;
                    case 13: // asks if what kind of employee you are stocker or manager
                    System.out.println("=============================");
                    System.out.println("|     Employee Menu      |");
                    System.out.println("=============================");
                    System.out.println("1. Stocker");
                    System.out.println("2. Manager");

                    int employeeTitle = -1;
                    
                    while (true) {
                        System.out.print("Enter your title: ");

                        if (!scanner.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a number.");
                            scanner.nextLine(); // clear input
                            continue;
                        }

                        employeeTitle = scanner.nextInt();
                        scanner.nextLine();

                        if (employeeTitle == 1 || employeeTitle == 2) {
                            break; // exit loop
                        } else {
                            System.out.println("Invalid choice. Please enter 1 or 2.");
                        }
                    }

                    if (employeeTitle == 1) { // stocker
                        System.out.println();
                        System.out.println("Welcome, Stocker! You can view low stock products and restock shelves.");

                        Stocker stocker = new Stocker("Doe", "John", 2001);

                        int stockerInput = -1;

                        int lowStockThreshold = 10; // default threshold

                        while (stockerInput != 4) { // loops and keeps asking till wants to leave stocker menu
                            System.out.println();
                            System.out.println("--- Stocker Menu ---");
                            System.out.println("1. View Low Stock Products");
                            System.out.println("2. Restock Product");
                            System.out.println("3. View Shelf");
                            System.out.println("4. Exit Stocker Menu");
                            System.out.print("Enter your choice: ");


                            try {
                                stockerInput = scanner.nextInt();
                                scanner.nextLine();
                            } catch (Exception e) {
                                System.out.println("Invalid input. Please enter a number.");
                                scanner.nextLine();
                                continue;
                            }

                            switch (stockerInput) {
                                case 1:
                                    System.out.println("1. Produce");
                                    System.out.println("2. Dairy");
                                    System.out.println("3. Snacks");
                                    System.out.println("4. Supplies");
                                    System.out.print("Enter section to check:");

                                    int sectionChoice = scanner.nextInt();
                                    scanner.nextLine();

                                    Shelf selectedShelf = null;

                                    switch (sectionChoice) {
                                        case 1:
                                            selectedShelf = produceShelf;
                                            break;
                                        case 2:
                                            selectedShelf = dairyShelf;
                                            break;
                                        case 3:
                                            selectedShelf = snacksShelf;
                                            break;
                                        case 4:
                                            selectedShelf = suppliesShelf;
                                            break;
                                        default:
                                            System.out.println("Invalid shelf.");
                                            break;
                                    }

                                    if (selectedShelf != null) {
                                        try {
                                            System.out.print("Enter low-stock threshold: ");
                                            lowStockThreshold = scanner.nextInt();
                                            scanner.nextLine();

                                            System.out.println();
                                            stocker.viewLowShelfStock(selectedShelf, lowStockThreshold);

                                            for (Products product : selectedShelf.getProducts()) { // shows how much is needed
                                                if (product.getQuantity() < lowStockThreshold) {
                                                    System.out.println(product.getName() + " needs "
                                                            + (lowStockThreshold - product.getQuantity())
                                                            + " more items.");
                                                }
                                            }

                                        } catch (Exception e) {
                                            System.out.println("Invalid input.");
                                            scanner.nextLine();
                                        }
                                    }

                                    break;

                                case 2: // products .stockToShelf by stocker to add shelf
                                    System.out.println();
                                    System.out.println("Choose a shelf to restock:");
                                    System.out.println("1. Produce");
                                    System.out.println("2. Dairy");
                                    System.out.println("3. Snacks");
                                    System.out.println("4. Supplies");
                                    System.out.print("Chosen shelf: ");

                                    int restockSectionChoice;
                                    try {
                                        restockSectionChoice = scanner.nextInt();
                                        scanner.nextLine();
                                    } catch (Exception e) {
                                        System.out.println("Invalid input.");
                                        scanner.nextLine();
                                        break;
                                    }

                                    Shelf selectedRestockShelf = null;
                                    String restockSection = "";

                                    switch (restockSectionChoice) {
                                        case 1:
                                            selectedRestockShelf = produceShelf;
                                            restockSection = "Produce";
                                            break;
                                        case 2:
                                            selectedRestockShelf = dairyShelf;
                                            restockSection = "Dairy";
                                            break;
                                        case 3:
                                            selectedRestockShelf = snacksShelf;
                                            restockSection = "Snacks";
                                            break;
                                        case 4:
                                            selectedRestockShelf = suppliesShelf;
                                            restockSection = "Supplies";
                                            break;
                                        default:
                                            System.out.println("Invalid shelf.");
                                            break;
                                    }

                                    if (selectedRestockShelf == null) {
                                        break;
                                    }

                                    if (selectedRestockShelf.getProducts().isEmpty()) {
                                        System.out.println("This shelf is empty.");
                                        break;
                                    }

                                    System.out.println();
                                    System.out.println("Reminder " + lowStockThreshold + " is the max.");
                                    System.out.println("Products on " + restockSection + " shelf:");
                                    int itemNumber = 1;

                                    for (Products product : selectedRestockShelf.getProducts()) {
                                        System.out.println(itemNumber + ". " + product.getName()
                                                + " (ID: " + product.getID()
                                                + ", Current Stock: " + product.getQuantity() + ")");
                                        itemNumber++;
                                    }

                                    System.out.print("Choose product to restock: ");
                                    int productChoice;

                                    try {
                                        productChoice = scanner.nextInt();
                                        scanner.nextLine();
                                    } catch (Exception e) {
                                        System.out.println("Invalid input.");
                                        scanner.nextLine();
                                        break;
                                    }

                                    Products chosenProduct = null;
                                    int currentIndex = 1;
                                    
                                    for (Products product : selectedRestockShelf.getProducts()) {
                                        if (currentIndex == productChoice) {
                                            chosenProduct = product;
                                            break;
                                        }
                                        currentIndex++;
                                    }

                                    if (chosenProduct == null) {
                                        System.out.println("Invalid product choice.");
                                        break;
                                    }

                                    System.out.print("Enter quantity to restock: ");
                                    int restockAmount;

                                    try {
                                        restockAmount = scanner.nextInt();
                                        scanner.nextLine();
                                    } catch (Exception e) {
                                        System.out.println("Invalid input.");
                                        scanner.nextLine();
                                        break;
                                    }

                                    if (restockAmount <= 0) {
                                        System.out.println("Quantity must be greater than 0.");
                                        break;
                                    }

                                    int newQuantity = chosenProduct.getQuantity() + restockAmount;

                                    if (newQuantity > lowStockThreshold) {
                                        System.out.println("Error: restock amount exceeds threshold of " + lowStockThreshold + ".");
                                        System.out.println(chosenProduct.getName() + " would become " + newQuantity + " in stock.");
                                        break;
                                    }

                                    chosenProduct.stockToShelf(restockAmount);
                                    System.out.println("Restocked successfully.");
                                    System.out.println(chosenProduct.getName() + " now has " + chosenProduct.getQuantity() + " on the shelf.");
                                    break;

                                case 3:
                                    System.out.println();
                                    System.out.println("Choose a shelf to view:");
                                    System.out.println("1. Produce");
                                    System.out.println("2. Dairy");
                                    System.out.println("3. Snacks");
                                    System.out.println("4. Supplies");
                                    System.out.println("5. Print All Shelves");
                                    System.out.print("Enter choice: ");

                                    int viewChoice;

                                    try {
                                        viewChoice = scanner.nextInt();
                                        scanner.nextLine();
                                    } catch (Exception e) {
                                        System.out.println("Invalid input.");
                                        scanner.nextLine();
                                        break;
                                    }

                                    switch (viewChoice) {
                                        case 1:
                                            produceShelf.printShelf();
                                            break;
                                        case 2:
                                            dairyShelf.printShelf();
                                            break;
                                        case 3:
                                            snacksShelf.printShelf();
                                            break;
                                        case 4:
                                            suppliesShelf.printShelf();
                                            break;
                                        case 5:
                                            System.out.println("\n--- Produce Shelf ---");
                                            produceShelf.printShelf();

                                            System.out.println("\n--- Dairy Shelf ---");
                                            dairyShelf.printShelf();

                                            System.out.println("\n--- Snacks Shelf ---");
                                            snacksShelf.printShelf();

                                            System.out.println("\n--- Supplies Shelf ---");
                                            suppliesShelf.printShelf();
                                            break;
                                        default:
                                            System.out.println("Invalid shelf choice.");
                                            break;
                                    }

                                break;

                                case 4:
                                    System.out.println("Thank you for using the Grocery Store System!");
                                    break;

                                default:
                                    System.out.println("Invalid choice. Please try again.");
                            }

                            if (stockerInput == 4) {
                                break;
                            }
                        }




                    } else if (employeeTitle == 2) { // manager
                        System.out.println("Welcome, Manager! You can manage inventory and view customer history.");

                        Manager manager = new Manager("John", "Smith", 3001);

                        int managerInput = -1;

                        while (managerInput != 7) {
                            System.out.println();
                            System.out.println("--- Manager Menu ---");
                            System.out.println("1. Add Product to Inventory");
                            System.out.println("2. Remove Product from Inventory");
                            System.out.println("3. Change Product Price");
                            System.out.println("4. Restock Inventory Products");
                            System.out.println("5. View Inventory");
                            System.out.println("6. View Customer Info");
                            System.out.println("7. Exit Manager Menu");
                            System.out.print("Enter your choice: ");

                            try {
                                managerInput = scanner.nextInt();
                                scanner.nextLine();
                            } catch (Exception e) {
                                System.out.println("Invalid input.");
                                scanner.nextLine();
                                continue;
                            }

                            switch (managerInput) {
                                case 1: // add new product to inventory
                                    System.out.println();
                                    System.out.println("Choose a section to add an item:");
                                    System.out.println("1. Produce");
                                    System.out.println("2. Dairy");
                                    System.out.println("3. Snacks");
                                    System.out.println("4. Supplies");
                                    System.out.print("Enter choice: ");

                                    int addSectionChoice;
                                    try {
                                        addSectionChoice = scanner.nextInt();
                                        scanner.nextLine();
                                    } catch (Exception e) {
                                        System.out.println("Invalid input. Try again.");
                                        scanner.nextLine();
                                        break;
                                    }

                                    String section = "";

                                    switch (addSectionChoice) {
                                        case 1:
                                            section = "Produce";
                                            break;
                                        case 2:
                                            section = "Dairy";
                                            break;
                                        case 3:
                                            section = "Snacks";
                                            break;
                                        case 4:
                                            section = "Supplies";
                                            break;
                                        default:
                                            System.out.println("Invalid section.");
                                            break;
                                    }

                                    if (section.equals("")) {
                                        break;
                                    }

                                    boolean done = false;

                                    while (!done) {
                                        try {
                                            System.out.println();
                                            System.out.print("Enter product name: ");
                                            String name = scanner.nextLine();

                                            System.out.print("Enter price: ");
                                            double price = scanner.nextDouble();

                                            System.out.print("Enter quantity: ");
                                            int quantity = scanner.nextInt();
                                            scanner.nextLine();

                                            int id = inventory.getNextProductId();// increment for next product

                                            Products newProduct = new Products(name, price, quantity, id);

                                            boolean added = manager.addProduct(inventory, section, newProduct);

                                            if (added) {
                                                System.out.println();
                                                System.out.println("Product added:");
                                                System.out.print("Name: " + name);
                                                System.out.print(" Section: " + section);
                                                System.out.print(" ID: " + id);
                                                System.out.print(" Price: $" + price);
                                                System.out.println(" Quantity: " + quantity);

                                                done = true;
                                            } else {
                                                System.out.println("Please try again.");
                                            }

                                        } catch (Exception e) {
                                            System.out.println("Invalid input or stock exceeds 100. Please try again.");
                                            scanner.nextLine();
                                        }
                                    }
                                break;

                                case 2: // remove product from inv
                                    System.out.println();
                                    System.out.println("Choose a section to remove product from a section:");
                                    System.out.println("1. Produce");
                                    System.out.println("2. Dairy");
                                    System.out.println("3. Snacks");
                                    System.out.println("4. Supplies");
                                    System.out.print("Chosen section: ");

                                    int removeSectionChoice;
                                    try {
                                        removeSectionChoice = scanner.nextInt();
                                        scanner.nextLine();                                        
                                    } catch (Exception e) {
                                        System.out.println("Invalid input. Try again.");
                                        scanner.nextLine();
                                        break;
                                    }

                                    section = "";

                                    switch (removeSectionChoice) {
                                        case 1:
                                            section = "Produce";
                                            break;
                                        case 2:
                                            section = "Dairy";
                                            break;
                                        case 3:
                                            section = "Snacks";
                                            break;
                                        case 4:
                                            section = "Supplies";
                                            break;
                                        default:
                                            System.out.println("Invalid section.");
                                            break;
                                    }
                                    if (section.equals("")) {
                                        break;
                                    }

                                    System.out.println(); // so no stuck loop
                                    boolean hasProducts = inventory.printSectionProducts(section);

                                    if (!hasProducts) {
                                        break;
                                    }
                                    
                                    done = false;

                                    while (!done) {
                                        try {
                                            System.out.println();
                                            System.out.print("Enter product ID (0 to cancel): ");
                                            int productId = scanner.nextInt();
                                            scanner.nextLine();

                                            if (productId == 0) {
                                                System.out.println("Cancelled.");
                                                break;
                                            }

                                            Products product = inventory.getProduct(section, productId);

                                            if (product == null) {
                                                System.out.println("Error: product ID not found in " + section + ". Please input valid ID.");
                                                continue;
                                            }

                                            System.out.print("Enter quantity to remove (0 to cancel): ");
                                            int quantityToRemove = scanner.nextInt();
                                            scanner.nextLine();

                                            if (quantityToRemove == 0) {
                                                System.out.println("Cancelled.");
                                                break;
                                            }

                                            String productName = product.getName();

                                            inventory.decreaseStock(section, productId, quantityToRemove);

                                            System.out.println();
                                            System.out.println("Inventory updated successfully.");
                                            System.out.print("Removed from: " + section);
                                            System.out.print("Product: " + productName);
                                            System.out.print("ID: " + productId);
                                            System.out.print("Amount Removed: " + quantityToRemove);
                                            System.out.println("Remaining Stock: " + product.getQuantity());

                                            done = true; // exit while loop

                                        } catch (NotFoundException | InvalidQuantityException e) {
                                            System.out.println("Error: " + e.getMessage());
                                            System.out.println("Please try again.");
                                        } catch (Exception e) {
                                            System.out.println("Invalid input. Please enter numbers only.");
                                            scanner.nextLine();
                                        }
                                    }
                                    
                                break;

                                case 3: // change product price
                                    System.out.println();
                                    System.out.println("Choose a section to change an item on: ");
                                    System.out.println("1. Produce");
                                    System.out.println("2. Dairy");
                                    System.out.println("3. Snacks");
                                    System.out.println("4. Supplies");
                                    System.out.print("Chosen section: ");

                                    int priceSection;
                                    try {
                                        priceSection = scanner.nextInt();
                                        scanner.nextLine();                                        
                                    } catch (Exception e) {
                                        System.out.println("Invalid input. Try again.");
                                        scanner.nextLine();
                                        break;
                                    }

                                    String priceSectionChoice = "";

                                    switch (priceSection) {
                                        case 1:
                                            priceSectionChoice = "Produce";
                                            break;
                                        case 2:
                                            priceSectionChoice = "Dairy";
                                            break;
                                        case 3:
                                            priceSectionChoice = "Snacks";
                                            break;
                                        case 4:
                                            priceSectionChoice = "Supplies";
                                            break;
                                        default:
                                            System.out.println("Invalid section.");
                                            break;
                                    }
                                    if (priceSectionChoice.equals("")) {
                                        break;
                                    }

                                    System.out.println(); // if no item exit
                                    hasProducts = inventory.printSectionProducts(priceSectionChoice);

                                    if (!hasProducts) {
                                        break;
                                    } 

                                    done = false;

                                    while (!done) {
                                        try {
                                            System.out.println();
                                            System.out.print("Enter product ID (0 to cancel): ");
                                            int productId = scanner.nextInt();
                                            scanner.nextLine();

                                            if (productId == 0) {
                                                System.out.println("Cancelled.");
                                                break;
                                            }

                                            System.out.print("Enter new price: ");
                                            double newPrice = scanner.nextDouble();
                                            scanner.nextLine();

                                            Products product = inventory.getProduct(priceSectionChoice, productId);

                                            if (product == null) {
                                                System.out.println("Error: product ID not found in " + priceSectionChoice + ".");
                                                continue;
                                            }
                                            

                                            String productName = product.getName();

                                            manager.changePrice(inventory, priceSectionChoice, productId, newPrice);

                                            System.out.println();
                                            System.out.println("Price updated successfully.");
                                            System.out.print("Section: " + priceSection);
                                            System.out.print(" Product: " + productName);
                                            System.out.print(" ID: " + productId);
                                            System.out.println(" New Price: $" + product.getPrice());

                                            done = true; // exit loop

                                        } catch (IllegalArgumentException e) {
                                            System.out.println("Error: " + e.getMessage());
                                        } catch (Exception e) {
                                            System.out.println("Invalid input. Please try again.");
                                            scanner.nextLine();
                                        }
                                    }
                                break;

                                case 4: // restock inventory product

                                //add way to see how much each product needs so manager knows how much 
                                    System.out.println();
                                    System.out.println("Choose a section to restock:");
                                    System.out.println("1. Produce");
                                    System.out.println("2. Dairy");
                                    System.out.println("3. Snacks");
                                    System.out.println("4. Supplies");
                                    System.out.print("Chosen section: ");

                                    int restockInventorySectionChoice;
                                    try {
                                        restockInventorySectionChoice = scanner.nextInt();
                                        scanner.nextLine();
                                    } catch (Exception e) {
                                        System.out.println("Invalid input. Try again.");
                                        scanner.nextLine();
                                        break;
                                    }

                                    section = "";

                                    switch (restockInventorySectionChoice) {
                                        case 1:
                                            section = "Produce";
                                            break;
                                        case 2:
                                            section = "Dairy";
                                            break;
                                        case 3:
                                            section = "Snacks";
                                            break;
                                        case 4:
                                            section = "Supplies";
                                            break;
                                        default:
                                            System.out.println("Invalid section.");
                                            break;
                                    }

                                    if (section.equals("")) {
                                        break;
                                    }

                                    System.out.println();
                                    boolean hasProductsToRestock = inventory.printSectionProducts(section);

                                    if (!hasProductsToRestock) {
                                        break;
                                    }

                                    done = false;

                                    while (!done) {
                                        try {
                                            System.out.print("\nEnter product ID (0 to cancel): ");
                                            int productId = scanner.nextInt();
                                            scanner.nextLine();

                                            if (productId == 0) {
                                                System.out.println("Cancelled.");
                                                break;
                                            }

                                            Products product = inventory.getProduct(section, productId);

                                            if (product == null) {
                                                System.out.println("Error: product ID not found in " + section + ".");
                                                continue;
                                            }

                                            System.out.print("Enter quantity to restock (0 to cancel): ");
                                            int quantityToRestock = scanner.nextInt();
                                            scanner.nextLine();

                                            if (quantityToRestock == 0) {
                                                System.out.println("Cancelled.");
                                                break;
                                            }

                                            inventory.restockProduct(section, productId, quantityToRestock);

                                            System.out.println();
                                            System.out.println("Inventory restocked successfully.");
                                            System.out.println();
                                            System.out.println("Section: " + section);
                                            System.out.println("Product: " + product.getName());
                                            System.out.println("ID: " + productId);
                                            System.out.println("Amount Added: " + quantityToRestock);
                                            System.out.println("New Stock: " + product.getQuantity());

                                            done = true;

                                        } catch (NotFoundException | InvalidQuantityException e) {
                                            System.out.println("Error: " + e.getMessage());
                                            System.out.println("Please try again.");
                                        } catch (Exception e) {
                                            System.out.println("Invalid input. Please enter numbers only.");
                                            scanner.nextLine();
                                        }
                                    }
                                break;

                                case 5: // view full inv
                                    System.out.println();
                                    manager.viewInventory(inventory);
                                break;

                                case 6: // view customer info
                                    System.out.println();
                                    System.out.println("Choose a customer to view:");
                                    System.out.println("1. Regular Customer");
                                    System.out.println("2. VIP Customer");
                                    System.out.print("Choose customer: ");

                                    
                                    int customerChoice;
                                    while (true) {
                                            System.out.print("Enter choice: ");

                                            if (!scanner.hasNextInt()) {
                                                System.out.println("Invalid input. Please enter 1 or 2.");
                                                scanner.nextLine();
                                                continue;
                                            }

                                            customerChoice = scanner.nextInt();
                                            scanner.nextLine();

                                            if (customerChoice == 1 || customerChoice == 2) {
                                                break;
                                            } else {
                                                System.out.println("Invalid choice. Please enter 1 or 2.");
                                            }
                                    }

                                    switch (customerChoice) {
                                        case 1:
                                            System.out.println();
                                            System.out.println("--- Regular Customer ---");
                                            customer1.displayCustomerInfo();
                                            customer1.getCart().viewCart();
                                            manager.viewCustomerHistory(customer1);
                                            break;
                                        
                                        case 2:
                                            System.out.println();
                                            System.out.println("--- VIP Customer ---");
                                            customer2.displayCustomerInfo();
                                            customer2.viewVIPBenefits();
                                            customer2.getCart().viewCart();
                                            manager.viewCustomerHistory(customer2);
                                            break;

                                        default:
                                            System.out.println("Invalid input.");
                                            break;
                                    }
                                break;

                                case 7: // exitt
                                    System.out.println("Exiting Manager Menu");
                                break;


                            }
                        }
                    }
                    // if stocker, show add to shelf, which move products to inventory, find products in shelf, view low stock products in shelf, remove protect
                    // if manager, change price, add products to inventory, remove products from inventory, view inventory, view customer history
                    break;

                case 14:
                    System.out.println("Thank you for using the Grocery Store System!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static String readLineInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("EXIT")) {
            gracefulExit(scanner);
        }

        return input;
    }

    private static int readIntInput(Scanner scanner, String prompt) {
        while (true) {
            String input = readLineInput(scanner, prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static void gracefulExit(Scanner scanner) {
        System.out.println("EXIT keyword detected. Closing the Grocery Store System. Goodbye!");
        scanner.close();
        System.exit(0);
    }
}