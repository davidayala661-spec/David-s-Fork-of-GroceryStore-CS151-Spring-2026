package products;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.InvalidPriceException;
import exceptions.InvalidProductException;
import exceptions.InvalidQuantityException;

public class ProductsTest {

    private Products product;

    @BeforeEach
    void setUp() throws Exception {
        product = new Products("Milk", 3.49, 10, 1001);
    }

    @Test
    void constructorShouldInitializeFields() {
        assertEquals("Milk", product.getName());
        assertEquals(3.49, product.getPrice());
        assertEquals(10, product.getQuantity());
        assertEquals(1001, product.getID());
    }

    @Test
    void constructorShouldThrowForNullName() {
        assertThrows(InvalidProductException.class,
                () -> new Products(null, 3.49, 10, 1001));
    }

    @Test
    void constructorShouldThrowForBlankName() {
        assertThrows(InvalidProductException.class,
                () -> new Products("   ", 3.49, 10, 1001));
    }

    @Test
    void constructorShouldThrowForNegativePrice() {
        assertThrows(InvalidPriceException.class,
                () -> new Products("Milk", -1.0, 10, 1001));
    }

    @Test
    void constructorShouldThrowForNegativeQuantity() {
        assertThrows(InvalidQuantityException.class,
                () -> new Products("Milk", 3.49, -1, 1001));
    }

    @Test
    void constructorShouldThrowForInvalidId() {
        assertThrows(InvalidProductException.class,
                () -> new Products("Milk", 3.49, 10, 0));
    }

    @Test
    void setNameShouldUpdateWhenValid() throws Exception {
        product.setName("Chocolate Milk");
        assertEquals("Chocolate Milk", product.getName());
    }

    @Test
    void setNameShouldThrowForBlankName() {
        assertThrows(InvalidProductException.class, () -> product.setName(""));
    }

    @Test
    void setPriceShouldUpdatePrice() throws Exception {
        product.setPrice(4.25);
        assertEquals(4.25, product.getPrice());
    }

    @Test
    void setPriceShouldThrowForNegativeValue() {
        assertThrows(InvalidPriceException.class, () -> product.setPrice(-1.0));
    }

    @Test
    void setQuantityShouldUpdateQuantity() throws Exception {
        product.setQuantity(25);
        assertEquals(25, product.getQuantity());
    }

    @Test
    void setQuantityShouldThrowForNegativeValue() {
        assertThrows(InvalidQuantityException.class, () -> product.setQuantity(-1));
    }

    @Test
    void stockToShelfShouldIncreaseQuantity() throws Exception {
        product.stockToShelf(5);
        assertEquals(15, product.getQuantity());
    }

    @Test
    void stockToShelfShouldThrowForZeroOrNegativeAmount() {
        assertThrows(InvalidQuantityException.class, () -> product.stockToShelf(0));
        assertThrows(InvalidQuantityException.class, () -> product.stockToShelf(-3));
    }
}