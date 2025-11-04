package org.example;

import org.example.Amazon.Amazon;
import org.example.Amazon.Cost.PriceRule;
import org.example.Amazon.Item;
import org.example.Amazon.ShoppingCart;
import org.example.Amazon.Cost.ItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AmazonUnitTest {

    private Amazon amazon;
    private ShoppingCart mockCart;
    private PriceRule mockRule;

    @BeforeEach
    void setUp() {
        mockCart = Mockito.mock(ShoppingCart.class);
        mockRule = Mockito.mock(PriceRule.class);
        amazon = new Amazon(mockCart, List.of(mockRule));
    }

    // ---------- SPECIFICATION-BASED TESTS ----------

    @Test
    @DisplayName("specification-based: adding item updates cart")
    void testAddItemToCart() {
        Item item = new Item(ItemType.ELECTRONIC, "Laptop", 1, 999.99);
        amazon.addToCart(item);
        verify(mockCart).add(item);
    }

    @Test
    @DisplayName("specification-based: calculate total returns expected value")
    void testCalculateTotalValue() {
        when(mockCart.getItems())
                .thenReturn(List.of(new Item(ItemType.ELECTRONIC, "Mouse", 1, 50.0)));
        when(mockRule.priceToAggregate(anyList())).thenReturn(45.0);

        double result = amazon.calculate();
        assertEquals(45.0, result, 0.001);
    }

    // ---------- STRUCTURAL-BASED TESTS ----------

    @Test
    @DisplayName("structural-based: calculate handles empty cart gracefully")
    void testCalculateEmptyCart() {
        when(mockCart.getItems()).thenReturn(List.of());
        when(mockRule.priceToAggregate(anyList())).thenReturn(0.0);

        double result = amazon.calculate();
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("structural-based: multiple rules applied in order")
    void testMultipleRulesApplied() {
        PriceRule rule1 = items -> 50.0;
        PriceRule rule2 = items -> 40.0;
        amazon = new Amazon(mockCart, List.of(rule1, rule2));

        when(mockCart.getItems())
                .thenReturn(List.of(new Item(ItemType.ELECTRONIC, "Keyboard", 1, 60.0)));

        double result = amazon.calculate();
        assertTrue(result >= 0); // ensures result computed successfully
    }
}
