package com.banas.market.checkout;

import com.banas.market.checkout.discount.DiscountService;
import com.banas.market.checkout.discount.model.ManualDiscount;
import com.banas.market.checkout.inventory.Item;
import com.banas.market.checkout.inventory.ItemService;
import com.banas.market.checkout.payment.CashPaymentService;
import com.banas.market.checkout.receipt.Receipt;
import com.banas.market.checkout.receipt.ReceiptHistoryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutTest {

    @Mock
    private DiscountService discountService;

    @Mock
    private ReceiptHistoryService receiptHistoryService;

    @Mock
    private ItemService itemService;

    private ArgumentCaptor<Receipt> receiptArgumentCaptor = ArgumentCaptor.forClass(Receipt.class);

    @InjectMocks
    private Checkout checkout = new Checkout();

    @Test
    public void scanItemWithNonExistingBarcode() {
        String barCode = "123";
        when(itemService.getByBarcode(barCode)).thenReturn(Optional.empty());

        checkout.scanItem(barCode);

        verify(itemService, times(1)).getByBarcode(barCode);
        verify(discountService, times(0)).applyBestPossibleDiscounts(receiptArgumentCaptor.capture());
    }

    @Test
    public void scanItemWithExistingBarcode() {
        String barCode = "123";
        Item item = new Item();
        item.setPrice(BigDecimal.ONE);
        when(itemService.getByBarcode(barCode)).thenReturn(Optional.of(item));

        checkout.scanItem(barCode);

        verify(itemService, times(1)).getByBarcode(barCode);
        verify(discountService, times(1)).applyBestPossibleDiscounts(receiptArgumentCaptor.capture());
        Assert.assertTrue(receiptArgumentCaptor.getValue().getItems().get(item) == 1);
    }

    @Test
    public void addItem() {
        String barCode = "123";
        Item item = new Item();
        item.setBarcode(barCode);
        item.setPrice(BigDecimal.ONE);

        checkout.addItem(item);

        verify(itemService, times(0)).getByBarcode(barCode);
        verify(discountService, times(1)).applyBestPossibleDiscounts(receiptArgumentCaptor.capture());
        Assert.assertTrue(receiptArgumentCaptor.getValue().getItems().get(item) == 1);
    }

    @Test
    public void addManualDiscount() {
        ManualDiscount manualDiscount = new ManualDiscount(BigDecimal.ONE);

        checkout.addManualDiscount(manualDiscount);

        verify(discountService, times(1)).applyBestPossibleDiscounts(receiptArgumentCaptor.capture());
        Assert.assertTrue(receiptArgumentCaptor.getValue().getDiscounts().getAppliedManualDiscounts().contains
                (manualDiscount));
    }

    @Test
    public void printReceipt() {
        checkout.printReceipt();

        verify(receiptHistoryService, times(0)).save(any());

        checkout.pay(new CashPaymentService());
        checkout.printReceipt();

        verify(receiptHistoryService, times(1)).save(any());
    }

}