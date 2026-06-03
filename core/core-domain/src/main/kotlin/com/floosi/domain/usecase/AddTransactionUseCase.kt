package com.floosi.domain.usecase

import com.floosi.common.Clock
import com.floosi.common.Result
import com.floosi.domain.model.CategoryId
import com.floosi.domain.model.Currency
import com.floosi.domain.model.Transaction
import com.floosi.domain.model.TransactionId
import com.floosi.domain.model.TransactionType
import com.floosi.domain.model.WalletId
import com.floosi.domain.repository.TransactionRepository
import com.floosi.domain.repository.WalletRepository
import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal

class AddTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val walletRepository: WalletRepository,
    private val clock: Clock,
) {

    suspend operator fun invoke(
        type: TransactionType,
        amount: BigDecimal,
        currency: Currency,
        walletId: WalletId,
        toWalletId: WalletId? = null,
        categoryId: CategoryId? = null,
        note: String? = null,
        occurredAt: LocalDateTime = clock.now(),
        timezone: String = "Africa/Cairo",
        amountInBase: BigDecimal = amount,
    ): Result<Transaction> {
        if (amount <= BigDecimal.ZERO) {
            return Result.error(IllegalArgumentException("Amount must be positive"))
        }

        if (type == TransactionType.EXPENSE) {
            val wallet = walletRepository.getById(walletId)
                ?: return Result.error(IllegalArgumentException("Wallet not found"))
            if (wallet.balance < amount) {
                return Result.error(IllegalArgumentException("Insufficient balance"))
            }
        }

        if (type == TransactionType.TRANSFER && toWalletId == null) {
            return Result.error(IllegalArgumentException("Transfer requires a destination wallet"))
        }

        val now = clock.now()
        val transaction = Transaction(
            id = TransactionId(0L),
            type = type,
            amount = amount,
            currency = currency,
            amountInBase = amountInBase,
            walletId = walletId,
            toWalletId = toWalletId,
            categoryId = categoryId,
            note = note,
            occurredAt = occurredAt,
            timezone = timezone,
            isExcluded = false,
            createdAt = now,
            updatedAt = now,
        )

        val savedTransaction = transactionRepository.insert(transaction)

        when (type) {
            TransactionType.EXPENSE -> {
                walletRepository.adjustBalance(walletId, amount.negate())
            }
            TransactionType.INCOME -> {
                walletRepository.adjustBalance(walletId, amount)
            }
            TransactionType.TRANSFER -> {
                walletRepository.adjustBalance(walletId, amount.negate())
                if (toWalletId != null) {
                    walletRepository.adjustBalance(toWalletId, amount)
                }
            }
        }

        return Result.success(savedTransaction)
    }
}
