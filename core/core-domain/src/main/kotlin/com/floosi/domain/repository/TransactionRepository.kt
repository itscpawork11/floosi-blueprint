package com.floosi.domain.repository

import com.floosi.domain.model.Dashboard
import com.floosi.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun observeAll(): Flow<List<Transaction>>
    fun observeByWallet(walletId: Long): Flow<List<Transaction>>
    fun observeByCategory(categoryId: Long): Flow<List<Transaction>>
    suspend fun getById(id: Long): Transaction?
    suspend fun insert(transaction: Transaction): Long
    suspend fun update(transaction: Transaction)
    suspend fun delete(id: Long)
    fun observeDashboard(): Flow<Dashboard>
    fun search(query: String): Flow<List<Transaction>>
}
