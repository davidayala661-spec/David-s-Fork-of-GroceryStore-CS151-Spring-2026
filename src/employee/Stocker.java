package employee;

import aisles.Aisles;
import inventory.Inventory;
import products.Products;
import shelf.Shelf;
import exceptions.InvalidProductException;
import exceptions.InvalidQuantityException;

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
        } catch (InvalidQuantityException | InvalidProductException e) {
            System.out.println("Error stocking product: " + e.getMessage());
        }
    }

    public void viewLowShelfStock(Shelf shelf, int threshold) {
        shelf.printLowStock(threshold); // print low stock products in the shelf
    }

    public void viewLowAisleShelfStock(Aisles aisle, int shelfNumber, int threshold) {
        System.out.println("Low stock on Aisle " + aisle.getAisleNumber() + " (" + aisle.getAisleType()
                + "), shelf " + shelfNumber + ":");
        boolean any = false;
        for (Products product : aisle.getProductsOnShelf(shelfNumber)) {
            if (product.getQuantity() < threshold) {
                any = true;
                System.out.println("- " + product.getName() + " (ID: " + product.getID()
                        + ", Stock: " + product.getQuantity() + ")");
            }
        }
        if (!any) {
            System.out.println("(No products below threshold.)");
        }
    }

}