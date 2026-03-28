package employee;

import aisles.Aisles;
import customers.RegularCustomer;
import customers.VIPCustomer;
import exceptions.InvalidQuantityException;
import exceptions.NotFoundException;
import input.ConsoleInput;
import inventory.Inventory;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import products.Products;
import shelf.Shelf;

public final class EmployeeMenu {

    private EmployeeMenu() {
    }

    public static void run(Scanner scanner, Inventory inventory,
            RegularCustomer customer1, VIPCustomer customer2,
            Shelf produceShelf, Shelf dairyShelf, Shelf snacksShelf, Shelf suppliesShelf,
            List<Aisles> aisles) {
                    System.out.println("=============================");
                    System.out.println("|     Employee Menu      |");
                    System.out.println("=============================");
                    System.out.println("1. Stocker");
                    System.out.println("2. Manager");
                    System.out.println("3. Exit (back to Grocery Store)");

                    int employeeTitle = -1;

                    while (true) {
                        employeeTitle = ConsoleInput.readInt(scanner, "Enter your title: ");
                        if (employeeTitle == 1 || employeeTitle == 2) {
                            break;
                        }
                        if (employeeTitle == 3) {
                            System.out.println("Returning to Grocery Store menu.");
                            return;
                        }
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                    }

                    if (employeeTitle == 1) { // stocker
                        System.out.println();
                        System.out.println("Welcome, Stocker! You can view low stock products and restock shelves.");

                        Stocker stocker = new Stocker("Doe", "John", 2001);

                        int stockerInput = -1;

                        int lowStockThreshold = 10; // default threshold

                        while (stockerInput != 7) { // loops and keeps asking till wants to leave stocker menu
                            System.out.println();
                            System.out.println("--- Stocker Menu ---");
                            System.out.println("1. View Low Stock Products");
                            System.out.println("2. Restock Product");
                            System.out.println("3. View Shelf");
                            System.out.println("4. Decrease product stock (inventory)");
                            System.out.println("5. View low stock in aisles");
                            System.out.println("6. View inventory");
                            System.out.println("7. Exit Stocker Menu");
                            stockerInput = ConsoleInput.readInt(scanner, "Enter your choice: ");

                            switch (stockerInput) {
                                case 1:
                                    System.out.println("1. Produce");
                                    System.out.println("2. Dairy");
                                    System.out.println("3. Snacks");
                                    System.out.println("4. Supplies");
                                    int sectionChoice = ConsoleInput.readInt(scanner, "Enter section to check:");

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
                                        lowStockThreshold = ConsoleInput.readInt(scanner, "Enter low-stock threshold: ");

                                        System.out.println();
                                        stocker.viewLowShelfStock(selectedShelf, lowStockThreshold);

                                        for (Products product : selectedShelf.getProducts()) { // shows how much is needed
                                            if (product.getQuantity() < lowStockThreshold) {
                                                System.out.println(product.getName() + " needs "
                                                        + (lowStockThreshold - product.getQuantity())
                                                        + " more items.");
                                            }
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
                                    int restockSectionChoice = ConsoleInput.readInt(scanner, "Chosen shelf: ");

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

                                    int productChoice = ConsoleInput.readInt(scanner, "Choose product to restock: ");

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

                                    int restockAmount = ConsoleInput.readInt(scanner, "Enter quantity to restock: ");

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
                                    int viewChoice = ConsoleInput.readInt(scanner, "Enter choice: ");

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
                                    try {
                                        String invSection = ConsoleInput.readLine(scanner, "Enter section: ");
                                        int decProductId = ConsoleInput.readInt(scanner, "Enter product ID: ");
                                        int decQty = ConsoleInput.readInt(scanner, "Enter quantity to decrease: ");
                                        inventory.decreaseStock(invSection, decProductId, decQty);
                                        System.out.println("Product stock decreased successfully.");
                                    } catch (NotFoundException | InvalidQuantityException e) {
                                        System.out.println("Decrease stock error: " + e.getMessage());
                                    }
                                    break;

                                case 5:
                                    int aisleThreshold = ConsoleInput.readInt(scanner, "Enter low-stock threshold: ");
                                    printLowStockAisles(aisles, aisleThreshold);
                                    break;

                                case 6:
                                    inventory.printInventory();
                                    break;

                                case 7:
                                    System.out.println("Exiting Stocker Menu");
                                    break;

                                default:
                                    System.out.println("Invalid choice. Please try again.");
                            }

                            if (stockerInput == 7) {
                                break;
                            }
                        }




                    } else if (employeeTitle == 2) { // manager
                        System.out.println("Welcome, Manager! You can manage inventory and view customer history.");

                        Manager manager = new Manager("John", "Smith", 3001);

                        int managerInput = -1;

                        while (managerInput != 8) {
                            System.out.println();
                            System.out.println("--- Manager Menu ---");
                            System.out.println("1. Add Product to Inventory");
                            System.out.println("2. Remove Product from Inventory");
                            System.out.println("3. Change Product Price");
                            System.out.println("4. Restock Inventory Products");
                            System.out.println("5. View Inventory");
                            System.out.println("6. View Customer Info");
                            System.out.println("7. Find product by ID");
                            System.out.println("8. Exit Manager Menu");
                            managerInput = ConsoleInput.readInt(scanner, "Enter your choice: ");

                            switch (managerInput) {
                                case 1: // add new product to inventory
                                    System.out.println();
                                    System.out.println("Choose a section to add an item:");
                                    System.out.println("1. Produce");
                                    System.out.println("2. Dairy");
                                    System.out.println("3. Snacks");
                                    System.out.println("4. Supplies");
                                    int addSectionChoice = ConsoleInput.readInt(scanner, "Enter choice: ");

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
                                            String name = ConsoleInput.readLine(scanner, "Enter product name: ");

                                            double price = ConsoleInput.readDouble(scanner, "Enter price: ");

                                            int quantity = ConsoleInput.readInt(scanner, "Enter quantity: ");

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
                                    int removeSectionChoice = ConsoleInput.readInt(scanner, "Chosen section: ");

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
                                            int productId = ConsoleInput.readInt(scanner, "Enter product ID (0 to cancel): ");

                                            if (productId == 0) {
                                                System.out.println("Cancelled.");
                                                break;
                                            }

                                            Products product = inventory.getProduct(section, productId);

                                            if (product == null) {
                                                System.out.println("Error: product ID not found in " + section + ". Please input valid ID.");
                                                continue;
                                            }

                                            int quantityToRemove = ConsoleInput.readInt(scanner, "Enter quantity to remove (0 to cancel): ");

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
                                    int priceSection = ConsoleInput.readInt(scanner, "Chosen section: ");

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
                                            int productId = ConsoleInput.readInt(scanner, "Enter product ID (0 to cancel): ");

                                            if (productId == 0) {
                                                System.out.println("Cancelled.");
                                                break;
                                            }

                                            double newPrice = ConsoleInput.readDouble(scanner, "Enter new price: ");

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
                                    int restockInventorySectionChoice = ConsoleInput.readInt(scanner, "Chosen section: ");

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
                                            int productId = ConsoleInput.readInt(scanner, "\nEnter product ID (0 to cancel): ");

                                            if (productId == 0) {
                                                System.out.println("Cancelled.");
                                                break;
                                            }

                                            Products product = inventory.getProduct(section, productId);

                                            if (product == null) {
                                                System.out.println("Error: product ID not found in " + section + ".");
                                                continue;
                                            }

                                            int quantityToRestock = ConsoleInput.readInt(scanner, "Enter quantity to restock (0 to cancel): ");

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
                                    int customerChoice;
                                    while (true) {
                                        customerChoice = ConsoleInput.readInt(scanner, "Enter choice: ");
                                        if (customerChoice == 1 || customerChoice == 2) {
                                            break;
                                        }
                                        System.out.println("Invalid choice. Please enter 1 or 2.");
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

                                case 7:
                                    try {
                                        int lookupId = ConsoleInput.readInt(scanner, "Enter product ID to find: ");
                                        Products foundProduct = inventory.findProduct(lookupId);
                                        System.out.println("Found: " + foundProduct);
                                    } catch (NotFoundException e) {
                                        System.out.println("Search error: " + e.getMessage());
                                    }
                                    break;

                                case 8:
                                    System.out.println("Exiting Manager Menu");
                                    break;

                                default:
                                    System.out.println("Invalid choice. Please try again.");
                            }
                        }
                    }
                    // if stocker, show add to shelf, which move products to inventory, find products in shelf, view low stock products in shelf, remove protect
                    // if manager, change price, add products to inventory, remove products from inventory, view inventory, view customer history
    }

    private static void printLowStockAisles(List<Aisles> aisles, int threshold) {
        boolean any = false;
        for (Aisles aisle : aisles) {
            boolean aisleHeaderPrinted = false;
            for (Map.Entry<Integer, List<Products>> entry : aisle.getShelves().entrySet()) {
                int shelfNum = entry.getKey();
                for (Products product : entry.getValue()) {
                    if (product.getQuantity() < threshold) {
                        if (!aisleHeaderPrinted) {
                            System.out.println();
                            System.out.println("Aisle " + aisle.getAisleNumber() + " (" + aisle.getAisleType() + ")");
                            aisleHeaderPrinted = true;
                            any = true;
                        }
                        System.out.println("  Shelf " + shelfNum + ": " + product.getName()
                                + " (ID " + product.getID() + ", stock " + product.getQuantity() + ")");
                    }
                }
            }
        }
        if (!any) {
            System.out.println("No products below threshold " + threshold + " in any aisle.");
        }
    }
}
