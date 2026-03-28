package inventory;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.CapacityExceededException;
import exceptions.DuplicateProductException;
import exceptions.InvalidProductException;
import exceptions.InvalidQuantityException;
import exceptions.InvalidSectionException;
import exceptions.NotFoundException;
import products.Products;

public class InventoryTest {

    private Inventory inventory;
    private Products apples;
    private Products milk;
    private Products chips;

    @BeforeEach
    void setUp() throws Exception {
        inventory = new Inventory();
        apples = new Products("Apples", 1.99, 20, 1001);
        milk = new Products("Milk", 3.49, 8, 1002);
        chips = new Products("Chips", 2.99, 5, 1003);

        inventory.addProduct("Produce", apples);
        inventory.addProduct("Dairy", milk);
        inventory.addProduct("Snacks", chips);
    }

    @Test
    void addProductShouldIncreaseTotalCount() throws Exception {
        Products rice = new Products("Rice", 10.99, 50, 1004);

        inventory.addProduct("Supplies", rice);

        assertEquals(4, inventory.getTotalProductCount());
        assertEquals(rice, inventory.getProduct("Supplies", 1004));
    }

    @Test
    void addProductShouldThrowWhenInventoryExceeds100DistinctProducts() throws Exception {
        Inventory testInventory = new Inventory();

        for (int i = 1; i <= 100; i++) {
            testInventory.addProduct("Section", new Products("Item" + i, 1.0, 1, 2000 + i));
        }

        assertThrows(CapacityExceededException.class,
                () -> testInventory.addProduct("Section", new Products("Overflow", 1.0, 1, 9999)));
    }

    @Test
    void addProductShouldThrowWhenProductQuantityExceeds100() {
        Inventory testInventory = new Inventory();

        assertThrows(CapacityExceededException.class,
                () -> testInventory.addProduct("Produce", new Products("Bulk", 2.0, 101, 5000)));
    }

    @Test
    void addProductShouldThrowForDuplicateIdInSameSection() throws Exception {
        assertThrows(DuplicateProductException.class, () ->
                inventory.addProduct("Produce", new Products("Green Apples", 2.49, 15, 1001)));
    }

    @Test
    void removeProductShouldDeleteProduct() throws Exception {
        inventory.removeProduct("Snacks", 1003);

        assertNull(inventory.getProduct("Snacks", 1003));
        assertEquals(2, inventory.getTotalProductCount());
    }

    @Test
    void removeProductShouldThrowForMissingSection() {
        assertThrows(NotFoundException.class, () -> inventory.removeProduct("Frozen", 1003));
    }

    @Test
    void removeProductShouldThrowForMissingProduct() {
        assertThrows(NotFoundException.class, () -> inventory.removeProduct("Snacks", 9999));
    }

    @Test
    void getProductShouldReturnNullForMissingSection() {
        assertNull(inventory.getProduct("Frozen", 1001));
    }

    @Test
    void findProductShouldReturnMatchingProduct() throws Exception {
        Products found = inventory.findProduct(1002);
        assertEquals(milk, found);
    }

    @Test
    void findProductShouldThrowForMissingId() {
        assertThrows(NotFoundException.class, () -> inventory.findProduct(9999));
    }

    @Test
    void restockProductShouldIncreaseQuantity() throws Exception {
        inventory.restockProduct("Dairy", 1002, 10);
        assertEquals(18, inventory.getProduct("Dairy", 1002).getQuantity());
    }

    @Test
    void restockProductShouldThrowForNonPositiveQuantity() {
        assertThrows(InvalidQuantityException.class, () -> inventory.restockProduct("Dairy", 1002, 0));
    }

    @Test
    void restockProductShouldThrowForMissingProduct() {
        assertThrows(NotFoundException.class, () -> inventory.restockProduct("Dairy", 9999, 5));
    }

    @Test
    void restockProductShouldThrowWhenStockWouldExceed100() {
        assertThrows(InvalidQuantityException.class, () -> inventory.restockProduct("Produce", 1001, 81));
    }

    @Test
    void decreaseStockShouldReduceQuantity() throws Exception {
        inventory.decreaseStock("Produce", 1001, 5);
        assertEquals(15, inventory.getProduct("Produce", 1001).getQuantity());
    }

    @Test
    void decreaseStockShouldThrowForNonPositiveQuantity() {
        assertThrows(InvalidQuantityException.class, () -> inventory.decreaseStock("Produce", 1001, 0));
    }

    @Test
    void decreaseStockShouldThrowForMissingProduct() {
        assertThrows(NotFoundException.class, () -> inventory.decreaseStock("Produce", 9999, 1));
    }

    @Test
    void decreaseStockShouldThrowWhenNotEnoughStock() {
        assertThrows(InvalidQuantityException.class, () -> inventory.decreaseStock("Produce", 1001, 999));
    }

    @Test
    void listLowStockShouldReturnProductsBelowThreshold() {
        List<Products> lowStock = inventory.listLowStock(10);

        assertEquals(2, lowStock.size());
        assertTrue(lowStock.contains(milk));
        assertTrue(lowStock.contains(chips));
    }

    @Test
    void searchByNameShouldMatchCaseInsensitiveKeyword() {
        List<Products> matches = inventory.searchByName("mi");

        assertEquals(1, matches.size());
        assertEquals(milk, matches.get(0));
    }

    @Test
    void findProductByExactNameShouldMatchIgnoringCase() {
        Products found = inventory.findProductByExactName("milk");

        assertNotNull(found);
        assertEquals(milk, found);
    }

    @Test
    void findProductByExactNameShouldReturnNullForBlankName() {
        assertNull(inventory.findProductByExactName("   "));
    }

    @Test
    void getProductsBySectionShouldReturnProductsInSection() {
        List<Products> produceProducts = inventory.getProductsBySection("Produce");

        assertEquals(1, produceProducts.size());
        assertEquals(apples, produceProducts.get(0));
    }

    @Test
    void getProductsBySectionShouldReturnEmptyListForMissingSection() {
        assertTrue(inventory.getProductsBySection("Frozen").isEmpty());
    }

    @Test
    void getNextProductIdShouldReturnMaxPlusOne() {
        assertEquals(1004, inventory.getNextProductId());
    }

    @Test
    void toStringShouldContainProductCount() {
        assertTrue(inventory.toString().contains("3 products"));
    }

    @Test
    void addProductShouldThrowForNullSection() throws Exception {
        Inventory testInventory = new Inventory();
        Products product = new Products("Milk", 3.49, 10, 1001);

        assertThrows(InvalidSectionException.class,
                () -> testInventory.addProduct(null, product));
    }

    @Test
    void addProductShouldThrowForBlankSection() throws Exception {
        Inventory testInventory = new Inventory();
        Products product = new Products("Milk", 3.49, 10, 1001);

        assertThrows(InvalidSectionException.class,
                () -> testInventory.addProduct("   ", product));
    }

    @Test
    void addProductShouldThrowForNullProduct() {
        Inventory testInventory = new Inventory();

        assertThrows(InvalidProductException.class,
                () -> testInventory.addProduct("Dairy", null));
    }

    @Test
    void addProductShouldThrowForDuplicateId() throws Exception {
        Inventory testInventory = new Inventory();
        Products milk1 = new Products("Milk", 3.49, 10, 1001);
        Products milk2 = new Products("Milk 2", 4.49, 8, 1001);

        testInventory.addProduct("Dairy", milk1);

        assertThrows(DuplicateProductException.class,
                () -> testInventory.addProduct("Dairy", milk2));
    }
}