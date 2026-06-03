package com.floosi.data.di

import com.floosi.data.repository.CategoryRepositoryImpl
import com.floosi.data.repository.TransactionRepositoryImpl
import com.floosi.data.repository.WalletRepositoryImpl
import com.floosi.domain.repository.CategoryRepository
import com.floosi.domain.repository.TransactionRepository
import com.floosi.domain.repository.WalletRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindWalletRepository(
        impl: WalletRepositoryImpl,
    ): WalletRepository

    @Binds
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl,
    ): CategoryRepository

    @Binds
    abstract fun bindTransactionRepository(
        impl: TransactionRepositoryImpl,
    ): TransactionRepository
}
