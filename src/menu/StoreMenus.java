package menu;

import aisles.Aisles;
import checkout.Checkout;
import customers.Customer;
import customers.RegularCustomer;
import customers.VIPCustomer;
import employee.Employee;
import employee.EmployeeMenu;
import input.ConsoleInput;
import inventory.Inventory;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import products.Products;
// Menu templates and information live here so Main stays uncluttered.
public final class StoreMenus {

    private StoreMenus() {
    }

    public static void runRegularCustomerSession(
            Scanner scanner,
            RegularCustomer customer,
            Inventory inventory,
            List<Aisles> aisles) {

        int sub = -1;
        while (sub != 8) {
            System.out.println("\n--- Regular customer ---");
            System.out.println("Signed in as: " + customer.getFullName()
                    + " (ID " + customer.getCustomerId() + ")");
            System.out.println("1. View my customer info");
            System.out.println("2. View my cart");
            System.out.println("3. Add item to my cart");
            System.out.println("4. Remove item from my cart");
            System.out.println("5. Clear my cart");
            System.out.println("6. View aisles (browse / buy)");
            System.out.println("7. Checkout / print receipt");
            System.out.println("8. Sign out (type exit to quit the program)");
            sub = ConsoleInput.readInt(scanner, "Choose an option: ");

            switch (sub) {
                case 1:
                    customer.displayCustomerInfo();
                    break;
                case 2:
                    customer.getCart().viewCart();
                    System.out.println("Total items: " + customer.getCart().getTotalItems());
                    break;
                case 3:
                    customer.getCart().addItem(
                            ConsoleInput.readLine(scanner, "Enter item to add: "));
                    break;
                case 4:
                    customer.getCart().removeItem(
                            ConsoleInput.readLine(scanner, "Enter item to remove: "));
                    break;
                case 5:
                    customer.getCart().clearCart();
                    break;
                case 6:
                    runAislesSession(scanner, aisles, customer);
                    break;
                case 7:
                    Checkout.printReceipt(customer, inventory, aisles);
                    break;
                case 8:
                    System.out.println("Signed out.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public static void runVipCustomerSession(
            Scanner scanner,
            VIPCustomer customer,
            Inventory inventory,
            List<Aisles> aisles) {

        int sub = -1;
        while (sub != 9) {
            System.out.println("\n--- VIP customer ---");
            System.out.println("Signed in as: " + customer.getFullName()
                    + " (ID " + customer.getCustomerId() + ")");
            System.out.println("1. View my customer info");
            System.out.println("2. View my cart");
            System.out.println("3. Add item to my cart");
            System.out.println("4. Remove item from my cart");
            System.out.println("5. Clear my cart");
            System.out.println("6. View VIP benefits");
            System.out.println("7. View aisles (browse / buy)");
            System.out.println("8. Checkout / print receipt");
            System.out.println("9. Sign out (type exit to quit the program)");
            sub = ConsoleInput.readInt(scanner, "Choose an option: ");

            switch (sub) {
                case 1:
                    customer.displayCustomerInfo();
                    break;
                case 2:
                    customer.getCart().viewCart();
                    System.out.println("Total items: " + customer.getCart().getTotalItems());
                    break;
                case 3:
                    customer.getCart().addItem(
                            ConsoleInput.readLine(scanner, "Enter item to add: "));
                    break;
                case 4:
                    customer.getCart().removeItem(
                            ConsoleInput.readLine(scanner, "Enter item to remove: "));
                    break;
                case 5:
                    customer.getCart().clearCart();
                    break;
                case 6:
                    customer.viewVIPBenefits();
                    break;
                case 7:
                    runAislesSession(scanner, aisles, customer);
                    break;
                case 8:
                    Checkout.printReceipt(customer, inventory, aisles);
                    break;
                case 9:
                    System.out.println("Signed out.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void runAislesSession(
            Scanner scanner,
            List<Aisles> aisles,
            Customer activeCustomer) {

        boolean viewingAisles = true;

        while (viewingAisles) {
            System.out.println("\nAvailable aisles:");
            for (Aisles aisle : aisles) {
                System.out.println("- Aisle " + aisle.getAisleNumber()
                        + " (" + aisle.getAisleType() + ")");
            }
            System.out.println("0. Back");

            try {
                int selectedAisleNumber =
                        ConsoleInput.readInt(scanner, "Enter aisle number to view: ");

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
                    int aisleAction = ConsoleInput.readInt(scanner, "Choose an option: ");

                    if (aisleAction == 2) {
                        viewingSingleAisle = false;
                        continue;
                    }

                    if (aisleAction != 1) {
                        System.out.println("Invalid choice.");
                        continue;
                    }

                    int productIdToBuy = ConsoleInput.readInt(scanner, "Enter product ID to buy: ");
                    int quantityToBuy = ConsoleInput.readInt(scanner, "Enter quantity to buy: ");

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
                        System.out.println("Not enough stock. Available: "
                                + selectedProduct.getQuantity());
                        continue;
                    }

                    selectedProduct.setQuantity(
                            selectedProduct.getQuantity() - quantityToBuy);

                    for (int i = 0; i < quantityToBuy; i++) {
                        activeCustomer.getCart().addItem(selectedProduct.getName());
                    }

                    System.out.println("Added " + quantityToBuy + " x "
                            + selectedProduct.getName() + " to "
                            + activeCustomer.getFullName() + "'s cart.");
                }

            } catch (Exception e) {
                System.out.println("Invalid input.");
            }
        }
    }

    public static void runEmployeeSession(
            Scanner scanner,
            Inventory inventory,
            List<Aisles> aisles,
            Map<Integer, RegularCustomer> regularCustomers,
            Map<Integer, VIPCustomer> vipCustomers,
            Employee signedInEmployee) {

        EmployeeMenu.run(
                scanner,
                inventory,
                regularCustomers,
                vipCustomers,
                aisles,
                signedInEmployee
        );
    }
}