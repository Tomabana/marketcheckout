package com.banas.market.checkout.discount

import com.banas.market.checkout.inventory.Item
import spock.lang.Shared
import spock.lang.Specification

class QuantityDiscountSpec extends Specification {

    @Shared
    def itemA = new Item(id: 1, name: "A", barcode: "1")
    @Shared
    def itemB = new Item(id: 2, name: "B", barcode: "2")

    def "check if discount can apply to specified basket"() {
        given: "create AA quantity discount"
        def quantityDiscount = new QuantityDiscount(item: itemA, quantity: 2)

        expect: "Check if discount can apply to items in basket"
        canApply == quantityDiscount.checkIfDiscountCanApply(itemsInBakset)

        where:
        itemsInBakset            | canApply
        new HashMap<>()          | false
        [(itemA): 1, (itemB): 2] | false
        [(itemA): 0]             | false
        [(itemA): 2]             | true
        [(itemA): 3]             | true
    }
}
