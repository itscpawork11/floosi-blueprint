package com.floosi.domain.usecase

import com.floosi.domain.model.CategoryId
import com.floosi.domain.model.Transaction
import com.floosi.domain.model.WalletId
import com.floosi.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionsUseCase(
    private val transactionRepository: TransactionRepository,
) {

    fun observeAll(): Flow<List<Transaction>> {
        return transactionRepository.observeAll()
    }

    fun observeByWallet(walletId: WalletId): Flow<List<Transaction>> {
        return transactionRepository.observeByWallet(walletId.value)
    }

    fun observeByCategory(categoryId: CategoryId): Flow<List<Transaction>> {
        return transactionRepository.observeByCategory(categoryId.value)
    }
}
