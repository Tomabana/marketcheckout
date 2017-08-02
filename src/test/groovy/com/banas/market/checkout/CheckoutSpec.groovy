package com.banas.market.checkout

import com.banas.market.checkout.discount.ManualDiscount
import com.banas.market.checkout.receipt.Receipt
import com.banas.market.checkout.receipt.ReceiptFactory
import com.banas.market.checkout.receipt.ReceiptRepository
import spock.lang.Specification

class CheckoutSpec extends Specification {

    def receiptRepository = Mock(ReceiptRepository)
    def receiptFactory = Mock(ReceiptFactory)
    def receipt = Mock(Receipt)
    def checkout = new Checkout(receiptFactory: receiptFactory, receiptRepository: receiptRepository)

    def "should create new receipt"() {
        when:
        checkout.startNewReceipt()

        then:
        1 * receiptFactory.createReceipt()
    }

    def "should add new manual discount to receipt"() {
        given:
        def manualDiscount = new ManualDiscount(0.1d)

        when:
        receiptFactory.createReceipt() >> receipt
        checkout.startNewReceipt()
        checkout.addManualDiscount(manualDiscount)

        then:
        1 * receipt.addManualDiscount(manualDiscount)
    }

    def "should not print receipt before payment"() {
        when:
        receiptFactory.createReceipt() >> receipt
        checkout.startNewReceipt()
        checkout.printReceipt()

        then:
        0 * receipt.print()
        0 * receiptRepository.save(receipt)
    }

    def "should print receipt after payment"() {
        when:
        receiptFactory.createReceipt() >> receipt
        checkout.startNewReceipt()
        checkout.pay()
        checkout.printReceipt()

        then:
        1 * receipt.print()
        1 * receiptRepository.save(receipt)
    }
}
