package cart;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShoppingCartTest {

    private ShoppingCart cart;

    @BeforeEach
    void setUp () throws Exception{
        cart = new ShoppingCart();
    }

    @Test
    void newCartShouldBeEmpty() {
        assertTrue(cart.isEmpty());
        assertEquals(0, cart.getTotalItems());
    }

    @Test
    void addItemShouldIncreaseItemCount() {
        cart.addItem("Milk");

        assertFalse(cart.isEmpty());
        assertEquals(1, cart.getTotalItems());
        assertEquals(List.of("Milk"), cart.getItemsSnapshot());
    }

    @Test
    void removeItemShouldRemoveMatchingItem() {
        cart.addItem("Milk");
        cart.addItem("Bread");

        cart.removeItem("Milk");

        assertEquals(1, cart.getTotalItems());
        assertEquals(List.of("Bread"), cart.getItemsSnapshot());
    }

    @Test
    void removeItemShouldDoNothingIfItemMissing() {
        cart.addItem("Milk");

        cart.removeItem("Eggs");

        assertEquals(1, cart.getTotalItems());
        assertEquals(List.of("Milk"), cart.getItemsSnapshot());
    }

    @Test
    void clearCartShouldRemoveEverything() {
        cart.addItem("Milk");
        cart.addItem("Bread");

        cart.clearCart();

        assertTrue(cart.isEmpty());
        assertEquals(0, cart.getTotalItems());
    }

    @Test
    void getItemsSnapshotShouldReturnCopyNotOriginalList() {
        cart.addItem("Milk");

        List<String> snapshot = cart.getItemsSnapshot();
        snapshot.add("Eggs");

        assertEquals(1, cart.getTotalItems());
        assertEquals(List.of("Milk"), cart.getItemsSnapshot());
    }

    @Test
    void cartShouldNotAllowMoreThan100Items() {
        for (int i = 0; i < 100; i++) {
            cart.addItem("Item" + i);
        }

        cart.addItem("OverflowItem");

        assertEquals(100, cart.getTotalItems());
        assertFalse(cart.getItemsSnapshot().contains("OverflowItem"));
    }

    @Test
    void toStringShouldContainItemCount() {
        cart.addItem("Milk");
        assertTrue(cart.toString().contains("1 item"));
    }
}