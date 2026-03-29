package employee;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import customers.RegularCustomer;
import exceptions.CapacityExceededException;
import inventory.Inventory;
import products.Products;

public class ManagerTest {

    private Manager manager;
    private Inventory inventory;

    @BeforeEach
    void setUp() throws Exception {
        manager = new Manager("John", "Smith", 3001);
        inventory = new Inventory();
    }

    @Test
    void constructorShouldInitializeFields() {
        assertEquals("John", manager.getFirstName());
        assertEquals("Smith", manager.getLastName());
        assertEquals("John Smith", manager.getFullName());
        assertEquals(3001, manager.getEmployeeID());
        assertEquals("Manager", manager.getDepartment());
        assertEquals(0.10, manager.getDiscountRate());
    }

    @Test
    void applyDiscountShouldApplyManagerDiscount() {
        assertEquals(90.0, manager.applyDiscount(100.0));
    }

    @Test
    void addProductShouldReturnTrueWhenSuccessful() throws CapacityExceededException {
        Products product = new Products("Milk", 3.49, 10, 1001);

        boolean added = manager.addProduct(inventory, "Dairy", product);

        assertTrue(added);
        assertEquals(product, inventory.getProduct("Dairy", 1001));
    }

    @Test
    void addProductShouldReturnFalseWhenCapacityExceeded() throws CapacityExceededException {
        for (int i = 0; i < 100; i++) {
            inventory.addProduct("Dairy", new Products("Item" + i, 1.0, 1, 2000 + i));
        }

        boolean added = manager.addProduct(inventory, "Dairy", new Products("Overflow", 1.0, 1, 9999));

        assertFalse(added);
    }

    @Test
    void addProductShouldReturnFalseForNonAisleSection() throws CapacityExceededException {
        Products product = new Products("Apples", 1.99, 5, 5001);
        assertFalse(manager.addProduct(inventory, "Produce", product));
        assertNull(inventory.getProduct("Produce", 5001));
    }

    @Test
    void changePriceShouldRejectNonAisleSection() throws CapacityExceededException {
        inventory.addProduct("Snacks", new Products("Chips", 2.99, 5, 3003));
        manager.changePrice(inventory, "Snacks", 3003, 3.49);
        assertEquals(2.99, inventory.getProduct("Snacks", 3003).getPrice());
    }

    @Test
    void removeProductShouldRejectNonAisleSection() throws CapacityExceededException {
        inventory.addProduct("Snacks", new Products("Chips", 2.99, 5, 3003));
        manager.removeProduct(inventory, "Snacks", 3003);
        assertNotNull(inventory.getProduct("Snacks", 3003));
    }

    @Test
    void changePriceShouldUpdateProductPrice() throws CapacityExceededException {
        Products milk = new Products("Milk", 3.49, 10, 1001);
        inventory.addProduct("Dairy", milk);

        manager.changePrice(inventory, "Dairy", 1001, 4.99);

        assertEquals(4.99, inventory.getProduct("Dairy", 1001).getPrice());
    }

    @Test
    void removeProductShouldDeleteProduct() throws CapacityExceededException {
        Products milk = new Products("Milk", 3.49, 10, 1001);
        inventory.addProduct("Dairy", milk);

        manager.removeProduct(inventory, "Dairy", 1001);

        assertNull(inventory.getProduct("Dairy", 1001));
    }

    @Test
    void viewCustomerHistoryShouldNotThrowForValidCustomer() {
        RegularCustomer customer = new RegularCustomer(101, "Vishal", "Raichur");

        assertDoesNotThrow(() -> manager.viewCustomerHistory(customer));
    }

    @Test
    void viewCustomerHistoryShouldNotThrowForNullCustomer() {
        assertDoesNotThrow(() -> manager.viewCustomerHistory(null));
    }

    @Test
    void viewCustomerHistoryShouldShowSavedHistory() {
        RegularCustomer customer = new RegularCustomer(101, "Vishal", "Raichur");
        customer.addPurchaseRecord("Receipt #1");

        assertDoesNotThrow(() -> manager.viewCustomerHistory(customer));
    }
}