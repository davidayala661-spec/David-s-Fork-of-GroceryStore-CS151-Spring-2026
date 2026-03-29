package employee;

import aisles.Aisles;
import customers.Customer;
import data.StoreDataLoader;
import customers.RegularCustomer;
import customers.VIPCustomer;
import exceptions.InvalidQuantityException;
import exceptions.NotFoundException;
import input.ConsoleInput;
import inventory.Inventory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import products.Products;

public final class EmployeeMenu {

    private static final int MANAGER_SHELF_RESTOCK_CAP = 10;

    private EmployeeMenu() {
    }

    public static void run(
            Scanner scanner,
            Inventory inventory,
            Map<Integer, RegularCustomer> regularCustomers,
            Map<Integer, VIPCustomer> vipCustomers,
            List<Aisles> aisles,
            Employee signedInEmployee) {

        System.out.println();
        System.out.println("Signed in as employee: " + signedInEmployee.getFullName()
                + " (ID " + signedInEmployee.getEmployeeID()
                + ", " + signedInEmployee.getDepartment() + ")");

        if (signedInEmployee instanceof Stocker stocker) {
            runStockerMenu(scanner, inventory, aisles, stocker);
        } else if (signedInEmployee instanceof Manager manager) {
            runManagerMenu(scanner, inventory, regularCustomers, vipCustomers, aisles,
                    manager);
        } else {
            System.out.println("Unknown employee type.");
        }
    }

