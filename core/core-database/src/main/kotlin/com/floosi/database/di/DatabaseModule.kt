package com.floosi.database.di

import android.content.Context
import androidx.room.Room
import com.floosi.database.FloosiDatabase
import com.floosi.database.dao.CategoryDao
import com.floosi.database.dao.TransactionDao
import com.floosi.database.dao.WalletDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FloosiDatabase {
        return Room.databaseBuilder(
            context,
            FloosiDatabase::class.java,
            FloosiDatabase.DATABASE_NAME,
        ).build()
    }

    @Provides
    fun provideWalletDao(database: FloosiDatabase): WalletDao = database.walletDao()

    @Provides
    fun provideCategoryDao(database: FloosiDatabase): CategoryDao = database.categoryDao()

    @Provides
    fun provideTransactionDao(database: FloosiDatabase): TransactionDao = database.transactionDao()
}
