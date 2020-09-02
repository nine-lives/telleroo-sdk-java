package com.telleroo

import spock.lang.Specification
import spock.lang.Unroll

class TransactionStateSpec extends Specification {
    @Unroll("I can get the correct state from the transmission string - #transmissionValue")
    def "I can get the correct state from the transmission string"() {
        when:
            TransactionState state = TransactionState.fromTransmissionValue(transmissionValue)
            TransactionState state2 = TransactionState.fromTransmissionValue(expected.name())
        then:
            expected == state
            expected == state2

        where:
            transmissionValue | expected
            "Preparing Payment" | TransactionState.PreparingPayment
            "Payment Sent" | TransactionState.PaymentSent
            "On Hold" | TransactionState.OnHold
            "Waiting for approval" | TransactionState.WaitingForApproval
            "Waiting for funds" | TransactionState.WaitingForFunds
            "Looking into it" | TransactionState.LookingIntoIt
            "Credit" | TransactionState.Credit
            "Failed" | TransactionState.Failed
            "Cancelled" | TransactionState.Cancelled
    }

}
