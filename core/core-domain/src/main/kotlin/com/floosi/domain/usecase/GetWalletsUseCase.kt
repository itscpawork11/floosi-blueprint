package com.floosi.domain.usecase

import com.floosi.domain.model.Wallet
import com.floosi.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow

class GetWalletsUseCase(
    private val walletRepository: WalletRepository,
) {

    fun observeActive(): Flow<List<Wallet>> {
        return walletRepository.observeActive()
    }

    fun observeAll(): Flow<List<Wallet>> {
        return walletRepository.observeAll()
    }
}
