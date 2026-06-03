package com.floosi.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavHostController
import com.floosi.app.R
import com.floosi.feature.categories.CategoriesScreen
import com.floosi.feature.home.HomeScreen
import com.floosi.feature.settings.SettingsScreen
import com.floosi.feature.transactions.TransactionsScreen
import com.floosi.feature.wallets.WalletsScreen

private data class TabItem(
    val labelRes: Int,
    val icon: @Composable () -> Unit,
    val route: String,
)

private val tabs = listOf(
    TabItem(
        labelRes = R.string.tab_home,
        icon = { Icon(Icons.Default.Home, contentDescription = null) },
        route = Routes.HOME,
    ),
    TabItem(
        labelRes = R.string.tab_transactions,
        icon = { Icon(Icons.Default.Receipt, contentDescription = null) },
        route = Routes.TRANSACTIONS,
    ),
    TabItem(
        labelRes = R.string.tab_wallets,
        icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null) },
        route = Routes.WALLETS,
    ),
    TabItem(
        labelRes = R.string.tab_categories,
        icon = { Icon(Icons.Default.Category, contentDescription = null) },
        route = Routes.CATEGORIES,
    ),
    TabItem(
        labelRes = R.string.tab_more,
        icon = { Icon(Icons.Default.MoreHoriz, contentDescription = null) },
        route = Routes.MORE,
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(navController: NavHostController) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.layoutDirection(LayoutDirection.Rtl),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = tabs[selectedTab].labelRes)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = tab.icon,
                        label = { Text(text = stringResource(id = tab.labelRes)) },
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.QUICK_ADD) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.quickadd_title),
                )
            }
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> HomeScreen(navController = navController)
                1 -> TransactionsScreen(navController = navController)
                2 -> WalletsScreen(navController = navController)
                3 -> CategoriesScreen(navController = navController)
                4 -> SettingsScreen(navController = navController)
            }
        }
    }
}
