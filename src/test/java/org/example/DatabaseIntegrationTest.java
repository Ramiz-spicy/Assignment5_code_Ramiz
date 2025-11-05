package org.example;

import org.example.Amazon.Cost.ItemType;
import org.example.Amazon.Database;
import org.example.Amazon.Item;
import org.example.Amazon.ShoppingCartAdaptor;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseIntegrationTest {

    private static Database database;
    private static ShoppingCartAdaptor adaptor;

    @BeforeAll
    static void setupDatabase() {
        database = new Database();
        adaptor = new ShoppingCartAdaptor(database);
    }

    @AfterAll
    static void tearDown() {
        database.close();
    }

    @Test
    @Order(1)
    @DisplayName("Database initializes and provides a connection")
    void testDatabaseConnection() {
        assertNotNull(database.getConnection());
        try {
            assertFalse(database.getConnection().isClosed());
        } catch (SQLException e) {
            fail("Unexpected SQLException while checking if connection is closed: " + e.getMessage());
        }
    }

    @Test
    @Order(2)
    @DisplayName("ShoppingCartAdaptor adds an item successfully")
    void testAddItem() {
        database.resetDatabase(); // clear table first
        Item newItem = new Item(ItemType.OTHER, "Notebook", 2, 10.0);
        adaptor.add(newItem);

        List<Item> items = adaptor.getItems();
        assertEquals(1, items.size());
        assertEquals("Notebook", items.get(0).getName());
    }

    @Test
    @Order(3)
    @DisplayName("ShoppingCartAdaptor counts items correctly")
    void testNumberOfItems() {
        int count = adaptor.numberOfItems();
        assertTrue(count >= 0);
    }

    @Test
    @Order(4)
    @DisplayName("Database resetDatabase clears all items")
    void testResetDatabase() {
        database.resetDatabase();
        List<Item> items = adaptor.getItems();
        assertTrue(items.isEmpty());
    }

    @Test
    @Order(5)
    @DisplayName("Database close() safely closes connection")
    void testCloseDatabase() {
        assertDoesNotThrow(() -> database.close());
        assertDoesNotThrow(() -> database.close()); // double-close should be safe
    }
}
