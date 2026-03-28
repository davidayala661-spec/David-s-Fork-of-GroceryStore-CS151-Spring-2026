package menu;

import aisles.Aisles;
import checkout.Checkout;
import customers.Customer;
import customers.RegularCustomer;
import customers.VIPCustomer;
import employee.EmployeeMenu;
import input.ConsoleInput;
import inventory.Inventory;
import java.util.List;
import java.util.Scanner;
import products.Products;
import shelf.Shelf;

public final class StoreMenus {

    private StoreMenus() {
    }

    public static void runRegularCustomerSession(Scanner scanner, RegularCustomer customer,
            Inventory inventory, List<Aisles> aisles) {
        int sub = -1;
        while (sub != 8) {
            System.out.println("\n--- Regular customer ---");
            System.out.println("1. View my customer info");
            System.out.println("2. View my cart");
            System.out.println("3. Add item to my cart");
            System.out.println("4. Remove item from my cart");
            System.out.println("5. Clear my cart");
            System.out.println("6. View aisles (browse / buy)");
            System.out.println("7. Checkout / print receipt");
            System.out.println("8. Back to main menu");
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
                    customer.getCart().addItem(ConsoleInput.readLine(scanner, "Enter item to add: "));
                    break;
                case 4:
                    customer.getCart().removeItem(ConsoleInput.readLine(scanner, "Enter item to remove: "));
                    break;
                case 5:
                    customer.getCart().clearCart();
                    break;
                case 6:
                    runAislesSession(scanner, aisles, customer, null);
                    break;
                case 7:
                    Checkout.printReceipt(customer, inventory, aisles);
                    break;
                case 8:
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public static void runVipCustomerSession(Scanner scanner, VIPCustomer customer,
            Inventory inventory, List<Aisles> aisles, RegularCustomer otherCustomer) {
        int sub = -1;
        while (sub != 9) {
            System.out.println("\n--- VIP customer ---");
            System.out.println("1. View my customer info");
            System.out.println("2. View my cart");
            System.out.println("3. Add item to my cart");
            System.out.println("4. Remove item from my cart");
            System.out.println("5. Clear my cart");
            System.out.println("6. View VIP benefits");
            System.out.println("7. View aisles (browse / buy)");
            System.out.println("8. Checkout / print receipt");
            System.out.println("9. Back to main menu");
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
                    customer.getCart().addItem(ConsoleInput.readLine(scanner, "Enter item to add: "));
                    break;
                case 4:
                    customer.getCart().removeItem(ConsoleInput.readLine(scanner, "Enter item to remove: "));
                    break;
                case 5:
                    customer.getCart().clearCart();
                    break;
                case 6:
                    customer.viewVIPBenefits();
                    break;
                case 7:
                    runAislesSession(scanner, aisles, otherCustomer, customer);
                    break;
                case 8:
                    Checkout.printReceipt(customer, inventory, aisles);
                    break;
                case 9:
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * @param primaryCart  regular customer (used for "1" when picking cart at checkout from aisle)
     * @param secondaryCart VIP customer (used for "2"), or null when session is regular-only
     */
    private static void runAislesSession(Scanner scanner, List<Aisles> aisles,
            RegularCustomer primaryCart, VIPCustomer secondaryCart) {
        boolean viewingAisles = true;
        while (viewingAisles) {
            System.out.println("\nAvailable aisles:");
            for (Aisles aisle : aisles) {
                System.out.println("- Aisle " + aisle.getAisleNumber() + " (" + aisle.getAisleType() + ")");
            }
            System.out.println("0. Back");

            try {
                int selectedAisleNumber = ConsoleInput.readInt(scanner, "Enter aisle number to view: ");

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

                    Customer cartCustomer;
                    if (secondaryCart == null) {
                        cartCustomer = primaryCart;
                    } else {
                        System.out.println("1. Regular customer's cart (ID " + primaryCart.getCustomerId() + ")");
                        System.out.println("2. VIP customer's cart (ID " + secondaryCart.getCustomerId() + ")");
                        int cartOwner = ConsoleInput.readInt(scanner, "Whose cart should this go to? ");
                        if (cartOwner == 1) {
                            cartCustomer = primaryCart;
                        } else if (cartOwner == 2) {
                            cartCustomer = secondaryCart;
                        } else {
                            System.out.println("Invalid choice.");
                            continue;
                        }
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
                        System.out.println("Not enough stock. Available: " + selectedProduct.getQuantity());
                        continue;
                    }

                    selectedProduct.setQuantity(selectedProduct.getQuantity() - quantityToBuy);
                    for (int i = 0; i < quantityToBuy; i++) {
                        cartCustomer.getCart().addItem(selectedProduct.getName());
                    }

                    System.out.println("Added " + quantityToBuy + " x " + selectedProduct.getName()
                            + " to customer " + cartCustomer.getCustomerId() + "'s cart.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input.");
            }
        }
    }

    public static void runEmployeeSession(Scanner scanner, Inventory inventory, List<Aisles> aisles,
            RegularCustomer customer1, VIPCustomer customer2,
            Shelf produceShelf, Shelf dairyShelf, Shelf snacksShelf, Shelf suppliesShelf) {
        EmployeeMenu.run(scanner, inventory, customer1, customer2,
                produceShelf, dairyShelf, snacksShelf, suppliesShelf, aisles);
    }
}
