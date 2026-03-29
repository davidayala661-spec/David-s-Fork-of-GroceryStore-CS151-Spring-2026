package employee;

import customers.Customer;
import data.StoreDataLoader;
import inventory.Inventory;
import products.Products;
import exceptions.CapacityExceededException;
import exceptions.DuplicateProductException;
import exceptions.InvalidPriceException;
import exceptions.InvalidProductException;
import exceptions.InvalidSectionException;
import exceptions.NotFoundException;

public class Manager extends Employee {

    public Manager(String firstName, String lastName, int employeeID) {
        super(firstName, lastName, employeeID, "Manager", 0.10);
        // Managers get 10% discount
    }

    // Managers can add quantity to the inventory
    public boolean addProduct(Inventory inventory, String section, Products product) {
        if (!StoreDataLoader.isAisleInventorySection(section)) {
            System.out.println("Managers can only add products to Dairy, Fruits, or Meats (store aisles).");
            return false;
        }
        try {
            inventory.addProduct(section, product);
            return true;
        } catch (CapacityExceededException | InvalidSectionException
                | InvalidProductException | DuplicateProductException e) {
            System.out.println("Add product error: " + e.getMessage());
            return false;
        }
    }

    // Managers can remove quantity from the inventory
    public void removeProduct(Inventory inventory, String section, int productID) {
        if (!StoreDataLoader.isAisleInventorySection(section)) {
            System.out.println("Managers can only remove products from Dairy, Fruits, or Meats (store aisles).");
            return;
        }

        Products product = inventory.getProduct(section, productID);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        try {
            inventory.removeProduct(section, productID);
            System.out.println("Removed product ID " + productID + " from section " + section);
        } catch (NotFoundException e) {
            System.out.println("Remove product error: " + e.getMessage());
        }
    }

    // Managers can change product prices
    public void changePrice(Inventory inventory, String section, int productID, double newPrice) {
        if (!StoreDataLoader.isAisleInventorySection(section)) {
            System.out.println("Managers can only change prices in Dairy, Fruits, or Meats (store aisles).");
            return;
        }

        Products product = inventory.getProduct(section, productID);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        try {
            product.setPrice(newPrice);
            System.out.println(product.getName() + " price changed to $" + newPrice);
        } catch (InvalidPriceException e) {
            System.out.println("Price update error: " + e.getMessage());
        }
    }

    // Managers can view inventory
    public void viewInventory(Inventory inventory) {
        inventory.printInventory();
    }

    public void viewCustomerHistory(Customer customer) {
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        } else {
            System.out.println("Purchase history for " + customer.getFullName() + ":");

            customer.printCustomerHistory(); // prints the customer's purchase history from customer class
        
        }
    }
}