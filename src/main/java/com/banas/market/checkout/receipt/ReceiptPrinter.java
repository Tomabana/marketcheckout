package com.banas.market.checkout.receipt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Component
@Service
public class ReceiptPrinter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptPrinter.class);

    public void print(Receipt receipt) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Date = ").append(receipt.getTimestamp()).append(System.lineSeparator());
        stringBuilder.append("Items:").append(System.lineSeparator());
        stringBuilder.append(receipt.getItems().entrySet().stream()
                .map(entry -> entry.getKey().getName() + " - " + entry.getValue())
                .collect(Collectors.joining(System.lineSeparator())));
        stringBuilder.append(System.lineSeparator()).append(System.lineSeparator());
        stringBuilder.append("Combined discounts:").append(System.lineSeparator());
        stringBuilder.append(receipt.getDiscounts().getAppliedCombinedDiscounts().stream()
                .map(combinedDiscount -> combinedDiscount.toString()).collect(Collectors.joining(System.lineSeparator())));
        stringBuilder.append(System.lineSeparator()).append(System.lineSeparator());
        stringBuilder.append("Quantity discounts:").append(System.lineSeparator());
        stringBuilder.append(receipt.getDiscounts().getAppliedQuantityDiscounts().stream()
                .map(combinedDiscount -> combinedDiscount.toString()).collect(Collectors.joining(System.lineSeparator())));
        stringBuilder.append(System.lineSeparator()).append(System.lineSeparator());
        stringBuilder.append("Manual discounts:").append(System.lineSeparator());
        stringBuilder.append(receipt.getDiscounts().getAppliedManualDiscounts().stream()
                .map(combinedDiscount -> combinedDiscount.toString()).collect(Collectors.joining(System.lineSeparator())));
        stringBuilder.append(System.lineSeparator()).append(System.lineSeparator());
        stringBuilder.append("Total price without discounts: " + receipt.getTotalPrice() + System.lineSeparator());
        stringBuilder.append("Total discount: ").append(receipt.getTotalDiscount()).append(System.lineSeparator());
        stringBuilder.append("Total price with applied discounts ").append(receipt.getTotalPrice().subtract(receipt.getTotalDiscount()));

        LOGGER.info(stringBuilder.toString());
    }
}
