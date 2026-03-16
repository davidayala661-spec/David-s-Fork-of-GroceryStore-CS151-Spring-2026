package employee;

import inventory.Inventory;
import product.Product;

public class Manager extends Employee {

    public Manager(String firstName, String lastName, int employeeID) {
        super(firstName, lastName, employeeID, "Manager", 0.10);
        // Managers get 10% discount
    }

    // Managers can add quantity to the inventory
    public void addProduct(Inventory inventory, String section, Product product) {
        inventory.addProduct(section, product);
        System.out.println("Added product " + product.getName() + " to section " + section);
    }

    // Managers can remove quantity from the inventory
    public void removeProduct(Inventory inventory, String section, int productID) {

        Product product = inventory.getProduct(section, productID);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        inventory.removeProduct(section, productID);
        System.out.println("Removed product ID " + productID + " from section " + section);
    }

    // Managers can change product prices
    public void changePrice(Inventory inventory, String section, int productID, double newPrice) {

        Product product = inventory.getProduct(section, productID);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        product.setPrice(newPrice);
        System.out.println(product.getName() + " price changed to $" + newPrice);
    }

    // Managers can view inventory
    public void viewInventory(Inventory inventory) {
        inventory.printInventory();
    }

    @Override
    public boolean canChangeQuantity() {
        return true; // managers can modify inventory quantities
    }
}