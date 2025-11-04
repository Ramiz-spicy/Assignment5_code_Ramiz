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

    @Test
    @DisplayName("specification-based: adding item updates cart")
    void testAddItemToCart() {
        Item item = new Item(ItemType.ELECTRONIC, "Laptop", 1, 999.99);
        amazon.addToCart(item);
        verify(mockCart).add(item);
    }

    @Test
    @DisplayName("structural-based: calculate price applies rules correctly")
    void testCalculatePrice() {
        when(mockCart.getItems())
                .thenReturn(List.of(new Item(ItemType.ELECTRONIC, "Book", 1, 20.0)));
        when(mockRule.priceToAggregate(anyList()))
                .thenReturn(18.0);

        double result = amazon.calculate();
        assertEquals(18.0, result);
    }
}
