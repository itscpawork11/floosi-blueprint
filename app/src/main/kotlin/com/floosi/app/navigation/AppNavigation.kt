package com.floosi.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.floosi.core.ui.PinSetupScreen
import com.floosi.core.ui.QuickAddSheet
import com.floosi.feature.lock.LockScreen
import com.floosi.feature.onboarding.OnboardingNavHost
import com.floosi.feature.search.SearchScreen
import com.floosi.feature.transactions.TransactionDetailScreen
import com.floosi.feature.transactions.TransactionsScreen
import com.floosi.feature.wallets.AddWalletScreen
import com.floosi.feature.wallets.WalletDetailScreen
import com.floosi.feature.wallets.WalletsScreen

object Routes {
    const val ONBOARDING = "onboarding"
    const val LOCK = "lock"
    const val HOME = "home"
    const val TRANSACTIONS = "transactions"
    const val WALLETS = "wallets"
    const val WALLET_DETAIL = "wallet/{id}"
    const val TRANSACTION_DETAIL = "transaction/{id}"
    const val ADD_WALLET = "add-wallet"
    const val QUICK_ADD = "quick-add"
    const val PIN_SETUP = "pin-setup"
    const val CATEGORIES = "categories"
    const val MORE = "more"
    const val SEARCH = "search"

    fun walletDetail(id: String) = "wallet/$id"
    fun transactionDetail(id: String) = "transaction/$id"
}

@Composable
fun AppNavigation(startRoute: String = Routes.HOME) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startRoute,
    ) {
        composable(Routes.ONBOARDING) {
            OnboardingNavHost(
                onComplete = {
                    navController.navigate(Routes.LOCK) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.LOCK) {
            LockScreen(
                onUnlocked = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOCK) { inclusive = true }
                    }
                },
                onSetupPin = {
                    navController.navigate(Routes.PIN_SETUP)
                }
            )
        }
        composable(Routes.HOME) {
            MainScaffold(navController = navController)
        }
        composable(Routes.TRANSACTIONS) {
            TransactionsScreen(navController = navController)
        }
        composable(Routes.WALLETS) {
            WalletsScreen(navController = navController)
        }
        composable(Routes.ADD_WALLET) {
            AddWalletScreen(navController = navController)
        }
        composable(
            route = Routes.WALLET_DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            WalletDetailScreen(navController = navController, walletId = id)
        }
        composable(
            route = Routes.TRANSACTION_DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            TransactionDetailScreen(navController = navController, transactionId = id)
        }
        composable(Routes.QUICK_ADD) {
            QuickAddSheet(
                onDismiss = { navController.popBackStack() }
            )
        }
        composable(Routes.PIN_SETUP) {
            PinSetupScreen(
                onComplete = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.SEARCH) {
            SearchScreen(navController = navController)
        }
    }
}
