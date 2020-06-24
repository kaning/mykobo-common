package com.mykobo.model
import org.joda.time.DateTime
import java.io.Serializable
import java.util.*

data class ChainHeightResponse(val ourHeight: Int, val sourceHeight: Int, val lastSourceSync: DateTime)

data class NewAddressResponse(
    val ownerId: UUID,
    val address: String,
    val createdAt: DateTime
) : Serializable

data class NewSystemAddressResponse(
    val address: String,
    val createdAt: DateTime
) : Serializable

data class TransactionResponse(
    val value: String,
    val transactionId: String,
    val date: Date?
) : Serializable

data class AddressState(
    val balance: String,
    val transactions: List<TransactionResponse>
) : Serializable

data class WalletResponse(
    val ownerId: String,
    val addresses: List<AddressResponse>,
    val totalBalance: String
) : Serializable

data class AddressResponse(
    val address: String,
    val state: AddressState
) : Serializable

data class TransactionCreatedResponse(val id: UUID, val configId: Int?, val message: String) : Serializable

data class TransactionUpdatedResponse(val id: UUID, val message: String) : Serializable

data class SystemWalletResponse(
    val id: String,
    val issuedReceiveAddresses: List<String>,
    val watchedAddresses: List<String>,
    val currentReceiveAddress: String,
    val currentChangeAddress: String,
    val balance: String,
    val received: String,
    val transactionOutputs: List<String>, // ["TXID outputSum"]
    val transactionInputs: List<String> // ["TXID inputSum"]
)

data class NewTransactionResponse(
    val transactionId: String?,
    val comment: String?,
    val value: String
)
