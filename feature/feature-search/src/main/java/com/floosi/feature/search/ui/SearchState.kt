package com.floosi.feature.search.ui

import com.floosi.domain.model.Transaction

data class SearchState(
    val query: String = "",
    val results: List<Transaction> = emptyList(),
    val isSearching: Boolean = false,
)

sealed interface SearchIntent {
    data class SetQuery(val query: String) : SearchIntent
    data object ClearSearch : SearchIntent
}
