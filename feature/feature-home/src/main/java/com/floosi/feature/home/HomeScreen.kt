package com.floosi.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.floosi.common.AmountFormatter
import com.floosi.domain.model.Transaction
import com.floosi.feature.home.ui.HomeViewModel
import com.floosi.ui.components.BalanceCardShimmer
import com.floosi.ui.components.FloosiEmptyState
import com.floosi.ui.components.TransactionListShimmer

private val INCOME_CATEGORY_NAMES = mapOf(
    1L to "راتب", 2L to "فري لانس", 3L to "استثمار",
    4L to "هدية", 5L to "أخرى",
)

private val EXPENSE_CATEGORY_NAMES = mapOf(
    1L to "أكل وشرب", 2L to "مواصلات", 3L to "إيجار",
    4L to "فواتير", 5L to "نت وتليفون", 6L to "تسوق",
    7L to "ترفيه", 8L to "صحة", 9L to "تعليم",
    10L to "ملابس", 11L to "هدية", 12L to "أخرى",
)

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.isLoading) {
        Column(modifier = Modifier.fillMaxSize()) {
            BalanceCardShimmer()
            Spacer(modifier = Modifier.height(8.dp))
            TransactionListShimmer()
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            TotalBalanceCard(
                totalBalance = state.totalBalance,
                monthlyIncome = state.monthlyIncome,
                monthlyExpense = state.monthlyExpense,
            )
        }

        item {
            MonthlySummaryCard(
                income = state.monthlyIncome,
                expense = state.monthlyExpense,
            )
        }

        item {
            Text(
                text = stringResource(R.string.home_recent_transactions),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        if (state.recentTransactions.isEmpty()) {
            item {
                FloosiEmptyState(
                    icon = Icons.Default.Receipt,
                    title = stringResource(R.string.home_no_transactions),
                    message = stringResource(R.string.home_no_transactions_desc),
                )
            }
        } else {
            items(state.recentTransactions, key = { it.id }) { transaction ->
                TransactionCard(transaction = transaction)
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TotalBalanceCard(
    totalBalance: java.math.BigDecimal,
    monthlyIncome: java.math.BigDecimal,
    monthlyExpense: java.math.BigDecimal,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.home_total_balance),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = AmountFormatter.format(totalBalance),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.home_monthly_income),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                )
                Text(
                    text = AmountFormatter.format(monthlyIncome),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.home_monthly_expense),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                )
                Text(
                    text = AmountFormatter.format(monthlyExpense),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

@Composable
private fun MonthlySummaryCard(
    income: java.math.BigDecimal,
    expense: java.math.BigDecimal,
) {
    val net = income.subtract(expense)
    val isPositive = net >= java.math.BigDecimal.ZERO

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPositive)
                MaterialTheme.colorScheme.tertiaryContainer
            else
                MaterialTheme.colorScheme.errorContainer,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = stringResource(R.string.home_net_month),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = AmountFormatter.format(net),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isPositive)
                        MaterialTheme.colorScheme.onTertiaryContainer
                    else
                        MaterialTheme.colorScheme.onErrorContainer,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = stringResource(R.string.home_income_label, AmountFormatter.format(income)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = stringResource(R.string.home_expense_label, AmountFormatter.format(expense)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun TransactionCard(transaction: Transaction) {
    val categoryName = (transaction.categoryId?.let { id ->
        if (transaction.type == com.floosi.domain.model.TransactionType.INCOME) {
            INCOME_CATEGORY_NAMES[id]
        } else {
            EXPENSE_CATEGORY_NAMES[id]
        }
    } ?: stringResource(R.string.home_category_other))
    val currencyLabel = stringResource(R.string.currency_egp)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                )
                if (transaction.note.isNotBlank()) {
                    Text(
                        text = transaction.note,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Text(
                text = buildString {
                    when (transaction.type) {
                        com.floosi.domain.model.TransactionType.INCOME -> append("+")
                        com.floosi.domain.model.TransactionType.EXPENSE -> append("-")
                        com.floosi.domain.model.TransactionType.TRANSFER -> {}
                    }
                    // Use AmountFormatter without currency symbol
                    val formatted = java.text.NumberFormat.getNumberInstance(java.util.Locale("ar", "EG")).apply {
                        minimumFractionDigits = 2
                        maximumFractionDigits = 2
                        isGroupingUsed = true
                    }.format(transaction.amountInBase.setScale(2, java.math.RoundingMode.HALF_UP))
                    append(" $formatted $currencyLabel")
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = when (transaction.type) {
                    com.floosi.domain.model.TransactionType.INCOME -> com.floosi.designsystem.theme.IncomeGreen
                    com.floosi.domain.model.TransactionType.EXPENSE -> com.floosi.designsystem.theme.ExpenseRed
                    com.floosi.domain.model.TransactionType.TRANSFER -> com.floosi.designsystem.theme.TransferBlue
                },
            )
        }
    }
}
