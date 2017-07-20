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
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DiscountProcessorTest {

    private Receipt receipt = new Receipt();
    private DataGenerator dataGenerator = new DataGenerator();

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
        when(itemService.getItemWithDiscounts(1L))
                .thenReturn(Optional.of(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_A)));
        when(itemService.getItemWithDiscounts(2L))
                .thenReturn(Optional.of(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_B)));
        when(itemService.getItemWithDiscounts(3L))
                .thenReturn(Optional.of(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_C)));
        when(itemService.getItemWithDiscounts(4L))
                .thenReturn(Optional.of(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_D)));
    }


    @Test
    public void applyBestPossibleDiscountsFirstScenario() {
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_A));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_A));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_A));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_B));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_A));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_C));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_D));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_A));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_A));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_A));


        validateAmounts(receipt, BigDecimal.valueOf(515), BigDecimal.valueOf(200));
    }

    @Test
    public void applyBestPossibleDiscountsSecondScenario() {
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_B));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_B));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_A));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_A));

        validateAmounts(receipt, BigDecimal.valueOf(300), BigDecimal.valueOf(40));
    }

    @Test
    public void applyBestPossibleDiscountsWithDefaultPercentageDiscount() {
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_B));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_A));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_B));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_B));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_B));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_B));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_B));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_A));
        receipt = addItemAndCalculateDiscount(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_B));

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