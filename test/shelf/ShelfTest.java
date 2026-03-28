package shelf;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.InvalidProductException;
import exceptions.InvalidSectionException;

import products.Products;

public class ShelfTest {

    private Shelf shelf;
    private Products milk;
    private Products bread;

    @BeforeEach
    void setUp() {
        shelf = new Shelf("Dairy");
        milk = new Products("Milk", 3.49, 10, 1001);
        bread = new Products("Bread", 2.49, 5, 1002);
    }

    @Test
    void constructorShouldInitializeSection() {
        assertEquals("Dairy", shelf.getSection());
        assertTrue(shelf.getProducts().isEmpty());
    }
    @Test
    void constructorShouldThrowForNullSection() {
        assertThrows(InvalidSectionException.class, () -> new Shelf(null));
    }

    @Test
    void constructorShouldThrowForBlankSection() {
        assertThrows(InvalidSectionException.class, () -> new Shelf("   "));
    }

    @Test
    void addProductShouldStoreProductById() {
        shelf.addProduct(milk);

        assertEquals(milk, shelf.getProduct(1001));
        assertEquals(1, shelf.getProducts().size());
    }

    @Test
    void addProductShouldReplaceSameId() {
        Products updatedMilk = new Products("Milk", 4.00, 20, 1001);

        shelf.addProduct(milk);
        shelf.addProduct(updatedMilk);

        assertEquals(1, shelf.getProducts().size());
        assertEquals(updatedMilk, shelf.getProduct(1001));
    }

    @Test
    void getProductShouldReturnNullIfMissing() {
        assertNull(shelf.getProduct(9999));
    }
    
    @Test
    void addProductShouldThrowForNullProduct() {
        assertThrows(InvalidProductException.class, () -> shelf.addProduct(null));
    }


    @Test
    void getProductsShouldReturnAllProducts() {
        shelf.addProduct(milk);
        shelf.addProduct(bread);

        Collection<Products> products = shelf.getProducts();

        assertEquals(2, products.size());
        assertTrue(products.contains(milk));
        assertTrue(products.contains(bread));
    }

    @Test
    void removeProductShouldDeleteMatchingProduct() throws Exception {
        shelf.addProduct(milk);
        shelf.removeProduct(1001);

        assertNull(shelf.getProduct(1001));
    }

    @Test
    void removeProductShouldDoNothingIfMissing() {
        shelf.addProduct(milk);

        shelf.removeProduct(9999);

        assertEquals(1, shelf.getProducts().size());
    }
}