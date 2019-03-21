package com.telleroo

import com.fasterxml.jackson.core.JsonParseException
import spock.lang.Specification

import java.time.Clock
import java.util.concurrent.Future

class WebhookCallProcessorSpec extends Specification {
    private static final String COMPANY_APPROVED_PAYLOAD =
    '''
        {
            "webhook": {
                "type": "Company Approved",
                "company_id": "538633c5-d120-45fa-9ee5-1c6a348232",
                "company_name": "Test Company One"
            }
        }
    '''

    def "I can process a new credit"() {
        given:
            WebhookCallProcessor processor = new WebhookCallProcessor(1)
            processor.addListener(new WebhookListener() {})
            String payload = '''
                    {
                      "webhook": {
                        "type": "New Credit",
                        "transaction_id": "129633c5-a230-32ef-7de8-2b7a459161",
                        "processed_at": "2016-12-01T12:18:23.372Z",
                        "amount": 100,
                        "currency_code": "GBP",
                        "account_id": "ed5af7d2-741c-4905-a3ba-66d332d604",
                        "sender_name": "Daryl Oates",
                        "reference": "Funding Telleroo",
                        "end_balance": 2300,
                        "created_at": "2016-12-02T12:15:22.486Z",
                        "updated_at": "2016-12-02T12:15:22.486Z"
                      }
                    }
           '''

        when:
        Transaction result
        processor.addListener(new WebhookListener() {
            @Override
            void newCredit(Transaction transaction) {
                result = transaction
            }
        })
        processor.process(payload).get()

        then:
        result != null
        result.transactionId == '129633c5-a230-32ef-7de8-2b7a459161'
    }

    def "I can process a failed payment"() {
        given:
        WebhookCallProcessor processor = new WebhookCallProcessor(1)
        processor.addListener(new WebhookListener() {})
        String payload = '''
                    {
                      "webhook": {
                        "type": "Failed Payment",
                        "transaction_id": "538633c5-d120-45fa-9ee5-1c6a348232",
                        "processed_at": "2016-12-01T12:18:23.372Z",
                        "amount": 5000,
                        "currency_code": "GBP",
                        "account_id": "ed5af7d2-741c-4905-a3ba-66d332d604",
                        "reference": "Withdrawal Telleroo",
                        "tag": "",
                        "reconciliation": "",
                        "status_info": "Transaction failed",
                        "end_balance": 2300,
                        "created_at": "2016-12-02T12:15:22.486Z",
                        "updated_at": "2016-12-02T12:15:22.486Z"
                      }
                    }
           '''

        when:
        Transaction result
        processor.addListener(new WebhookListener() {
            @Override
            void failedPayment(Transaction transaction) {
                result = transaction
            }
        })
        processor.process(payload).get()

        then:
        result != null
        result.transactionId == '538633c5-d120-45fa-9ee5-1c6a348232'
    }

    def "I can process a sent payment"() {
        given:
        WebhookCallProcessor processor = new WebhookCallProcessor(1)
        processor.addListener(new WebhookListener() {})
        String payload = '''
                    {
                      "webhook": {
                        "type": "Sent Payment",
                        "transaction_id": "538633c5-d120-45fa-9ee5-1c6a348232",
                        "processed_at": "2016-12-01T12:18:23.372Z",
                        "amount": 5000,
                        "currency_code": "GBP",
                        "recipient_id": "638633c5-d120-15fa-9ee5-7c6a348266",
                        "account_id": "ed5af7d2-741c-4905-a3ba-66d332d604",
                        "reference": "Withdrawal Telleroo",
                        "end_balance": 2300,
                        "idempotent_key": "418633c5-1120-45fa-9ee5-1c6a348232",
                        "created_at": "2016-12-02T12:15:22.486Z",
                        "updated_at": "2016-12-02T12:15:22.486Z"
                      }
                    }
           '''

        when:
        Transaction result
        processor.addListener(new WebhookListener() {
            @Override
            void sentPayment(Transaction transaction) {
                result = transaction
            }
        })
        processor.process(payload).get()

        then:
        result != null
        result.transactionId == '538633c5-d120-45fa-9ee5-1c6a348232'
    }

