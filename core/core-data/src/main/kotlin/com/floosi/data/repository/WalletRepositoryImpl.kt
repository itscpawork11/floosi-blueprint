package com.floosi.data.repository

import com.floosi.data.mapper.toDomain
import com.floosi.data.mapper.toEntity
import com.floosi.database.dao.WalletDao
import com.floosi.domain.model.Wallet
import com.floosi.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val walletDao: WalletDao,
) : WalletRepository {

    override fun observeActive(): Flow<List<Wallet>> {
        return walletDao.observeActive().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeAll(): Flow<List<Wallet>> {
        return walletDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getById(id: Long): Wallet? {
        return walletDao.getById(id)?.toDomain()
    }

    override suspend fun insert(wallet: Wallet): Long {
        return walletDao.insert(wallet.toEntity())
    }

    override suspend fun update(wallet: Wallet) {
        walletDao.update(wallet.toEntity())
    }

    override suspend fun setArchived(id: Long, archived: Boolean) {
        walletDao.setArchived(id, archived)
    }

    override suspend fun adjustBalance(id: Long, amount: BigDecimal) {
        walletDao.adjustBalance(id, amount.toDouble())
    }

    override suspend fun delete(id: Long) {
        walletDao.getById(id)?.let { walletDao.delete(it) }
    }
}
