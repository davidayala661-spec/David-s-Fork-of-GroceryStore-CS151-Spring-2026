package data;

import aisles.Aisles;
import exceptions.CapacityExceededException;
import exceptions.DuplicateProductException;
import exceptions.InvalidPriceException;
import exceptions.InvalidProductException;
import exceptions.InvalidQuantityException;
import exceptions.InvalidSectionException;
import inventory.Inventory;
import java.util.ArrayList;
import java.util.List;
import products.Products;

public class StoreDataLoader {
    private static final int DEFAULT_AISLE_COUNT = 3;
    private static final int DEFAULT_SHELVES_PER_AISLE = 3;
    private static final int DEFAULT_PRODUCTS_PER_SHELF = 5;
    private static final int STARTING_PRODUCT_ID = 1000;
    private static final int MAX_QUANTITY = 10;

    private static final String[] AISLE_TYPES = {"Dairy", "Fruits", "Meats"};

    private static final String[][] AISLE_PRODUCTS = {
            {"Milk", "Cheese", "Yogurt", "Butter", "Cream",
                    "Cottage Cheese", "Sour Cream", "Ice Cream", "Greek Yogurt", "Chocolate Milk",
                    "Half and Half", "Whipped Cream", "Mozzarella", "Cheddar", "Parmesan"},
            {"Apples", "Bananas", "Oranges", "Strawberries", "Blueberries",
                    "Grapes", "Mangos", "Pineapples", "Peaches", "Pears",
                    "Watermelon", "Kiwi", "Cherries", "Plums", "Lemons"},
            {"Chicken Breast", "Ground Beef", "Turkey", "Pork Chops", "Salmon",
                    "Tuna", "Bacon", "Sausage", "Ham", "Steak",
                    "Lamb Chops", "Shrimp", "Tilapia", "Cod", "Meatballs"}
    };

    private static final double[][] AISLE_PRICES = {
            {3.49, 4.99, 1.99, 2.99, 2.79, 3.29, 2.49, 5.99, 1.89, 3.99, 2.69, 3.19, 4.59, 4.79, 5.49},
            {1.99, 0.79, 1.29, 3.99, 4.49, 2.99, 1.49, 3.49, 2.29, 1.89, 5.99, 0.99, 3.79, 2.19, 0.89},
            {6.99, 5.49, 4.99, 6.49, 8.99, 7.49, 5.99, 4.49, 5.29, 10.99, 11.49, 9.99, 7.99, 8.49, 6.29}
    };

    private StoreDataLoader() {
    }

    public static List<Aisles> loadDefaultAisles() {
        List<Aisles> aisles = new ArrayList<>();
        int nextProductId = STARTING_PRODUCT_ID;

        for (int aisleNumber = 1; aisleNumber <= DEFAULT_AISLE_COUNT; aisleNumber++) {
            String aisleType = AISLE_TYPES[aisleNumber - 1];
            Aisles aisle = new Aisles(aisleType, aisleNumber);

            for (int shelfNumber = 1; shelfNumber <= DEFAULT_SHELVES_PER_AISLE; shelfNumber++) {
                aisle.addShelf(shelfNumber);

                for (int productIndex = 1; productIndex <= DEFAULT_PRODUCTS_PER_SHELF; productIndex++) {
                    int listIndex = (shelfNumber - 1) * DEFAULT_PRODUCTS_PER_SHELF + (productIndex - 1);
                    String productName = AISLE_PRODUCTS[aisleNumber - 1][listIndex];
                    double price = AISLE_PRICES[aisleNumber - 1][listIndex];
                    int quantity = Math.min(MAX_QUANTITY, 6 + productIndex);

                    try {
                        Products product = new Products(productName, price, quantity, nextProductId);
                        aisle.addProductToShelf(shelfNumber, product);
                        nextProductId++;
                    } catch (Exception e) {
                        throw new RuntimeException("Error loading default aisle data: " + e.getMessage(), e);
                    }
                }
            }

            aisles.add(aisle);
        }

        return aisles;
    }

    public static void loadAislesIntoInventory(Inventory inventory, List<Aisles> aisles)
            throws CapacityExceededException, InvalidSectionException,
                   InvalidProductException, DuplicateProductException {
        for (Aisles aisle : aisles) {
            String sectionName = aisle.getAisleType();

            for (Products product : aisle.getAllProducts()) {
                inventory.addProduct(sectionName, product);
            }
        }
    }
}
