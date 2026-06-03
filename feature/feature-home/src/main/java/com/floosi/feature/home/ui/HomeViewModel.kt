package com.floosi.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floosi.domain.usecase.ObserveDashboardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeDashboardUseCase: ObserveDashboardUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        observeDashboard()
    }

    private fun observeDashboard() {
        viewModelScope.launch {
            observeDashboardUseCase().collect { dashboard ->
                _state.update {
                    it.copy(
                        totalBalance = dashboard.totalBalance,
                        monthlyIncome = dashboard.monthlyIncome,
                        monthlyExpense = dashboard.monthlyExpense,
                        recentTransactions = dashboard.recentTransactions,
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Refresh -> observeDashboard()
        }
    }
}
