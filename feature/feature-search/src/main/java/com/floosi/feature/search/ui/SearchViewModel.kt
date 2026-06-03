package com.floosi.feature.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floosi.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private var searchJob: Job? = null

    fun onIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.SetQuery -> {
                _state.update { it.copy(query = intent.query) }
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(300)
                    val q = _state.value.query.trim()
                    if (q.length < 2) {
                        _state.update { it.copy(results = emptyList(), isSearching = false) }
                        return@launch
                    }
                    _state.update { it.copy(isSearching = true) }
                    transactionRepository.search("*$q*").collect { results ->
                        _state.update {
                            it.copy(results = results, isSearching = false)
                        }
                    }
                }
            }
            SearchIntent.ClearSearch -> {
                searchJob?.cancel()
                _state.value = SearchState()
            }
        }
    }
}
