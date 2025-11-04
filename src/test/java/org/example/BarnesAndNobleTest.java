package org.example.Barnes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class BarnesAndNobleTest {

    private BookDatabase bookDb;
    private BuyBookProcess process;
    private BarnesAndNoble barnes;

    @BeforeEach
    void setUp() {

        bookDb = new BookDatabase() {
            private final Map<String, Book> books = new HashMap<>();

            {
                books.put("123", new Book("123", 50, 5));
                books.put("456", new Book("456", 30, 2));
                books.put("789", new Book("789", 40, 0)); // out of stock
            }

            @Override
            public Book findByISBN(String ISBN) {
                return books.get(ISBN);
            }
        };

        process = new BuyBookProcess() {
            @Override
            public void buyBook(Book book, int amount) {
                // pretend to process purchase
            }
        };

        barnes = new BarnesAndNoble(bookDb, process);
    }


    // SPECIFICATION-BASED TESTS


    @Test
    @DisplayName("specification-based: calculates total price correctly for available books")
    void testGetPriceForCart_AllAvailable() {
        Map<String, Integer> order = new HashMap<>();
        order.put("123", 3); // fully available
        order.put("456", 1);

        PurchaseSummary summary = barnes.getPriceForCart(order);


        assertEquals(180, summary.getTotalPrice());
        assertTrue(summary.getUnavailable().isEmpty());
    }

    @Test
    @DisplayName("specification-based: handles unavailable book quantities correctly")
    void testGetPriceForCart_PartiallyAvailable() {
        Map<String, Integer> order = new HashMap<>();
        order.put("456", 5);

        PurchaseSummary summary = barnes.getPriceForCart(order);


        assertEquals(60, summary.getTotalPrice());
        assertEquals(3, summary.getUnavailable().get(new Book("456", 30, 2)));
    }

    @Test
    @DisplayName("specification-based: returns null when order map is null")
    void testGetPriceForCart_NullOrder() {
        PurchaseSummary summary = barnes.getPriceForCart(null);
        assertNull(summary);
    }


    // STRUCTURE-BASED TESTS


    @Test
    @DisplayName("structural-based: ensures getPriceForCart iterates through all ISBN keys")
    void testGetPriceForCart_IteratesAllKeys() {
        Map<String, Integer> order = new HashMap<>();
        order.put("123", 1);
        order.put("456", 1);
        order.put("789", 1);

        PurchaseSummary summary = barnes.getPriceForCart(order);

        // Out of stock "789" should appear in unavailable
        assertEquals(80, summary.getTotalPrice()); // (50 + 30)
        assertTrue(summary.getUnavailable().containsKey(new Book("789", 40, 0)));
    }
// for part 2
    @Test
    @DisplayName("structural-based: ensures addUnavailable and addToTotalPrice are called properly")
    void testAddUnavailableAndTotalPriceIntegration() {
        Map<String, Integer> order = new HashMap<>();
        order.put("123", 2); // 2 * 50 = 100
        order.put("789", 1); // unavailable

        PurchaseSummary summary = barnes.getPriceForCart(order);

        assertEquals(100, summary.getTotalPrice());
        assertEquals(1, summary.getUnavailable().size());
    }

    @Test
    @DisplayName("structural-based: ensures returned PurchaseSummary is not modifiable")
    void testGetUnavailableIsUnmodifiable() {
        Map<String, Integer> order = new HashMap<>();
        order.put("123", 1);

        PurchaseSummary summary = barnes.getPriceForCart(order);

        assertThrows(UnsupportedOperationException.class, () -> {
            summary.getUnavailable().put(new Book("999", 10, 1), 1);
        });
    }
}
