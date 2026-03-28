package aisles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import products.Products;

public class AislesTest {

    private Aisles aisle;
    private Products milk;
    private Products cheese;

    @BeforeEach
    void setUp() throws Exception{
        aisle = new Aisles("Dairy", 1);
        milk = new Products("Milk", 3.49, 10, 1001);
        cheese = new Products("Cheese", 4.99, 5, 1002);
    }

    @Test
    void constructorShouldInitializeFieldsCorrectly() {
        assertEquals("Dairy", aisle.getAisleType());
        assertEquals(1, aisle.getAisleNumber());
        assertTrue(aisle.getShelves().isEmpty());
    }

    @Test
    void constructorShouldThrowForNullAisleType() {
        assertThrows(IllegalArgumentException.class, () -> new Aisles(null, 1));
    }

    @Test
    void constructorShouldThrowForBlankAisleType() {
        assertThrows(IllegalArgumentException.class, () -> new Aisles("   ", 1));
    }

    @Test
    void constructorShouldThrowForInvalidAisleNumber() {
        assertThrows(IllegalArgumentException.class, () -> new Aisles("Dairy", 0));
    }

    @Test
    void setAisleTypeShouldUpdateType() {
        aisle.setAisleType("Frozen");
        assertEquals("Frozen", aisle.getAisleType());
    }

    @Test
    void setAisleTypeShouldThrowForBlankValue() {
        assertThrows(IllegalArgumentException.class, () -> aisle.setAisleType(""));
    }

    @Test
    void addShelfShouldCreateShelf() {
        aisle.addShelf(1);

        Map<Integer, List<Products>> shelves = aisle.getShelves();
        assertTrue(shelves.containsKey(1));
        assertTrue(shelves.get(1).isEmpty());
    }

    @Test
    void addShelfShouldThrowForInvalidShelfNumber() {
        assertThrows(IllegalArgumentException.class, () -> aisle.addShelf(0));
    }

    @Test
    void addProductToShelfShouldAddProduct() {
        aisle.addProductToShelf(1, milk);

        List<Products> products = aisle.getProductsOnShelf(1);
        assertEquals(1, products.size());
        assertEquals(milk, products.get(0));
    }

    @Test
    void addProductToShelfShouldCreateShelfIfMissing() {
        aisle.addProductToShelf(5, milk);

        assertTrue(aisle.getShelves().containsKey(5));
        assertEquals(1, aisle.getProductsOnShelf(5).size());
    }

    @Test
    void addProductToShelfShouldThrowForNullProduct() {
        assertThrows(IllegalArgumentException.class, () -> aisle.addProductToShelf(1, null));
    }

    @Test
    void addProductToShelfShouldThrowForInvalidShelfNumber() {
        assertThrows(IllegalArgumentException.class, () -> aisle.addProductToShelf(0, milk));
    }

    @Test
    void removeProductFromShelfShouldRemoveMatchingProduct() {
        aisle.addProductToShelf(1, milk);
        aisle.addProductToShelf(1, cheese);

        boolean removed = aisle.removeProductFromShelf(1, 1001);

        assertTrue(removed);
        assertEquals(1, aisle.getProductsOnShelf(1).size());
        assertEquals(cheese, aisle.getProductsOnShelf(1).get(0));
    }

    @Test
    void removeProductFromShelfShouldRemoveShelfWhenEmpty() {
        aisle.addProductToShelf(1, milk);

        boolean removed = aisle.removeProductFromShelf(1, 1001);

        assertTrue(removed);
        assertFalse(aisle.getShelves().containsKey(1));
    }

    @Test
    void removeProductFromShelfShouldReturnFalseForMissingShelf() {
        assertFalse(aisle.removeProductFromShelf(99, 1001));
    }

    @Test
    void removeProductFromShelfShouldReturnFalseForMissingProduct() {
        aisle.addProductToShelf(1, milk);
        assertFalse(aisle.removeProductFromShelf(1, 9999));
    }

    @Test
    void getProductsOnShelfShouldReturnEmptyListForMissingShelf() {
        assertTrue(aisle.getProductsOnShelf(10).isEmpty());
    }

    @Test
    void getProductsOnShelfShouldBeUnmodifiable() {
        aisle.addProductToShelf(1, milk);

        List<Products> products = aisle.getProductsOnShelf(1);
        assertThrows(UnsupportedOperationException.class, () -> products.add(cheese));
    }

    @Test
    void getAllProductsShouldReturnAllProductsAcrossShelves() {
        aisle.addProductToShelf(1, milk);
        aisle.addProductToShelf(2, cheese);

        List<Products> allProducts = aisle.getAllProducts();

        assertEquals(2, allProducts.size());
        assertTrue(allProducts.contains(milk));
        assertTrue(allProducts.contains(cheese));
    }

    @Test
    void getShelvesShouldBeUnmodifiable() {
        aisle.addShelf(1);

        Map<Integer, List<Products>> shelves = aisle.getShelves();
        assertThrows(UnsupportedOperationException.class, () -> shelves.put(2, List.of()));
    }

    @Test
    void toStringShouldContainAisleInfo() {
        assertTrue(aisle.toString().contains("Aisle 1"));
        assertTrue(aisle.toString().contains("Dairy"));
    }
}