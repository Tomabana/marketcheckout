package com.banas.market.checkout;

import com.banas.market.checkout.discount.DiscountService;
import com.banas.market.checkout.discount.model.ManualDiscount;
import com.banas.market.checkout.inventory.Item;
import com.banas.market.checkout.inventory.ItemService;
import com.banas.market.checkout.payment.IPayment;
import com.banas.market.checkout.receipt.Receipt;
import com.banas.market.checkout.receipt.ReceiptHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Component
@Service
@Scope("prototype")
public class Checkout implements ICheckout {

    private static final Logger LOGGER = LoggerFactory.getLogger(Checkout.class);

    private Receipt receipt = new Receipt();
    private boolean paymentDone = false;

    public Checkout() {
        LOGGER.info("Creating new checkout.");
    }

    @Autowired
    private DiscountService discountService;

    @Autowired
    private ReceiptHistoryService receiptHistoryService;

    @Autowired
    private ItemService itemService;

    @Override
    public void startNewReceipt() {
        LOGGER.info("Starting new receipt");
        receipt = new Receipt();
    }

    @Override
    public Item scanItem(String barcode) {
        LOGGER.info("Scanning item with barCode = {}", barcode);
        Optional<Item> item = itemService.getByBarcode(barcode);
        if (item.isPresent()) {
            addItem(item.get());
        } else {
            LOGGER.warn("Item with barcode = {} not found", barcode);
        }
        return null;
    }

    @Override
    public void addItem(Item item) {
        LOGGER.info("Adding item to receipt");
        receipt.addItem(item);
        receipt = discountService.applyBestPossibleDiscounts(receipt);
    }

    @Override
    public void addManualDiscount(ManualDiscount manualDiscount) {
        receipt.getDiscounts().getAppliedManualDiscounts().add(manualDiscount);
        receipt = discountService.applyBestPossibleDiscounts(receipt);
    }

    @Override
    public void pay(IPayment payment) {
        payment.pay(receipt);
        paymentDone = true;
    }

    @Override
    public Receipt printReceipt() {
        if (paymentDone) {
            LOGGER.info("Printing receipt.");
            LOGGER.info(receipt.toString());
            receiptHistoryService.save(receipt);
        } else {
            LOGGER.warn("The receipt is not paid. Client has to pay receipt before printing it out.");
        }
        return receipt;
    }
}