    private static void runStockerMenu(
            Scanner scanner,
            Inventory inventory,
            List<Aisles> aisles,
            Stocker stocker) {

        System.out.println("Welcome, Stocker! You can view low stock products and restock shelves.");

        int stockerInput = -1;
        int lowStockThreshold = StoreDataLoader.getMaxShelfQuantityPerProduct();

        while (stockerInput != 7) {
            System.out.println();
            System.out.println("--- Stocker Menu ---");
            System.out.println("1. View Low Stock Products");
            System.out.println("2. Restock Product");
            System.out.println("3. View Shelf");
            System.out.println("4. Decrease product stock (inventory)");
            System.out.println("5. View low stock in aisles");
            System.out.println("6. View inventory");
            System.out.println("7. Sign out (type exit to quit the program)");
            stockerInput = ConsoleInput.readInt(scanner, "Enter your choice: ");

            switch (stockerInput) {
                case 1:
                    AisleShelfPick pickLow = pickAisleShelf(scanner, aisles);
                    if (pickLow != null) {
                        lowStockThreshold = ConsoleInput.readInt(
                                scanner, "Enter low-stock threshold: ");

                        System.out.println();
                        stocker.viewLowAisleShelfStock(
                                pickLow.aisle(), pickLow.shelfNumber(), lowStockThreshold);

                        for (Products product : pickLow.aisle().getProductsOnShelf(pickLow.shelfNumber())) {
                            if (product.getQuantity() < lowStockThreshold) {
                                System.out.println(product.getName() + " needs "
                                        + (lowStockThreshold - product.getQuantity())
                                        + " more items.");
                            }
                        }
                    }
                    break;

                case 2:
                    runAisleShelfRestockFromBackRoom(scanner, inventory, aisles, lowStockThreshold);
                    break;

                case 3:
                    System.out.println();
                    System.out.println("1. View one aisle shelf");
                    System.out.println("2. View all aisles");
                    int viewMode = ConsoleInput.readInt(scanner, "Enter choice: ");
                    if (viewMode == 1) {
                        AisleShelfPick pickView = pickAisleShelf(scanner, aisles);
                        if (pickView != null) {
                            printAisleShelfProducts(pickView.aisle(), pickView.shelfNumber());
                        }
                    } else if (viewMode == 2) {
                        for (Aisles aisle : aisles) {
                            aisle.printAisle();
                            System.out.println();
                        }
                    } else {
                        System.out.println("Invalid choice.");
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
                    int aisleThreshold = ConsoleInput.readInt(
                            scanner, "Enter low-stock threshold: ");
                    printLowStockAisles(aisles, aisleThreshold);
                    break;

                case 6:
                    inventory.printInventory();
                    break;

                case 7:
                    System.out.println("Signing out of Stocker Menu.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void runManagerMenu(
            Scanner scanner,
            Inventory inventory,
            Map<Integer, RegularCustomer> regularCustomers,
            Map<Integer, VIPCustomer> vipCustomers,
            List<Aisles> aisles,
            Manager manager) {

        System.out.println("Welcome, Manager! You can manage inventory and view customer history.");

        int managerInput = -1;

        while (managerInput != 8) {
            System.out.println();
            System.out.println("--- Manager Menu ---");
            System.out.println("1. Add Product to Inventory");
            System.out.println("2. Remove Product from Inventory");
            System.out.println("3. Change Product Price");
            System.out.println("4. Restock shelf from back-room inventory");
            System.out.println("5. View Inventory");
            System.out.println("6. View Customer Info");
            System.out.println("7. Find product by ID");
            System.out.println("8. Sign out (type exit to quit the program)");
            managerInput = ConsoleInput.readInt(scanner, "Enter your choice: ");

            switch (managerInput) {
                case 1:
                    handleAddProduct(scanner, inventory, manager);
                    break;

                case 2:
                    handleRemoveProduct(scanner, inventory);
                    break;

                case 3:
                    handleChangePrice(scanner, inventory, manager);
                    break;

                case 4:
                    handleRestockInventory(scanner, inventory, aisles);
                    break;

                case 5:
                    System.out.println();
                    manager.viewInventory(inventory);
                    break;

                case 6:
                    viewCustomerInfo(scanner, manager, regularCustomers, vipCustomers);
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
                    System.out.println("Signing out of Manager Menu.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleAddProduct(
            Scanner scanner,
            Inventory inventory,
            Manager manager) {

        System.out.println();
        String section = chooseInventorySection(scanner);
        if (section.isEmpty()) {
            return;
        }
        if (!StoreDataLoader.isAisleInventorySection(section)) {
            System.out.println("Managers can only work with Dairy, Fruits, or Meats (store aisles).");
            return;
        }

        boolean done = false;
        while (!done) {
            try {
                System.out.println();
                String name = ConsoleInput.readLine(scanner, "Enter product name: ");
                double price = ConsoleInput.readDouble(scanner, "Enter price: ");
                int quantity = ConsoleInput.readInt(scanner, "Enter quantity: ");
                int id = inventory.getNextProductId();

                Products newProduct = new Products(name, price, quantity, id);
                boolean added = manager.addProduct(inventory, section, newProduct);

                if (added) {
                    System.out.println();
                    System.out.println("Product added:");
                    System.out.println("Name: " + name);
                    System.out.println("Section: " + section);
                    System.out.println("ID: " + id);
                    System.out.println("Price: $" + price);
                    System.out.println("Quantity: " + quantity);
                    done = true;
                } else {
                    System.out.println("Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input or stock exceeds 100. Please try again.");
            }
        }
    }

    private static void handleRemoveProduct(
            Scanner scanner,
            Inventory inventory) {

        System.out.println();
        String section = chooseInventorySection(scanner);
        if (section.isEmpty()) {
            return;
        }
        if (!StoreDataLoader.isAisleInventorySection(section)) {
            System.out.println("Managers can only work with Dairy, Fruits, or Meats (store aisles).");
            return;
        }

        System.out.println();
        boolean hasProducts = inventory.printSectionProducts(section);
        if (!hasProducts) {
            return;
        }

        boolean done = false;
        while (!done) {
            try {
                System.out.println();
                int productId = ConsoleInput.readInt(
                        scanner, "Enter product ID (0 to cancel): ");

                if (productId == 0) {
                    System.out.println("Cancelled.");
                    break;
                }

                Products product = inventory.getProduct(section, productId);
                if (product == null) {
                    System.out.println("Error: product ID not found in " + section + ".");
                    continue;
                }

                int quantityToRemove = ConsoleInput.readInt(
                        scanner, "Enter quantity to remove (0 to cancel): ");

                if (quantityToRemove == 0) {
                    System.out.println("Cancelled.");
                    break;
                }

                String productName = product.getName();
                inventory.decreaseStock(section, productId, quantityToRemove);

                System.out.println();
                System.out.println("Inventory updated successfully.");
                System.out.println("Removed from: " + section);
                System.out.println("Product: " + productName);
                System.out.println("ID: " + productId);
                System.out.println("Amount Removed: " + quantityToRemove);
                System.out.println("Remaining Stock: " + product.getQuantity());

                done = true;
            } catch (NotFoundException | InvalidQuantityException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Please try again.");
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter numbers only.");
            }
        }
    }

    private static void handleChangePrice(
            Scanner scanner,
            Inventory inventory,
            Manager manager) {

        System.out.println();
        String section = chooseInventorySection(scanner);
        if (section.isEmpty()) {
            return;
        }
        if (!StoreDataLoader.isAisleInventorySection(section)) {
            System.out.println("Managers can only work with Dairy, Fruits, or Meats (store aisles).");
            return;
        }

        System.out.println();
        boolean hasProducts = inventory.printSectionProducts(section);
        if (!hasProducts) {
            return;
        }

        boolean done = false;
        while (!done) {
            try {
                System.out.println();
                int productId = ConsoleInput.readInt(
                        scanner, "Enter product ID (0 to cancel): ");

                if (productId == 0) {
                    System.out.println("Cancelled.");
                    break;
                }

                double newPrice = ConsoleInput.readDouble(scanner, "Enter new price: ");

                Products product = inventory.getProduct(section, productId);
                if (product == null) {
                    System.out.println("Error: product ID not found in " + section + ".");
                    continue;
                }

                String productName = product.getName();
                manager.changePrice(inventory, section, productId, newPrice);

                System.out.println();
                System.out.println("Price updated successfully.");
                System.out.println("Section: " + section);
                System.out.println("Product: " + productName);
                System.out.println("ID: " + productId);
                System.out.println("New Price: $" + product.getPrice());

                done = true;
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private static void handleRestockInventory(
            Scanner scanner,
            Inventory inventory,
            List<Aisles> aisles) {
        System.out.println("Same aisles and shelves as the store floor (loaded by StoreDataLoader).");
        System.out.println();
        runAisleShelfRestockFromBackRoom(scanner, inventory, aisles,
                StoreDataLoader.getMaxShelfQuantityPerProduct());
    }

    private static void runAisleShelfRestockFromBackRoom(
            Scanner scanner,
            Inventory inventory,
            List<Aisles> aisles,
            int shelfMaxPerItem) {

        AisleShelfPick pickRestock = pickAisleShelf(scanner, aisles);
        if (pickRestock == null) {
            return;
        }

        String aisleSection = pickRestock.aisle().getAisleType();

        List<Products> shelfProducts = new ArrayList<>(
                pickRestock.aisle().getProductsOnShelf(pickRestock.shelfNumber()));

        if (shelfProducts.isEmpty()) {
            System.out.println("This shelf is empty.");
            return;
        }

        System.out.println();
        System.out.println("Move stock from back-room inventory onto this shelf.");
        System.out.println("Shelf max per item: " + shelfMaxPerItem + ".");
        System.out.println("Products on Aisle " + pickRestock.aisle().getAisleNumber()
                + " (" + aisleSection + "), shelf "
                + pickRestock.shelfNumber() + ":");

        int itemNumber = 1;
        for (Products product : shelfProducts) {
            Products backRoom = inventory.getProduct(aisleSection, product.getID());
            int backQty = backRoom != null ? backRoom.getQuantity() : 0;
            System.out.println(itemNumber + ". " + product.getName()
                    + " (ID: " + product.getID()
                    + ", on shelf: " + product.getQuantity()
                    + ", back room: " + backQty + ")");
            itemNumber++;
        }

        int productChoice = ConsoleInput.readInt(
                scanner, "Choose product to restock: ");

        Products chosenProduct = null;
        if (productChoice >= 1 && productChoice <= shelfProducts.size()) {
            chosenProduct = shelfProducts.get(productChoice - 1);
        }

        if (chosenProduct == null) {
            System.out.println("Invalid product choice.");
            return;
        }

        Products backRoomProduct = inventory.getProduct(aisleSection, chosenProduct.getID());
        if (backRoomProduct == null) {
            System.out.println("No back-room inventory for this product ID in section "
                    + aisleSection + ".");
            return;
        }

        int restockAmount = ConsoleInput.readInt(
                scanner, "Enter quantity to move from back room to shelf: ");

        if (restockAmount <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return;
        }

        if (backRoomProduct.getQuantity() < restockAmount) {
            System.out.println("Not enough back-room stock. Available: "
                    + backRoomProduct.getQuantity());
            return;
        }

        int newShelfQty = chosenProduct.getQuantity() + restockAmount;
        if (newShelfQty > shelfMaxPerItem) {
            System.out.println("Error: shelf would exceed max of " + shelfMaxPerItem + ".");
            System.out.println(chosenProduct.getName() + " would have " + newShelfQty
                    + " on the shelf.");
            return;
        }

        try {
            inventory.decreaseStock(aisleSection, chosenProduct.getID(), restockAmount);
            chosenProduct.stockToShelf(restockAmount);
            System.out.println("Restocked successfully (moved from inventory to shelf).");
            System.out.println(chosenProduct.getName() + " — shelf: "
                    + chosenProduct.getQuantity() + ", back room: "
                    + inventory.getProduct(aisleSection, chosenProduct.getID()).getQuantity());
        } catch (NotFoundException | InvalidQuantityException e) {
            System.out.println("Restock error: " + e.getMessage());
        }
    }

    private static void viewCustomerInfo(
            Scanner scanner,
            Manager manager,
            Map<Integer, RegularCustomer> regularCustomers,
            Map<Integer, VIPCustomer> vipCustomers) {

        System.out.println();
        System.out.println("Choose a customer type:");
        System.out.println("1. Regular Customer");
        System.out.println("2. VIP Customer");
        int customerChoice = ConsoleInput.readInt(scanner, "Enter choice: ");

        int customerId = ConsoleInput.readInt(scanner, "Enter customer ID: ");

        Customer selectedCustomer = null;

        switch (customerChoice) {
            case 1:
                selectedCustomer = regularCustomers.get(customerId);
                break;
            case 2:
                selectedCustomer = vipCustomers.get(customerId);
                break;
            default:
                System.out.println("Invalid customer type.");
                return;
        }

        if (selectedCustomer == null) {
            System.out.println("No customer found with ID " + customerId + ".");
            return;
        }

        System.out.println();
        selectedCustomer.displayCustomerInfo();
        selectedCustomer.getCart().viewCart();
        manager.viewCustomerHistory(selectedCustomer);
    }

    private record AisleShelfPick(Aisles aisle, int shelfNumber) {
    }

    private static AisleShelfPick pickAisleShelf(Scanner scanner, List<Aisles> aisles) {
        System.out.println();
        System.out.println("Aisles:");
        for (Aisles a : aisles) {
            System.out.println("  " + a.getAisleNumber() + ". Aisle " + a.getAisleNumber()
                    + " (" + a.getAisleType() + ")");
        }
        int aisleNum = ConsoleInput.readInt(scanner, "Enter aisle number: ");
        Aisles selected = null;
        for (Aisles a : aisles) {
            if (a.getAisleNumber() == aisleNum) {
                selected = a;
                break;
            }
        }
        if (selected == null) {
            System.out.println("Aisle not found.");
            return null;
        }
        List<Integer> shelfNums = new ArrayList<>(selected.getShelves().keySet());
        Collections.sort(shelfNums);
        if (shelfNums.isEmpty()) {
            System.out.println("That aisle has no shelves.");
            return null;
        }
        System.out.println("Shelf numbers in this aisle: " + shelfNums);
        int shelfNum = ConsoleInput.readInt(scanner, "Enter shelf number: ");
        if (!selected.getShelves().containsKey(shelfNum)) {
            System.out.println("Shelf not found.");
            return null;
        }
        return new AisleShelfPick(selected, shelfNum);
    }

    private static void printAisleShelfProducts(Aisles aisle, int shelfNumber) {
        System.out.println();
        System.out.println("Aisle " + aisle.getAisleNumber() + " (" + aisle.getAisleType()
                + "), shelf " + shelfNumber + ":");
        List<Products> products = aisle.getProductsOnShelf(shelfNumber);
        if (products.isEmpty()) {
            System.out.println("This shelf is empty.");
            return;
        }
        for (Products p : products) {
            System.out.println("- " + p.getName() + " (ID: " + p.getID() + ", Price: $"
                    + p.getPrice() + ", Stock: " + p.getQuantity() + ")");
        }
    }

    private static String chooseInventorySection(Scanner scanner) {
        String[] sections = StoreDataLoader.AISLE_SECTION_NAMES;
        System.out.println("Inventory section (Dairy, Fruits, Meats — matches store aisles):");
        for (int i = 0; i < sections.length; i++) {
            System.out.println((i + 1) + ". " + sections[i]);
        }
        int choice = ConsoleInput.readInt(scanner,
                "Enter choice (1-" + sections.length + "): ");

        if (choice >= 1 && choice <= sections.length) {
            return sections[choice - 1];
        }
        System.out.println("Invalid section.");
        return "";
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
                            System.out.println("Aisle " + aisle.getAisleNumber()
                                    + " (" + aisle.getAisleType() + ")");
                            aisleHeaderPrinted = true;
                            any = true;
                        }

                        System.out.println("  Shelf " + shelfNum + ": "
                                + product.getName()
                                + " (ID " + product.getID()
                                + ", stock " + product.getQuantity() + ")");
                    }
                }
            }
        }

        if (!any) {
            System.out.println("No products below threshold "
                    + threshold + " in any aisle.");
        }
    }
}