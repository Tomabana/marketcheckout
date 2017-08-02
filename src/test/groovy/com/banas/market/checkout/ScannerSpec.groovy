package com.banas.market.checkout

import com.banas.market.checkout.inventory.Item
import com.banas.market.checkout.inventory.ItemRepository
import spock.lang.Shared
import spock.lang.Specification

class ScannerSpec extends Specification {

    @Shared
    def item = new Item(barcode: "A")
    def itemRepository = Mock(ItemRepository)
    def scanner = new Scanner(itemRepository: itemRepository)

    def "should return founded item"() {
        setup:
        itemRepository.findByBarcode(item.barcode) >> item

        expect:
        foundedItem == scanner.scanItem(barCode)

        where:
        foundedItem | barCode
        null        | "B"
        item        | item.barcode
    }
}
