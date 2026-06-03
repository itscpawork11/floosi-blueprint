package com.floosi.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.floosi.database.entity.WalletEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {

    @Query("SELECT * FROM wallets WHERE is_archived = 0 ORDER BY sort_order ASC")
    fun observeActive(): Flow<List<WalletEntity>>

    @Query("SELECT * FROM wallets ORDER BY sort_order ASC")
    fun observeAll(): Flow<List<WalletEntity>>

    @Query("SELECT * FROM wallets WHERE id = :id")
    suspend fun getById(id: Long): WalletEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wallet: WalletEntity): Long

    @Update
    suspend fun update(wallet: WalletEntity)

    @Query("UPDATE wallets SET is_archived = :archived WHERE id = :id")
    suspend fun setArchived(id: Long, archived: Boolean)

    @Query("UPDATE wallets SET balance = balance + :amount WHERE id = :id")
    suspend fun adjustBalance(id: Long, amount: Double)

    @Delete
    suspend fun delete(wallet: WalletEntity)
}
