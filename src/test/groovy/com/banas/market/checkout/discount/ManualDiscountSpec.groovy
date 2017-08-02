package com.banas.market.checkout.discount

import spock.lang.Specification

class ManualDiscountSpec extends Specification {

    def "check if percentage discount can apply"() {
        given:
        def percentageDiscount = new ManualDiscount(0.1d)

        expect:
        percentageDiscount.getDiscount(100) > 0
        percentageDiscount.getDiscount(0) == 0
    }

    def "check if amount discount can apply"() {
        given:
        def amountDiscount = new ManualDiscount(BigDecimal.valueOf(100))

        expect:
        amountDiscount.getDiscount(100) > 0
        amountDiscount.getDiscount(0) == 0
    }

    def "check if 0 percentage discount can be created"() {
        when:
        new ManualDiscount(0d)

        then:
        thrown(IllegalArgumentException)
    }
}
