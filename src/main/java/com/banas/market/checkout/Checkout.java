package com.banas.market.checkout;

import com.banas.market.checkout.discount.DiscountService;
import com.banas.market.checkout.discount.model.ManualDiscount;
import com.banas.market.checkout.inventory.Item;
import com.banas.market.checkout.inventory.ItemRepository;
import com.banas.market.checkout.receipt.Receipt;
import com.banas.market.checkout.receipt.ReceiptHistoryRepository;
import com.banas.market.checkout.receipt.entities.ReceiptHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@Scope("prototype")
public class Checkout {

    private static final Logger LOGGER = LoggerFactory.getLogger(Checkout.class);

    private Receipt receipt = new Receipt();
    private boolean paymentDone = false;

    public Checkout() {
        LOGGER.info("Creating new checkout.");
    }

    @Autowired
    private DiscountService discountService;

    @Autowired
    private ReceiptHistoryRepository receiptHistoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    public void startNewReceipt() {
        LOGGER.info("Starting new receipt");
        receipt = new Receipt();
    }

    public Item scanItem(String barcode) {
        LOGGER.info("Scanning item with barCode = {}", barcode);
        Item item = itemRepository.findByBarcode(barcode);
        if (item != null) {
            addItem(item);
        } else {
            LOGGER.warn("Item with barcode = {} not found", barcode);
        }
        return null;
    }

    public void addItem(Item item) {
        LOGGER.info("Adding item to receipt");
        receipt.addItem(item);
        receipt = discountService.applyBestPossibleDiscounts(receipt);
    }

    public void addManualDiscount(ManualDiscount manualDiscount) {
        receipt.getDiscounts().getAppliedManualDiscounts().add(manualDiscount);
        receipt = discountService.applyBestPossibleDiscounts(receipt);
    }

    public void pay() {
        paymentDone = true;
    }

    public Receipt printReceipt() {
        if (paymentDone) {
            LOGGER.info("Printing receipt.");
            LOGGER.info(receipt.toString());
            receiptHistoryRepository.save(new ReceiptHistory(receipt));
        } else {
            LOGGER.warn("The receipt is not paid. Client has to pay receipt before printing it out.");
        }
        return receipt;
    }
}
