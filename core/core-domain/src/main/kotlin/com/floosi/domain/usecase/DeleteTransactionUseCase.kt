package com.floosi.domain.usecase

import com.floosi.domain.model.Transaction
import com.floosi.domain.model.TransactionType
import com.floosi.domain.repository.TransactionRepository
import com.floosi.domain.repository.WalletRepository

class DeleteTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val walletRepository: WalletRepository,
) {

    suspend operator fun invoke(transaction: Transaction) {
        transactionRepository.delete(transaction.id)

        when (transaction.type) {
            TransactionType.EXPENSE -> {
                walletRepository.adjustBalance(transaction.walletId, transaction.amount)
            }
            TransactionType.INCOME -> {
                walletRepository.adjustBalance(transaction.walletId, transaction.amount.negate())
            }
            TransactionType.TRANSFER -> {
                walletRepository.adjustBalance(transaction.walletId, transaction.amount)
                transaction.toWalletId?.let { toWalletId ->
                    walletRepository.adjustBalance(toWalletId, transaction.amount.negate())
                }
            }
        }
    }
}
