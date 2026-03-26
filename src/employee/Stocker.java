package employee;

import inventory.Inventory;
import products.Products;
import shelf.Shelf;

public class Stocker extends Employee {
    public Stocker(String firstName, String lastName, int employeeID) {
        super(firstName, lastName, employeeID, "Stocker", 0.06); 
        // Stockers get a 6% discount
    }


    public void stockProduct(Inventory inventory, Shelf shelf, int productID, int quantity) {
        // add inventory change like subtract from inventory and interact with product and inventory to add stock to the product in the section
        Products products = inventory.getProduct(shelf.getSection(), productID);

        if (products == null) {
            System.out.println(productID + " not found in section " + shelf.getSection());
            return;
        }

        try {
            products.stockToShelf(quantity); // move inventory ot shelf
            shelf.addProduct(products); // add product to shelf

            System.out.println("Stocked " + quantity + " " + products.getName() + " in shelf " + shelf.getSection());
        } catch (IllegalArgumentException e) {
            System.out.println("Error stocking product: " + e.getMessage());
        }
    }

    public void viewLowShelfStock(Shelf shelf, int threshold) {
        shelf.printLowStock(threshold); // print low stock products in the shelf
    }

}