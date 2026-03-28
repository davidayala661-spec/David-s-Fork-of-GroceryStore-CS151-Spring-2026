package employee;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.CapacityExceededException;
import inventory.Inventory;
import products.Products;
import shelf.Shelf;

public class StockerTest {

    private Stocker stocker;
    private Inventory inventory;
    private Shelf shelf;
    private Products apples;

    @BeforeEach
    void setUp() throws Exception {
        stocker = new Stocker("Doe", "John", 2001);
        inventory = new Inventory();
        shelf = new Shelf("Produce");
        apples = new Products("Apples", 1.99, 20, 1001);

        inventory.addProduct("Produce", apples);
    }

    @Test
    void constructorShouldInitializeFields() {
        assertEquals("Doe", stocker.getFirstName());
        assertEquals("John", stocker.getLastName());
        assertEquals(2001, stocker.getEmployeeID());
        assertEquals("Stocker", stocker.getDepartment());
        assertEquals(0.06, stocker.getDiscountRate());
    }

    @Test
    void applyDiscountShouldApplyStockerDiscount() {
        assertEquals(94.0, stocker.applyDiscount(100.0));
    }

    @Test
    void stockProductShouldIncreaseQuantityAndAddToShelf() {
        stocker.stockProduct(inventory, shelf, 1001, 5);

        assertEquals(25, apples.getQuantity());
        assertEquals(apples, shelf.getProduct(1001));
    }

    @Test
    void stockProductShouldDoNothingWhenProductMissing() {
        stocker.stockProduct(inventory, shelf, 9999, 5);

        assertNull(shelf.getProduct(9999));
        assertEquals(20, apples.getQuantity());
    }

    @Test
    void stockProductShouldNotAllowInvalidAmount() {
        assertDoesNotThrow(() -> stocker.stockProduct(inventory, shelf, 1001, 0));
        assertNull(shelf.getProduct(1001));
        assertEquals(20, apples.getQuantity());
    }

    @Test
    void viewLowShelfStockShouldNotThrow() {
        shelf.addProduct(new Products("Bananas", 1.29, 2, 1002));
        assertDoesNotThrow(() -> stocker.viewLowShelfStock(shelf, 5));
    }
}