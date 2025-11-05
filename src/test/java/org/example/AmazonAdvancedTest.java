package org.example;

import org.example.Amazon.Amazon;
import org.example.Amazon.Cost.ItemType;
import org.example.Amazon.Cost.PriceRule;
import org.example.Amazon.Item;
import org.example.Amazon.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AmazonAdvancedTest {

    private Amazon amazon;
    private ShoppingCart mockCart;
    private PriceRule mockRule;

    @BeforeEach
    void setUp() {
        mockCart = mock(ShoppingCart.class);
        mockRule = mock(PriceRule.class);
        amazon = new Amazon(mockCart, List.of(mockRule));
    }

    // ---------- specification-based tests ----------

    @Test
    @DisplayName("specification-based: calculate applies all rules correctly")
    void testMultipleRulesApplied() {
        PriceRule rule1 = items -> 50.0;
        PriceRule rule2 = items -> 25.0;
        Amazon amazonMulti = new Amazon(mockCart, List.of(rule1, rule2));
        when(mockCart.getItems()).thenReturn(List.of(new Item(ItemType.OTHER, "Pen", 1, 5.0)));
        double result = amazonMulti.calculate();
        assertEquals(75.0, result, 0.01);
    }

    @Test
    @DisplayName("specification-based: empty cart returns zero total")
    void testEmptyCartCalculation() {
        when(mockCart.getItems()).thenReturn(List.of());
        when(mockRule.priceToAggregate(anyList())).thenReturn(0.0);
        double result = amazon.calculate();
        assertEquals(0.0, result);
    }

    // ---------- structural-based tests ----------

    @Test
    @DisplayName("structural-based: adding multiple items updates total")
    void testAddMultipleItemsUpdatesTotal() {
        ShoppingCart realCart = new ShoppingCart() {
            private final List<Item> items = new ArrayList<>();
            @Override
            public void add(Item item) { items.add(item); }
            @Override
            public List<Item> getItems() { return items; }
            @Override
            public int numberOfItems() { return items.size(); }
        };

        PriceRule rule = items -> items.stream().mapToDouble(i -> i.getPricePerUnit()).sum();
        Amazon amazonReal = new Amazon(realCart, List.of(rule));

        realCart.add(new Item(ItemType.OTHER, "Mouse", 1, 25.0));
        double total1 = amazonReal.calculate();

        realCart.add(new Item(ItemType.OTHER, "Monitor", 1, 200.0));
        double total2 = amazonReal.calculate();

        assertTrue(total2 > total1);
    }

    @Test
    @DisplayName("structural-based: cart correctly stores all added items")
    void testCartRetainsItems() {
        ShoppingCart realCart = new ShoppingCart() {
            private final List<Item> items = new ArrayList<>();
            @Override
            public void add(Item item) { items.add(item); }
            @Override
            public List<Item> getItems() { return items; }
            @Override
            public int numberOfItems() { return items.size(); }
        };

        PriceRule rule = items -> items.size() * 10.0;
        Amazon amazonReal = new Amazon(realCart, List.of(rule));

        realCart.add(new Item(ItemType.OTHER, "Keyboard", 1, 30.0));
        realCart.add(new Item(ItemType.ELECTRONIC, "Headphones", 1, 100.0));

        assertEquals(2, realCart.numberOfItems());
        assertTrue(realCart.getItems().stream().anyMatch(i -> i.getName().equals("Headphones")));
    }
}
