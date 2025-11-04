package org.example;

import org.example.Amazon.Amazon;
import org.example.Amazon.Cost.PriceRule;
import org.example.Amazon.Item;
import org.example.Amazon.ShoppingCart;
import org.example.Amazon.Cost.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    @DisplayName("specification-based: calculates discounted total price correctly")
    void testCalculateTotalWithDiscount() {
        cart.add(new Item(ItemType.ELECTRONIC, "Headphones", 1, 100.0));
        cart.add(new Item(ItemType.ELECTRONIC, "Keyboard", 1, 50.0));

        double total = amazon.calculate();
        assertEquals(135.0, total, 0.01);
    }

    @Test
    @DisplayName("structural-based: ensures cart retains all items")
    void testMultipleAdds() {
        cart.add(new Item(ItemType.OTHER, "Mouse", 1, 25.0));
        cart.add(new Item(ItemType.OTHER, "Monitor", 1, 200.0));

        double total = amazon.calculate();
        assertTrue(total > 0);
        assertEquals(2, cart.getItems().size());
    }
}
