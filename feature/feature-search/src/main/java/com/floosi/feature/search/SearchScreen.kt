package com.floosi.feature.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.floosi.feature.search.ui.SearchIntent
import com.floosi.feature.search.ui.SearchViewModel
import com.floosi.ui.components.FloosiEmptyState

@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        OutlinedTextField(
            value = state.query,
            onValueChange = { viewModel.onIntent(SearchIntent.SetQuery(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.search_hint)) },
            leadingIcon = { androidx.compose.material3.Icon(Icons.Default.Search, contentDescription = null) },
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.query.isBlank()) {
            FloosiEmptyState(
                icon = Icons.Default.Search,
                title = stringResource(R.string.search_empty_title),
                message = stringResource(R.string.search_empty_desc),
                modifier = Modifier.weight(1f),
            )
        } else if (state.results.isEmpty() && !state.isSearching) {
            FloosiEmptyState(
                icon = Icons.Default.Search,
                title = stringResource(R.string.search_no_results),
                message = stringResource(R.string.search_no_results_desc),
                modifier = Modifier.weight(1f),
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Text(
                        text = stringResource(R.string.search_results_count, state.results.size),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                items(state.results, key = { it.id }) { transaction ->
                    SearchResultCard(transaction = transaction)
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun SearchResultCard(transaction: Transaction) {
    val typeLabel = when (transaction.type) {
        com.floosi.domain.model.TransactionType.INCOME -> stringResource(R.string.search_type_income)
        com.floosi.domain.model.TransactionType.EXPENSE -> stringResource(R.string.search_type_expense)
        com.floosi.domain.model.TransactionType.TRANSFER -> stringResource(R.string.search_type_transfer)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = transaction.note.ifBlank { typeLabel },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = "%.2f ج.م".format(transaction.amountInBase),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = when (transaction.type) {
                    com.floosi.domain.model.TransactionType.INCOME -> com.floosi.designsystem.theme.IncomeGreen
                    com.floosi.domain.model.TransactionType.EXPENSE -> com.floosi.designsystem.theme.ExpenseRed
                    com.floosi.domain.model.TransactionType.TRANSFER -> com.floosi.designsystem.theme.TransferBlue
                },
            )
        }
    }
}
