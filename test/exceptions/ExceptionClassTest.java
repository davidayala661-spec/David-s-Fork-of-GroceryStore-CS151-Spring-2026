package exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ExceptionClassTest {

    @Test
    void invalidPriceExceptionShouldStoreMessage() {
        InvalidPriceException ex = new InvalidPriceException("Bad price");
        assertEquals("Bad price", ex.getMessage());
    }

    @Test
    void invalidProductExceptionShouldStoreMessage() {
        InvalidProductException ex = new InvalidProductException("Bad product");
        assertEquals("Bad product", ex.getMessage());
    }

    @Test
    void invalidSectionExceptionShouldStoreMessage() {
        InvalidSectionException ex = new InvalidSectionException("Bad section");
        assertEquals("Bad section", ex.getMessage());
    }

    @Test
    void duplicateProductExceptionShouldStoreMessage() {
        DuplicateProductException ex = new DuplicateProductException("Duplicate");
        assertEquals("Duplicate", ex.getMessage());
    }

    @Test
    void capacityExceededExceptionShouldStoreMessage() {
        CapacityExceededException ex = new CapacityExceededException("Too many");
        assertEquals("Too many", ex.getMessage());
    }

    @Test
    void invalidQuantityExceptionShouldStoreMessage() {
        InvalidQuantityException ex = new InvalidQuantityException("Bad quantity");
        assertEquals("Bad quantity", ex.getMessage());
    }

    @Test
    void notFoundExceptionShouldStoreMessage() {
        NotFoundException ex = new NotFoundException("Missing");
        assertEquals("Missing", ex.getMessage());
    }
}