package com.mykobo.validator

import com.mykobo.model.AddressListRequest
import com.mykobo.model.TransactionRequest
import com.mykobo.model.ValidatingRequest
import junit.framework.TestCase.assertEquals
import org.junit.Test


class ValidatorTest {

    data class EmailTest(val email: String) : ValidatingRequest() {
        override var validationList: List<ValidationResult> =
            listOf(EmailValidator.validate("email", email))
    }

    @Test
    fun `Email should validate`() {
        val validEmailTest = EmailTest("valid@email.address")
        val invalidEmailTest = EmailTest("invalidemail")
        assert(validEmailTest.isValid())
        assert(!invalidEmailTest.isValid())
        assertEquals("email is not a valid email address", invalidEmailTest.validationMessage)
    }

    @Test
    fun `Collections should validate`() {
        val validAddressListRequest = AddressListRequest(listOf("address1", "address2"))
        val invalidAddressList = AddressListRequest(emptyList())
        assert(validAddressListRequest.isValid())
        assert(!invalidAddressList.isValid())
        assertEquals("This collection [addresses] cannot be empty", invalidAddressList.validationMessage)
    }

    @Test
    fun `multiple fields should validate`() {
        val transactionRequest =
            TransactionRequest(to = "string", from = "string", amount = "10.00", comment = "some comment")
        val invalidAmountRequest = transactionRequest.copy(amount = "invalid")
        val invalidToRequest = transactionRequest.copy(to = "")
        val multipleInvalid = transactionRequest.copy(from = "", to = "", amount = "")
        assert(transactionRequest.isValid())
        assert(!invalidAmountRequest.isValid())
        assertEquals("amount is not an acceptable number", invalidAmountRequest.validationMessage)

        assert(!invalidToRequest.isValid())
        assertEquals("[to] cannot be empty", invalidToRequest.validationMessage)

        assert(!multipleInvalid.isValid())
        assertEquals(
            "[from] cannot be empty\n[to] cannot be empty\namount is not an acceptable number",
            multipleInvalid.validationMessage
        )

    }
}