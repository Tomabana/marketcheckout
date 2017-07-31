package com.banas.market.checkout.discount

import com.banas.market.checkout.inventory.Item
import spock.lang.Shared
import spock.lang.Specification

class CombinedDiscountSpec extends Specification {
    @Shared
    def itemA = new Item(id: 1, name: "A", barcode: "1")
    @Shared
    def itemB = new Item(id: 2, name: "B", barcode: "2")
    @Shared
    def itemC = new Item(id: 3, name: "C", barcode: "3")

    def "check if discount can apply to specified basket"() {
        given: "create AB combined discount"
        def combinedDiscount = new CombinedDiscount()
        combinedDiscount.discountItems = new HashSet<>(Arrays.asList(itemA, itemB))

        expect: "Check if discount can apply to items in basket"
        canApply == combinedDiscount.checkIfDiscountCanApply(itemsInBakset)

        where:
        itemsInBakset            | canApply
        new HashMap<>()          | false
        [(itemA): 1, (itemB): 2] | true
        [(itemC): 1, (itemB): 2] | false
        [(itemA): 0, (itemB): 2] | false
    }
}
