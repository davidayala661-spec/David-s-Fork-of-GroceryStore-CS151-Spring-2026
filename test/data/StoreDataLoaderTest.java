package data;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import aisles.Aisles;
import exceptions.CapacityExceededException;
import inventory.Inventory;

public class StoreDataLoaderTest {

    @Test
    void loadDefaultAislesShouldCreateThreeAisles() {
        List<Aisles> aisles = StoreDataLoader.loadDefaultAisles();

        assertEquals(3, aisles.size());
        assertEquals("Dairy", aisles.get(0).getAisleType());
        assertEquals("Fruits", aisles.get(1).getAisleType());
        assertEquals("Meats", aisles.get(2).getAisleType());
    }

    @Test
    void loadDefaultAislesShouldCreateThreeShelvesPerAisle() {
        List<Aisles> aisles = StoreDataLoader.loadDefaultAisles();

        for (Aisles aisle : aisles) {
            assertEquals(3, aisle.getShelves().size());
        }
    }

    @Test
    void loadDefaultAislesShouldCreateFiveProductsPerShelf() {
        List<Aisles> aisles = StoreDataLoader.loadDefaultAisles();

        for (Aisles aisle : aisles) {
            aisle.getShelves().forEach((shelfNumber, products) -> assertEquals(5, products.size()));
        }
    }

    @Test
    void loadAislesIntoInventoryShouldLoadAllProducts() throws Exception {
        Inventory inventory = new Inventory();
        List<Aisles> aisles = StoreDataLoader.loadDefaultAisles();

        StoreDataLoader.loadAislesIntoInventory(inventory, aisles);

        assertEquals(45, inventory.getTotalProductCount());
        assertNotNull(inventory.findProductByExactName("Milk"));
        assertNotNull(inventory.findProductByExactName("Apples"));
        assertNotNull(inventory.findProductByExactName("Chicken Breast"));
    }
}