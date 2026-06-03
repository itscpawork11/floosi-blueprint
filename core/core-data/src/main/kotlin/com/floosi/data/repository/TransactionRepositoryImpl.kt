package com.floosi.data.repository

import com.floosi.data.mapper.toDomain
import com.floosi.data.mapper.toEntity
import com.floosi.database.dao.CategoryDao
import com.floosi.database.dao.TransactionDao
import com.floosi.database.dao.WalletDao
import com.floosi.database.entity.CategoryExpense
import com.floosi.domain.model.Dashboard
import com.floosi.domain.model.Transaction
import com.floosi.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val walletDao: WalletDao,
    private val categoryDao: CategoryDao,
) : TransactionRepository {

    override fun observeAll(): Flow<List<Transaction>> {
        return transactionDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeByWallet(walletId: Long): Flow<List<Transaction>> {
        return transactionDao.observeByWallet(walletId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeByCategory(categoryId: Long): Flow<List<Transaction>> {
        return transactionDao.observeByCategory(categoryId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getById(id: Long): Transaction? {
        return transactionDao.getById(id)?.toDomain()
    }

    override suspend fun insert(transaction: Transaction): Long {
        return transactionDao.insert(transaction.toEntity())
    }

    override suspend fun update(transaction: Transaction) {
        transactionDao.update(transaction.toEntity())
    }

    override suspend fun delete(id: Long) {
        transactionDao.getById(id)?.let { transactionDao.delete(it) }
    }

    override fun observeDashboard(): Flow<Dashboard> {
        return combine(
            walletDao.observeActive(),
            transactionDao.observeDashboard(),
            transactionDao.observeExpensesByCategory(
                startOfMonthEpochMillis(),
                startOfNextMonthEpochMillis(),
            ),
        ) { wallets, raw, categoryExpenses ->
            Dashboard(
                totalBalance = wallets.sumOf { it.balance.toDouble() }.toBigDecimal(),
                monthlyIncome = raw.monthlyIncome.toBigDecimal(),
                monthlyExpense = raw.monthlyExpense.toBigDecimal(),
                recentTransactions = raw.recentTransactions.map { it.toDomain() },
                expensesByCategory = categoryExpenses.associate { entry ->
                    (entry.categoryId ?: -1L) to entry.total.toBigDecimal()
                },
            )
        }
    }

    override fun search(query: String): Flow<List<Transaction>> {
        return transactionDao.search(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    private fun startOfMonthEpochMillis(): Long {
        val now = java.time.LocalDateTime.now(java.time.ZoneOffset.UTC)
        return now.withDayOfMonth(1)
            .withHour(0).withMinute(0).withSecond(0).withNano(0)
            .toInstant(java.time.ZoneOffset.UTC).toEpochMilli()
    }

    private fun startOfNextMonthEpochMillis(): Long {
        val now = java.time.LocalDateTime.now(java.time.ZoneOffset.UTC)
        return now.withDayOfMonth(1)
            .withHour(0).withMinute(0).withSecond(0).withNano(0)
            .plusMonths(1)
            .toInstant(java.time.ZoneOffset.UTC).toEpochMilli()
    }
}
