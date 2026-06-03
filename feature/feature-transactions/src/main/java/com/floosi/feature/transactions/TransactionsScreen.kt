package com.floosi.feature.transactions

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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import com.floosi.domain.model.Transaction
import com.floosi.feature.transactions.ui.TransactionFilter
import com.floosi.feature.transactions.ui.TransactionsIntent
import com.floosi.feature.transactions.ui.TransactionsViewModel
import com.floosi.ui.components.FloosiEmptyState
import com.floosi.ui.components.TransactionListShimmer
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TransactionsScreen(
    navController: NavHostController,
    viewModel: TransactionsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            TransactionListShimmer()
            return@Column
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TransactionFilter.entries.forEach { filter ->
                FilterChip(
                    selected = state.filter == filter,
                    onClick = { viewModel.onIntent(TransactionsIntent.SetFilter(filter)) },
                    label = {
                        Text(
                            when (filter) {
                                TransactionFilter.ALL -> stringResource(R.string.transactions_filter_all)
                                TransactionFilter.EXPENSE -> stringResource(R.string.transactions_filter_expense)
                                TransactionFilter.INCOME -> stringResource(R.string.transactions_filter_income)
                                TransactionFilter.TRANSFER -> stringResource(R.string.transactions_filter_transfer)
                            },
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = when (filter) {
                            TransactionFilter.ALL -> MaterialTheme.colorScheme.secondaryContainer
                            TransactionFilter.EXPENSE -> MaterialTheme.colorScheme.errorContainer
                            TransactionFilter.INCOME -> MaterialTheme.colorScheme.tertiaryContainer
                            TransactionFilter.TRANSFER -> MaterialTheme.colorScheme.primaryContainer
                        },
                    ),
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (state.groupedTransactions.isEmpty()) {
            FloosiEmptyState(
                icon = Icons.Default.Receipt,
                title = stringResource(R.string.transactions_no_transactions),
                message = stringResource(R.string.transactions_no_transactions_desc),
                modifier = Modifier.weight(1f),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                state.groupedTransactions.forEach { (date, transactions) ->
                    item {
                        Text(
                            text = date.format(DateTimeFormatter.ofPattern("EEEE, d MMM yyyy", Locale("ar"))),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
                        )
                    }
                    items(transactions, key = { it.id }) { transaction ->
                        TransactionItem(transaction = transaction)
                    }
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {
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
                    text = transaction.note.ifBlank { stringResource(R.string.transactions_fallback_note) },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = transaction.occurredAt.format(
                        DateTimeFormatter.ofPattern("hh:mm a", Locale("ar"))
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(
                text = buildString {
                    when (transaction.type) {
                        com.floosi.domain.model.TransactionType.INCOME -> append("+")
                        com.floosi.domain.model.TransactionType.EXPENSE -> append("-")
                        com.floosi.domain.model.TransactionType.TRANSFER -> {}
                    }
                    append("%.2f ج.م".format(transaction.amountInBase))
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
