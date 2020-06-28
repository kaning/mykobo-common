package com.mykobo.model

import com.mykobo.validator.*
import java.io.Serializable

interface HttpRequestBody

abstract class ValidatingRequest {
    abstract fun validationList(): List<ValidationResult>

    fun isValid() =
        validationList().isNotEmpty() && validationList().map { it.valid }.fold(true, { acc, next -> acc && next })

    val validationMessage: String
        get() =
            validationList()
                .filter { !it.valid }
                .map { it.message }
                .joinToString(prefix = "", separator = "\n")
}

data class AddressListRequest(val addresses: List<String>) : HttpRequestBody, ValidatingRequest() {
    override fun validationList(): List<ValidationResult> = listOf(
        NonEmptyCollectionValidator.validate("addresses", addresses)
    )

}

data class TransactionRequest(
    val from: String,
    val to: String,
    val amount: String,
    val comment: String
) : ValidatingRequest(), HttpRequestBody {
    override fun validationList() = listOf(
        NonEmptyValidator.validate("from", from),
        NonEmptyValidator.validate("to", to),
        NumberFormatValidator.validate("amount", amount)
    )
}

data class SellTransactionRequest(
    val amount: Double
) : ValidatingRequest(), HttpRequestBody {
    override fun validationList(): List<ValidationResult> = listOf(
        NonZeroValidator.validate("amount", amount)
    )
}

data class NewAddressRequest(
    val ownerId: String
) : ValidatingRequest(), Serializable {
    override fun validationList(): List<ValidationResult> = listOf(
        NonEmptyValidator.validate("ownerId", ownerId)
    )
}

data class DeleteAddressRequest(
    val ownerId: String,
    val address: String
) : ValidatingRequest(), Serializable {
    override fun validationList(): List<ValidationResult> = listOf(
        NonEmptyValidator.validate("ownerId", ownerId),
        NonEmptyValidator.validate("address", address)
    )
}

data class CreateCryptoOrderRequest(val reference: String, val btcAddress: String) : ValidatingRequest(), Serializable {
    override fun validationList(): List<ValidationResult> =
        listOf(
            NonEmptyValidator.validate("reference", reference),
            NonEmptyValidator.validate("btcAddress", btcAddress)
        )
}

data class NewWalletRequest(
    val ownerId: String
) : ValidatingRequest(), Serializable, HttpRequestBody {
    override fun validationList(): List<ValidationResult> =
        listOf(
            NonEmptyValidator.validate("ownerId", ownerId)
        )
}

data class FundTransferRequest(
    val sourceWallet: String,
    val to: String,
    val amount: Double,
    val comment: String?
) : ValidatingRequest() {
    override fun validationList(): List<ValidationResult> =
        listOf(
            NonEmptyValidator.validate("to", to),
            NonEmptyValidator.validate("from", sourceWallet),
            NonZeroValidator.validate("amount", amount)
        )
}
