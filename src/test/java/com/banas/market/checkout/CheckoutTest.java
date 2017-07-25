package com.banas.market.checkout;

import com.banas.market.checkout.discount.DiscountService;
import com.banas.market.checkout.discount.model.ManualDiscount;
import com.banas.market.checkout.inventory.Item;
import com.banas.market.checkout.inventory.ItemRepository;
import com.banas.market.checkout.receipt.Receipt;
import com.banas.market.checkout.receipt.ReceiptHistoryRepository;
import com.banas.market.checkout.receipt.entities.ReceiptHistory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutTest {

    @Mock
    private DiscountService discountService;

    @Mock
    private ReceiptHistoryRepository receiptHistoryRepository;

    @Mock
    private ItemRepository itemRepository;

    private ArgumentCaptor<Receipt> receiptArgumentCaptor = ArgumentCaptor.forClass(Receipt.class);

    @InjectMocks
    private Checkout checkout = new Checkout();

    @Test
    public void scanItemWithNonExistingBarcode() {
        String barCode = "123";
        when(itemRepository.findByBarcode(barCode)).thenReturn(null);

        checkout.scanItem(barCode);

        verify(itemRepository, times(1)).findByBarcode(barCode);
        verify(discountService, times(0)).applyBestPossibleDiscounts(receiptArgumentCaptor.capture());
    }

    @Test
    public void scanItemWithExistingBarcode() {
        String barCode = "123";
        Item item = new Item();
        item.setPrice(BigDecimal.ONE);
        when(itemRepository.findByBarcode(barCode)).thenReturn(item);

        checkout.scanItem(barCode);

        verify(itemRepository, times(1)).findByBarcode(barCode);
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

        verify(itemRepository, times(0)).findByBarcode(barCode);
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

        verify(receiptHistoryRepository, times(0)).save(any(ReceiptHistory.class));

        checkout.pay();
        checkout.printReceipt();

        verify(receiptHistoryRepository, times(1)).save(any(ReceiptHistory.class));
    }

}