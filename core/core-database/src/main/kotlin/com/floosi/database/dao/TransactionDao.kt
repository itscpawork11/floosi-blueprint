package com.floosi.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.floosi.database.entity.CategoryExpense
import com.floosi.database.entity.DashboardRaw
import com.floosi.database.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDateTime
import java.time.ZoneOffset

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions ORDER BY occurred_at DESC")
    fun observeAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE wallet_id = :walletId ORDER BY occurred_at DESC")
    fun observeByWallet(walletId: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE category_id = :categoryId ORDER BY occurred_at DESC")
    fun observeByCategory(categoryId: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getById(id: Long): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY occurred_at DESC LIMIT 10")
    fun observeRecentTransactions(): Flow<List<TransactionEntity>>

    @Query(
        """
        SELECT COALESCE(SUM(amount_in_base), 0)
        FROM transactions
        WHERE type = 'INCOME' AND occurred_at >= :startEpochMillis AND occurred_at < :endEpochMillis
        """,
    )
    fun observeMonthlyIncome(startEpochMillis: Long, endEpochMillis: Long): Flow<Double>

    @Query(
        """
        SELECT COALESCE(SUM(amount_in_base), 0)
        FROM transactions
        WHERE type = 'EXPENSE' AND occurred_at >= :startEpochMillis AND occurred_at < :endEpochMillis
        """,
    )
    fun observeMonthlyExpense(startEpochMillis: Long, endEpochMillis: Long): Flow<Double>

    @Query(
        """
        SELECT category_id, COALESCE(SUM(amount_in_base), 0) AS total
        FROM transactions
        WHERE type = 'EXPENSE' AND occurred_at >= :startEpochMillis AND occurred_at < :endEpochMillis
        GROUP BY category_id
        """,
    )
    fun observeExpensesByCategory(startEpochMillis: Long, endEpochMillis: Long): Flow<List<CategoryExpense>>

    @Query(
        """
        SELECT t.* FROM transactions t
        JOIN transactions_fts ON transactions_fts.rowid = t.id
        WHERE transactions_fts MATCH :query
        ORDER BY t.occurred_at DESC
        """,
    )
    fun search(query: String): Flow<List<TransactionEntity>>

    fun observeDashboard(): Flow<DashboardRaw> {
        val now = LocalDateTime.now(ZoneOffset.UTC)
        val startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
        val startOfNextMonth = startOfMonth.plusMonths(1)
        val startEpoch = startOfMonth.toInstant(ZoneOffset.UTC).toEpochMilli()
        val endEpoch = startOfNextMonth.toInstant(ZoneOffset.UTC).toEpochMilli()

        return combine(
            observeMonthlyIncome(startEpoch, endEpoch),
            observeMonthlyExpense(startEpoch, endEpoch),
            observeRecentTransactions(),
        ) { income, expense, recent ->
            DashboardRaw(
                monthlyIncome = income,
                monthlyExpense = expense,
                recentTransactions = recent,
            )
        }
    }
}
