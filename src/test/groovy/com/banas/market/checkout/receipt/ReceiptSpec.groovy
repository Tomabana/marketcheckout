package com.banas.market.checkout.receipt

import com.banas.market.checkout.discount.CombinedDiscount
import com.banas.market.checkout.discount.DiscountPolicy
import com.banas.market.checkout.discount.ManualDiscount
import com.banas.market.checkout.discount.QuantityDiscount
import com.banas.market.checkout.inventory.Item
import spock.lang.Shared
import spock.lang.Specification

class ReceiptSpec extends Specification {

    @Shared
    def itemA = new Item(barcode: "A", price: 100)
    def discountPolicy = Mock(DiscountPolicy)
    def receipt = new Receipt(discountPolicy)

    def "should add new item and calculate discount"() {
        when:
        receipt.addItem(itemA)

        then:
        receipt.getReceiptItemsWithNotAppliedDiscount().get(itemA) > 0
        receipt.getLastAddedItem() == itemA
        receipt.getTotalPriceIncludingDiscounts() > 0
        1 * discountPolicy.addNewDiscounts(receipt)
    }

    def "should add manual discount"() {
        given:
        def manualDiscount = new ManualDiscount(0.1d)

        when:
        receipt.addItem(itemA)
        receipt.addManualDiscount(manualDiscount)

        then:
        receipt.getTotalPriceIncludingDiscounts() > 0
        receipt.isPercentageDiscountApplied(manualDiscount)
    }

    def "should add combined discount"() {
        given:
        def itemB = new Item(barcode: "B", price: 100)
        def combinedDiscount = new CombinedDiscount(discount: 100, discountItems: Arrays.asList(itemA, itemB))

        when:
        receipt.addItem(itemA)
        receipt.addItem(itemB)
        receipt.addCombinedDiscount(combinedDiscount)

        then: "total price is greater than 0 and items from combined discount are used"
        receipt.getTotalPriceIncludingDiscounts() > 0
        receipt.getReceiptItemsWithNotAppliedDiscount().get(itemA) == 0
        receipt.getReceiptItemsWithNotAppliedDiscount().get(itemB) == 0
    }

    def "should add quantity discount"() {
        given:
        def quantityDiscount = new QuantityDiscount(quantity: 2, item: itemA, price: 100)

        when:
        receipt.addItem(itemA)
        receipt.addItem(itemA)
        receipt.addQuantityDiscount(quantityDiscount)

        then: "total price is greater than 0 and item from quantity discount is used"
        receipt.getTotalPriceIncludingDiscounts() > 0
        receipt.getReceiptItemsWithNotAppliedDiscount().get(itemA) == 0
    }
}
