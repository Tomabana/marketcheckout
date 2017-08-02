package com.banas.market.checkout.discount

import com.banas.market.checkout.inventory.Item
import com.banas.market.checkout.inventory.ItemRepository
import com.banas.market.checkout.receipt.Receipt
import spock.lang.Shared
import spock.lang.Specification

class DiscountPolicySpec extends Specification {

    @Shared
    def itemA = new Item(id: 1, name: "A", barcode: "1")
    @Shared
    def itemB = new Item(id: 2, name: "B", barcode: "2")
    @Shared
    def itemC = new Item(id: 3, name: "C", barcode: "3")
    @Shared
    def combinedDiscountAB = new CombinedDiscount(discountItems: Arrays.asList(itemA, itemB))
    @Shared
    def combinedDiscountAC = new CombinedDiscount(discountItems: Arrays.asList(itemA, itemC))
    @Shared
    def quantityDiscount = new QuantityDiscount(item: itemA, quantity: 2)

    def "proper discounts should be added to receipt"() {
        given:
        itemA.quantityDiscounts = Arrays.asList(quantityDiscount)
        itemA.combinedDiscounts = Arrays.asList(combinedDiscountAB, combinedDiscountAC)
        def discountPolicy = new DiscountPolicy(itemRepository: Mock(ItemRepository))
        def receipt = Mock(Receipt)

        when:
        discountPolicy.itemRepository.findByIdWithDiscounts(itemA.id) >> itemA
        receipt.getLastAddedItem() >> itemA
        receipt.getReceiptItemsWithNotAppliedDiscount() >> [(itemA): 3, (itemB): 1]
        receipt.getTotalPriceIncludingDiscounts() >> 100
        discountPolicy.addNewDiscounts(receipt)

        then:
        1 * receipt.addCombinedDiscount(combinedDiscountAB)
        1 * receipt.addQuantityDiscount(quantityDiscount)
        0 * receipt.addCombinedDiscount(combinedDiscountAC)
    }
}