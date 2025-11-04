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

public class AmazonIntegrationTest {

    private Amazon amazon;
    private ShoppingCart cart;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart() {
            private final List<Item> items = new ArrayList<>();

            @Override
            public void add(Item item) {
                items.add(item);
            }

            @Override
            public List<Item> getItems() {
                return items;
            }

            @Override
            public int numberOfItems() {
                return items.size();
            }
        };

        PriceRule discountRule = items -> items.stream()
                .mapToDouble(i -> i.getPricePerUnit() * i.getQuantity())
                .sum() * 0.9;

        amazon = new Amazon(cart, List.of(discountRule));
    }


    // SPECIFICATION-BASED TESTS


    @Test
    @DisplayName("specification-based: calculates discounted total correctly")
    void testDiscountedTotal() {
        cart.add(new Item(ItemType.ELECTRONIC, "Headphones", 1, 100.0));
        cart.add(new Item(ItemType.ELECTRONIC, "Keyboard", 1, 50.0));

        double total = amazon.calculate();
        assertEquals(135.0, total, 0.01);
    }

    @Test
    @DisplayName("specification-based: adds multiple items and total updates")
    void testTotalIncreasesWithItems() {
        cart.add(new Item(ItemType.OTHER, "Mousepad", 1, 10.0));
        double total1 = amazon.calculate();

        cart.add(new Item(ItemType.OTHER, "Monitor Stand", 1, 30.0));
        double total2 = amazon.calculate();

        assertTrue(total2 > total1);
    }


    // STRUCTURAL-BASED TESTS


    @Test
    @DisplayName("structural-based: ensures all items are stored correctly")
    void testCartRetainsItems() {
        cart.add(new Item(ItemType.OTHER, "Mouse", 1, 25.0));
        cart.add(new Item(ItemType.OTHER, "Monitor", 1, 200.0));

        assertEquals(2, cart.numberOfItems());
        assertTrue(cart.getItems().stream().anyMatch(i -> i.getName().equals("Monitor")));
    }

    @Test
    @DisplayName("structural-based: empty cart returns zero total")
    void testEmptyCartTotal() {
        double total = amazon.calculate();
        assertEquals(0.0, total);
    }
}
