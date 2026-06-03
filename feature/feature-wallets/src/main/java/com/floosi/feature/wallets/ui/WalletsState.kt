package com.floosi.feature.wallets.ui

import com.floosi.domain.model.Wallet
import com.floosi.domain.model.WalletType
import java.math.BigDecimal

data class WalletsState(
    val wallets: List<Wallet> = emptyList(),
    val totalBalance: BigDecimal = BigDecimal.ZERO,
    val isLoading: Boolean = true
)

data class AddEditWalletState(
    val isEditing: Boolean = false,
    val walletId: Long? = null,
    val name: String = "",
    val type: WalletType = WalletType.CASH,
    val balance: String = "",
    val currencyCode: String = "EGP",
    val colorArgb: Int = 0xFF0F6E5C.toInt(),
    val iconKey: String = "wallet_cash",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorRes: Int? = null
)

sealed interface WalletsIntent {
    data object LoadWallets : WalletsIntent
    data class SetArchived(val walletId: Long, val archived: Boolean) : WalletsIntent
    data class DeleteWallet(val walletId: Long) : WalletsIntent
}

sealed interface AddEditWalletIntent {
    data class SetName(val name: String) : AddEditWalletIntent
    data class SetType(val type: WalletType) : AddEditWalletIntent
    data class SetBalance(val balance: String) : AddEditWalletIntent
    data class SetCurrency(val currencyCode: String) : AddEditWalletIntent
    data class SetColor(val colorArgb: Int) : AddEditWalletIntent
    data class SetIcon(val iconKey: String) : AddEditWalletIntent
    data object Save : AddEditWalletIntent
    data object Reset : AddEditWalletIntent
}
