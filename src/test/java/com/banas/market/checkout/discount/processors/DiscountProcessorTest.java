package com.banas.market.checkout.discount.processors;

import com.banas.market.checkout.inventory.Item;
import com.banas.market.checkout.inventory.ItemService;
import com.banas.market.checkout.receipt.Receipt;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DiscountProcessorTest {

    private Receipt receipt = new Receipt();

    @Spy
    @SuppressWarnings("unused")
    private ManualDiscountProcessor manualDiscountProcessor = new ManualDiscountProcessor();

    @Spy
    @SuppressWarnings("unused")
    private CombinedDiscountProcessor combinedDiscountProcessor = new CombinedDiscountProcessor();

    @Spy
    @SuppressWarnings("unused")
    private QuantityDiscountProcessor quantityDiscountProcessor = new QuantityDiscountProcessor();

    @Mock
    private ItemService itemService;

    @InjectMocks
    private DiscountProcessor discountProcessor = new DiscountProcessor();

    @Before
    public void setUp() {
        when(itemService.getItemWithDiscounts(1L)).thenReturn(Optional.of(DataGenerator.ITEM_A));
        when(itemService.getItemWithDiscounts(2L)).thenReturn(Optional.of(DataGenerator.ITEM_B));
        when(itemService.getItemWithDiscounts(3L)).thenReturn(Optional.of(DataGenerator.ITEM_C));
        when(itemService.getItemWithDiscounts(4L)).thenReturn(Optional.of(DataGenerator.ITEM_D));
    }


    @Test
    public void applyBestPossibleDiscountsFirstScenario() {
        List<Item> items = Arrays.asList(DataGenerator.ITEM_A, DataGenerator.ITEM_A, DataGenerator.ITEM_A,
                DataGenerator.ITEM_B, DataGenerator.ITEM_A, DataGenerator.ITEM_C, DataGenerator.ITEM_D,
                DataGenerator.ITEM_A, DataGenerator.ITEM_A, DataGenerator.ITEM_A);
        items.stream().forEach(item -> receipt = addItemAndCalculateDiscount(item));

        validateAmounts(receipt, BigDecimal.valueOf(515), BigDecimal.valueOf(200));
    }

    @Test
    public void applyBestPossibleDiscountsSecondScenario() {
        List<Item> items = Arrays.asList(DataGenerator.ITEM_B, DataGenerator.ITEM_B, DataGenerator.ITEM_A,
                DataGenerator.ITEM_A);

        items.stream().forEach(item -> receipt = addItemAndCalculateDiscount(item));
        validateAmounts(receipt, BigDecimal.valueOf(300), BigDecimal.valueOf(40));
    }

    @Test
    public void applyBestPossibleDiscountsWithDefaultPercentageDiscount() {
        List<Item> items = Arrays.asList(DataGenerator.ITEM_B, DataGenerator.ITEM_A, DataGenerator.ITEM_B,
                DataGenerator.ITEM_B, DataGenerator.ITEM_B, DataGenerator.ITEM_B, DataGenerator.ITEM_B,
                DataGenerator.ITEM_A, DataGenerator.ITEM_B);
        items.stream().forEach(item -> receipt = addItemAndCalculateDiscount(item));

        validateAmounts(receipt, BigDecimal.valueOf(800), BigDecimal.valueOf(175));
    }

    private void validateAmounts(Receipt receipt, BigDecimal expectedTotalPrice, BigDecimal expectedTotalDiscount) {
        Assert.assertTrue("Proper price without discounts ",
                receipt.getTotalPrice().compareTo(expectedTotalPrice) == 0);
        Assert.assertTrue("Proper total discount ",
                receipt.getTotalDiscount().compareTo(expectedTotalDiscount) == 0);
    }

    private Receipt addItemAndCalculateDiscount(Item item) {
        receipt.addItem(item);
        return discountProcessor.applyBestPossibleDiscounts(receipt);
    }

}