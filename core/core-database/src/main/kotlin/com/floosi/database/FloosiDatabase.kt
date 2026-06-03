package com.floosi.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.floosi.database.converter.Converters
import com.floosi.database.dao.CategoryDao
import com.floosi.database.dao.TransactionDao
import com.floosi.database.dao.WalletDao
import com.floosi.database.entity.CategoryEntity
import com.floosi.database.entity.TagEntity
import com.floosi.database.entity.TransactionEntity
import com.floosi.database.entity.TransactionFtsEntity
import com.floosi.database.entity.TransactionTagCrossRef
import com.floosi.database.entity.WalletEntity

@Database(
    entities = [
        WalletEntity::class,
        CategoryEntity::class,
        TransactionEntity::class,
        TagEntity::class,
        TransactionTagCrossRef::class,
        TransactionFtsEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class FloosiDatabase : RoomDatabase() {

    abstract fun walletDao(): WalletDao

    abstract fun categoryDao(): CategoryDao

    abstract fun transactionDao(): TransactionDao

    companion object {
        const val DATABASE_NAME = "floosi.db"
    }
}
