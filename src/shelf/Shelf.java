package shelf;

import java.util.HashMap;
import products.Products;
import java.util.Collection;

public class Shelf {
    
    private String section;
    private HashMap<Integer, Products> products; // maps product ID to Product

    public Shelf(String section) {
        this.section = section;
        this.products = new HashMap<>();
    }

    public String getSection() {
        return section;
    }

    public void addProduct(Products product) { // adds product to shelf
        products.put(product.getID(), product);
    }

    public Products getProduct(int productID) {
        return products.get(productID);
    }

    public Collection<Products> getProducts() {
        return products.values();
    }

    public void removeProduct(int productID) {
        products.remove(productID);
    }

    public void printLowStock(int count) { // prints out products thats low stock in the shelf
        System.out.println("Low stock products in shelf " + section + ":");
        
        for (Products product : products.values()) {
            if (product.getQuantity() < count) { // low stock and tells them to restock
                System.out.println("- " + product.getName() + " (ID: " + product.getID() + ", Stock: " + product.getQuantity() + ")");
            }
        }
    }

    public void printShelf() {
        System.out.println("Products on shelf " + section + ":");
        for (Products product : products.values()) {
            System.out.println("- " + product.getName() + " (ID: " + product.getID() + ", Price: $" + product.getPrice() + ", Stock: " + product.getQuantity() + ")");
        }
    }

}
