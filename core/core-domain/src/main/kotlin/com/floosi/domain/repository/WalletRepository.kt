package com.floosi.domain.repository

import com.floosi.domain.model.Wallet
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface WalletRepository {
    fun observeActive(): Flow<List<Wallet>>
    fun observeAll(): Flow<List<Wallet>>
    suspend fun getById(id: Long): Wallet?
    suspend fun insert(wallet: Wallet): Long
    suspend fun update(wallet: Wallet)
    suspend fun setArchived(id: Long, archived: Boolean)
    suspend fun adjustBalance(id: Long, amount: BigDecimal)
    suspend fun delete(id: Long)
}
