package com.floosi.domain.usecase

import com.floosi.domain.model.WalletId
import com.floosi.domain.repository.WalletRepository
import java.math.BigDecimal

class UpdateWalletBalanceUseCase(
    private val walletRepository: WalletRepository,
) {

    suspend operator fun invoke(walletId: WalletId, delta: BigDecimal) {
        walletRepository.adjustBalance(walletId.value, delta)
    }
}
