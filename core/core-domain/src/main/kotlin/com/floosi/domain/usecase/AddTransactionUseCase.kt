package com.floosi.domain.usecase

import com.floosi.common.Result
import com.floosi.domain.model.CategoryId
import com.floosi.domain.model.Currency
import com.floosi.domain.model.Transaction
import com.floosi.domain.model.TransactionType
import com.floosi.domain.model.WalletId
import com.floosi.domain.repository.TransactionRepository
import com.floosi.domain.repository.WalletRepository
import java.math.BigDecimal
import java.time.LocalDateTime

class AddTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val walletRepository: WalletRepository,
) {

    suspend operator fun invoke(
        type: TransactionType,
        amount: BigDecimal,
        currencyCode: String = Currency.DEFAULT.code,
        walletId: WalletId,
        toWalletId: WalletId? = null,
        categoryId: CategoryId? = null,
        note: String? = null,
        occurredAt: LocalDateTime = LocalDateTime.now(),
        timezone: String = "Africa/Cairo",
        amountInBase: BigDecimal = amount,
    ): Result<Transaction> {
        if (amount <= BigDecimal.ZERO) {
            return Result.error(IllegalArgumentException("Amount must be positive"))
        }

        if (type == TransactionType.EXPENSE) {
            val wallet = walletRepository.getById(walletId.value)
                ?: return Result.error(IllegalArgumentException("Wallet not found"))
            if (wallet.balance < amount) {
                return Result.error(IllegalArgumentException("Insufficient balance"))
            }
        }

        if (type == TransactionType.TRANSFER && toWalletId == null) {
            return Result.error(IllegalArgumentException("Transfer requires a destination wallet"))
        }

        val now = LocalDateTime.now()
        val transaction = Transaction(
            type = type,
            amount = amount,
            currencyCode = currencyCode,
            amountInBase = amountInBase,
            walletId = walletId.value,
            toWalletId = toWalletId?.value,
            categoryId = categoryId?.value,
            note = note ?: "",
            occurredAt = occurredAt,
            timezone = timezone,
            isExcluded = false,
            createdAt = now,
            updatedAt = now,
        )

        val savedId = transactionRepository.insert(transaction)
        val savedTransaction = transaction.copy(id = savedId)

        when (type) {
            TransactionType.EXPENSE -> {
                walletRepository.adjustBalance(walletId.value, amount.negate())
            }
            TransactionType.INCOME -> {
                walletRepository.adjustBalance(walletId.value, amount)
            }
            TransactionType.TRANSFER -> {
                walletRepository.adjustBalance(walletId.value, amount.negate())
                if (toWalletId != null) {
                    walletRepository.adjustBalance(toWalletId.value, amount)
                }
            }
        }

        return Result.success(savedTransaction)
    }
}
