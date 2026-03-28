package checkout;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import aisles.Aisles;
import customers.RegularCustomer;
import customers.VIPCustomer;
import exceptions.CapacityExceededException;
import inventory.Inventory;
import products.Products;

public class CheckoutTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() throws Exception{
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void printReceiptShouldHandleEmptyCart() {
        RegularCustomer customer = new RegularCustomer(101, "Vishal", "Raichur");
        Inventory inventory = new Inventory();

        Checkout.printReceipt(customer, inventory);

        assertTrue(outContent.toString().contains("Cart is empty. Nothing to checkout."));
    }

    @Test
    void printReceiptShouldClearCartAfterCheckout() throws Exception {
        RegularCustomer customer = new RegularCustomer(101, "Vishal", "Raichur");
        Inventory inventory = new Inventory();
        inventory.addProduct("Dairy", new Products("Milk", 3.49, 10, 1001));

        customer.getCart().addItem("Milk");
        customer.getCart().addItem("Milk");

        Checkout.printReceipt(customer, inventory);

        assertTrue(outContent.toString().contains("GROCERY STORE RECEIPT"));
        assertEquals(0, customer.getCart().getTotalItems());
    }

    @Test
    void printReceiptShouldApplyVipDiscount() throws CapacityExceededException {
        VIPCustomer customer = new VIPCustomer(102, "John", "Doe", 0.10);
        Inventory inventory = new Inventory();
        inventory.addProduct("Dairy", new Products("Milk", 10.00, 10, 1001));

        customer.getCart().addItem("Milk");

        Checkout.printReceipt(customer, inventory);

        String output = outContent.toString();
        assertTrue(output.contains("VIP discount"));
        assertTrue(output.contains("TOTAL"));
    }

    @Test
    void printReceiptShouldResolvePriceFromAislesWhenMissingInInventory() {
        RegularCustomer customer = new RegularCustomer(101, "Vishal", "Raichur");
        Inventory inventory = new Inventory();

        Aisles aisle = new Aisles("Dairy", 1);
        aisle.addProductToShelf(1, new Products("Milk", 3.49, 10, 1001));

        customer.getCart().addItem("Milk");

        Checkout.printReceipt(customer, inventory, List.of(aisle));

        String output = outContent.toString();
        assertTrue(output.contains("Milk"));
        assertTrue(output.contains("3.49"));
    }

    @Test
    void printReceiptShouldHandleBlankItemNames() {
        RegularCustomer customer = new RegularCustomer(101, "Vishal", "Raichur");
        Inventory inventory = new Inventory();

        customer.getCart().addItem("   ");

        Checkout.printReceipt(customer, inventory);

        assertTrue(outContent.toString().contains("Cart has no valid item names. Nothing to checkout."));
    }
}