    def "I can process an approved company"() {
        given:
        WebhookCallProcessor processor = new WebhookCallProcessor(1)
        processor.addListener(new WebhookListener() {})

        when:
        Company result
        processor.addListener(new WebhookListener() {
            @Override
            void companyApproved(Company company) {
                result = company
            }
        })
        processor.process(COMPANY_APPROVED_PAYLOAD).get()

        then:
        result != null
        result.companyId == '538633c5-d120-45fa-9ee5-1c6a348232'
    }

    def "I can handle parsing errors"() {
        given:
        WebhookCallProcessor processor = new WebhookCallProcessor(1)
        processor.addListener(new WebhookListener() {})
        String payload = '{invalid"json}'


        when:
        Exception resultException
        String resultPayload
        processor.addListener(new WebhookListener() {
            @Override
            void error(Exception e, String received) {
                resultException = e
                resultPayload = received
            }
        })
        processor.process(payload).get()


        then:
        resultException != null
        resultException.class == JsonParseException
        resultPayload != null
        resultPayload == payload
    }

    def "I can handle listener errors"() {
        given:
        WebhookCallProcessor processor = new WebhookCallProcessor(1)
        processor.addListener(new WebhookListener() {})

        when:
        Exception resultException
        String resultPayload
        processor.addListener(new WebhookListener() {
            @Override
            void companyApproved(Company company) {
                throw new IllegalStateException()
            }
            @Override
            void error(Exception e, String received) {
                resultException = e
                resultPayload = received
            }
        })
        processor.process(COMPANY_APPROVED_PAYLOAD).get()


        then:
        resultException != null
        resultException.class == IllegalStateException
        resultPayload != null
        resultPayload == COMPANY_APPROVED_PAYLOAD
    }

    def "I can handle error listener errors"() {
        given:
        WebhookCallProcessor processor = new WebhookCallProcessor(1)
        processor.addListener(new WebhookListener() {})

        when:
        Company result
        Exception exceptionResult
        processor.addListener(new WebhookListener() {
            @Override
            void companyApproved(Company company) {
                result = company
                throw new IllegalStateException()
            }

            @Override
            void error(Exception e, String received) {
                exceptionResult = e
                throw new IllegalStateException()
            }
        })
        Object o = processor.process(COMPANY_APPROVED_PAYLOAD).get()


        then:
        o == null
        result != null
        exceptionResult != null
    }

    def "The default number of threads is 5"() {
        given:
        WebhookCallProcessor processor = new WebhookCallProcessor()
        processor.addListener(new WebhookListener() {})

        when:
        processor.addListener(new WebhookListener() {
            @Override
            void companyApproved(Company company) {
                Thread.sleep(1000)
            }
            @Override
            void error(Exception e, String received) {
                e.printStackTrace()
            }
        })

        long startTime = -Clock.systemUTC().millis()
        List<Future> futures = (0..4).collect {
            processor.process(COMPANY_APPROVED_PAYLOAD)
        }
        Future future = processor.process(COMPANY_APPROVED_PAYLOAD)

        then:
        (startTime + Clock.systemUTC().millis()) < 1000

        when:
        futures.each { it.get() }

        then:
        (startTime + Clock.systemUTC().millis()) >= 1000
        (startTime + Clock.systemUTC().millis()) < 5000

        when:
        future.get()

        then:
        (startTime + Clock.systemUTC().millis()) >= 2000
    }

    def "I can add and remove multiple listeners"() {
        given:
        WebhookCallProcessor processor = new WebhookCallProcessor()
        processor.addListener(new WebhookListener() {})

        when:
        Company company1
        Company company2
        WebhookListener l1 = new WebhookListener() {
            @Override
            void companyApproved(Company company) {
                company1 = company
            }
        }

        WebhookListener l2 = new WebhookListener() {
            @Override
            void companyApproved(Company company) {
                company2 = company
            }
        }

        processor.addListener(l1)
        processor.addListener(l2)
        processor.process(COMPANY_APPROVED_PAYLOAD).get()

        then:
        company1 != null
        company2 != null
        company1.companyId == '538633c5-d120-45fa-9ee5-1c6a348232'
        company2.companyId == '538633c5-d120-45fa-9ee5-1c6a348232'

        when:
        company1 = null
        company2 = null

        processor.removeListener(l2)
        processor.process(COMPANY_APPROVED_PAYLOAD).get()

        then:
        company1 != null
        company2 == null
        company1.companyId == '538633c5-d120-45fa-9ee5-1c6a348232'
    }
}
