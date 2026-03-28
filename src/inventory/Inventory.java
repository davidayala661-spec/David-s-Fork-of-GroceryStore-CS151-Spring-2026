package inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import exceptions.CapacityExceededException;
import exceptions.NotFoundException;
import exceptions.InvalidQuantityException;
import products.Products;

public class Inventory {
    private static final int MAX_PRODUCTS = 100;

    // section -> (productId -> product)
    private HashMap<String, HashMap<Integer, Products>> inventory;

    public Inventory() {
        inventory = new HashMap<>();
    }

    public void addProduct(String section, Products product) throws CapacityExceededException {
        if (getTotalProductCount() >= MAX_PRODUCTS) {
            throw new CapacityExceededException("Cannot add more than 100 products to inventory.");
        }

        if (product.getQuantity() > MAX_PRODUCTS) {
            throw new CapacityExceededException("Product stock cannot exceed 100.");
        }

        inventory.putIfAbsent(section, new HashMap<Integer, Products>());
        HashMap<Integer, Products> sectionProducts = inventory.get(section);

        if (sectionProducts.containsKey(product.getID())) {
            System.out.println("Product ID already exists in section " + section);
            return;
        }

        sectionProducts.put(product.getID(), product);
    }

    public void removeProduct(String section, int productID) throws NotFoundException {
        if (!inventory.containsKey(section)) {
            throw new NotFoundException("Section not found: " + section);
        }

        HashMap<Integer, Products> sectionProducts = inventory.get(section);

        if (!sectionProducts.containsKey(productID)) {
            throw new NotFoundException("Product ID " + productID + " not found in section " + section);
        }

        sectionProducts.remove(productID);

        if (sectionProducts.isEmpty()) {
            inventory.remove(section);
        }
    }

    public Products getProduct(String section, int productID) {
        if (!inventory.containsKey(section)) {
            return null;
        }
        return inventory.get(section).get(productID);
    }

    public Products findProduct(int productID) throws NotFoundException {
        for (HashMap<Integer, Products> sectionProducts : inventory.values()) {
            if (sectionProducts.containsKey(productID)) {
                return sectionProducts.get(productID);
            }
        }
        throw new NotFoundException("Product ID " + productID + " not found in inventory.");
    }

    public void restockProduct(String section, int productID, int quantity)
            throws NotFoundException, InvalidQuantityException {
        if (quantity <= 0) {
            throw new InvalidQuantityException("Restock quantity must be greater than 0.");
        }

        Products product = getProduct(section, productID);

        if (product == null) {
            throw new NotFoundException("Product ID " + productID + " not found in section " + section);
        }

        if (product.getQuantity() + quantity > MAX_PRODUCTS) {
            throw new InvalidQuantityException("Stock cannot exceed 100 for " + product.getName());
        }

        product.setQuantity(product.getQuantity() + quantity);
    }

    public void decreaseStock(String section, int productID, int quantity)
            throws NotFoundException, InvalidQuantityException {
        if (quantity <= 0) {
            throw new InvalidQuantityException("Decrease quantity must be greater than 0.");
        }

        Products product = getProduct(section, productID);

        if (product == null) {
            throw new NotFoundException("Product ID " + productID + " not found in section " + section);
        }

        if (quantity > product.getQuantity()) {
            throw new InvalidQuantityException("Not enough stock available for " + product.getName());
        }

        product.setQuantity(product.getQuantity() - quantity);
    }

    public List<Products> listLowStock(int threshold) {
        List<Products> lowStockProducts = new ArrayList<Products>();

        for (HashMap<Integer, Products> sectionProducts : inventory.values()) {
            for (Products product : sectionProducts.values()) {
                if (product.getQuantity() < threshold) {
                    lowStockProducts.add(product);
                }
            }
        }

        return lowStockProducts;
    }

    public List<Products> searchByName(String keyword) {
        List<Products> matches = new ArrayList<Products>();
        String lowerKeyword = keyword.toLowerCase();

        for (HashMap<Integer, Products> sectionProducts : inventory.values()) {
            for (Products product : sectionProducts.values()) {
                if (product.getName().toLowerCase().contains(lowerKeyword)) {
                    matches.add(product);
                }
            }
        }

        return matches;
    }

    public Products findProductByExactName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        for (HashMap<Integer, Products> sectionProducts : inventory.values()) {
            for (Products product : sectionProducts.values()) {
                if (product.getName().equalsIgnoreCase(name.trim())) {
                    return product;
                }
            }
        }
        return null;
    }

    public void printInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        System.out.println("Full Inventory:");
        for (String section : inventory.keySet()) {
            System.out.println("Section: " + section);
            for (Products product : inventory.get(section).values()) {
                System.out.println("- " + product.getName()
                        + " (ID: " + product.getID()
                        + ", Price: $" + product.getPrice()
                        + ", Stock: " + product.getQuantity() + ")");
            }
        }
    }

    public int getTotalProductCount() {
        int count = 0;
        for (HashMap<Integer, Products> sectionProducts : inventory.values()) {
            count += sectionProducts.size();
        }
        return count;
    }

    public boolean printSectionProducts(String section) {
        if (!inventory.containsKey(section) || inventory.get(section).isEmpty()) {
            System.out.println("No products found in section " + section + ".");
            return false;
        }

        System.out.println("Products in " + section + ":");
        for (Products product : inventory.get(section).values()) {
            int needed = MAX_PRODUCTS - product.getQuantity();
            System.out.println("- " + product.getName() + " (ID: " + product.getID() + ", Stock: " + product.getQuantity() + ", Price: $" + product.getPrice() + ", Needs: " + needed + ")");

        }

        return true;
    }

    public List<Products> getProductsBySection(String section) {
        List<Products> productsInSection = new ArrayList<>();

        if (!inventory.containsKey(section)) {
            return productsInSection;
        }

        productsInSection.addAll(inventory.get(section).values());
        return productsInSection;
    }

    public int getNextProductId() {
        int maxId = 1000;

        for (HashMap<Integer, Products> sectionProducts : inventory.values()) {
            for (Products product : sectionProducts.values()) {
                if (product.getID() > maxId) {
                    maxId = product.getID();
                }
            }
        }

        return maxId + 1;
    }

    @Override
    public String toString() {
        return "Inventory with " + getTotalProductCount() + " products.";
    }
}