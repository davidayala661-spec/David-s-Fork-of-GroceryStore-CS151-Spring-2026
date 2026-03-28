package customers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VIPCustomerTest {

    private VIPCustomer vipCustomer;

    @BeforeEach
    void setUp() throws Exception{
        vipCustomer = new VIPCustomer(102, "John", "Doe", 0.10);
    }

    @Test
    void constructorShouldInitializeFields() {
        assertEquals(102, vipCustomer.getCustomerId());
        assertEquals("John", vipCustomer.getFirstName());
        assertEquals("Doe", vipCustomer.getLastName());
        assertEquals(0.10, vipCustomer.getDiscountRate());
        assertEquals(0, vipCustomer.getPoints());
    }

    @Test
    void addPointsShouldIncreasePointsForPositiveValue() {
        vipCustomer.addPoints(50);
        assertEquals(50, vipCustomer.getPoints());
    }

    @Test
    void addPointsShouldIgnoreZeroOrNegativeValues() {
        vipCustomer.addPoints(0);
        vipCustomer.addPoints(-10);

        assertEquals(0, vipCustomer.getPoints());
    }

    @Test
    void vipCustomerShouldHaveCart() {
        assertNotNull(vipCustomer.getCart());
    }
}