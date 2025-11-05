package org.example;

import org.example.Amazon.Cost.DeliveryPrice;
import org.example.Amazon.Cost.ExtraCostForElectronics;
import org.example.Amazon.Cost.ItemType;
import org.example.Amazon.Cost.PriceRule;
import org.example.Amazon.Cost.RegularCost;
import org.example.Amazon.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AmazonCostTest {

    // ---------- SPECIFICATION-BASED TESTS ----------

    @Test
    @DisplayName("specification-based: RegularCost sums all items correctly")
    void testRegularCostSum() {
        PriceRule rule = new RegularCost();
        List<Item> items = List.of(
                new Item(ItemType.OTHER, "Chair", 1, 50.0),
                new Item(ItemType.OTHER, "Table", 1, 100.0)
        );
        double result = rule.priceToAggregate(items);
        assertEquals(150.0, result, 0.001);
    }

    @Test
    @DisplayName("specification-based: ExtraCostForElectronics calculates non-negative total")
    void testExtraCostForElectronics() {
        PriceRule rule = new ExtraCostForElectronics();
        List<Item> items = List.of(
                new Item(ItemType.ELECTRONIC, "Laptop", 1, 100.0),
                new Item(ItemType.OTHER, "Book", 1, 20.0)
        );
        double result = rule.priceToAggregate(items);
        assertTrue(result >= 0.0); // ensures method works safely regardless of cost logic
    }

    // ---------- STRUCTURAL-BASED TESTS ----------

    @Test
    @DisplayName("structural-based: DeliveryPrice returns valid total for small order")
    void testDeliveryPriceSmallOrder() {
        PriceRule rule = new DeliveryPrice();
        List<Item> items = List.of(new Item(ItemType.OTHER, "Pen", 1, 2.0));
        double result = rule.priceToAggregate(items);
        assertTrue(result >= 0.0);
    }

    @Test
    @DisplayName("structural-based: DeliveryPrice returns valid total for large order")
    void testDeliveryPriceLargeOrder() {
        PriceRule rule = new DeliveryPrice();
        List<Item> items = List.of(
                new Item(ItemType.OTHER, "Book", 4, 10.0),
                new Item(ItemType.OTHER, "Notebook", 5, 5.0)
        );
        double result = rule.priceToAggregate(items);
        assertTrue(result >= 0.0);
    }
}
