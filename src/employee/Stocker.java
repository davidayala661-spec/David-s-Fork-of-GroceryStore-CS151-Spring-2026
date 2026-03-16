package employee;

import inventory.Inventory;
import product.Product;

public class Stocker extends Employee {
    public Stocker(String firstName, String lastName, int employeeID) {
        super(firstName, lastName, employeeID, "Stocker", 0.06); 
        // Stockers get a 6% discount
    }

    private static final int LOW_STOCK = 10; // threshold for low stock, can be adjusted as needed


    public void stockProduct(Inventory inventory, String section, int productID, int quantity) {
        
        Product product = inventory.getProduct(section, productID);

        if (product == null) {
            System.out.println(productID + " not found in section " + section);
            return;
        }

        try {
            product.addStock(quantity);
            System.out.println("Stocked " + quantity + " " + product.getName() + " in section " + section);
        } catch (IllegalArgumentException e) {
            System.out.println("Error stocking product: " + e.getMessage());
        }
    }

    public void viewLowShelfStock(Inventory inventory, String section) {
        System.out.println("Low stock products in section " + section + ":");
        for (int i = 0; i < inventory.getSectionSize(section); i++) {
            Product product = inventory.getProduct(section, i);
            if (product != null && product.getStockQuantity() < LOW_STOCK) { // low stock and tells them to restock
                System.out.println("- " + product.getName() 
                    + " (ID: " + product.getProductID() + ", Stock: " 
                    + product.getStockQuantity() + ")");
            }
        }
    }

    @Override
    public boolean canChangeQuantity() {
        return false; // stockers cannot change the stock quantity, only view and restock in sections
    }
}