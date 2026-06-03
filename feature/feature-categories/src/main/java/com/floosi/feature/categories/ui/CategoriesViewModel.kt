package com.floosi.feature.categories.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.floosi.domain.model.CategoryType
import com.floosi.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CategoriesState())
    val state: StateFlow<CategoriesState> = _state.asStateFlow()

    init {
        observeCategories()
    }

    private fun observeCategories() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            categoryRepository.observeByType(_state.value.selectedType).collect { categories ->
                _state.update {
                    it.copy(
                        categories = categories,
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun onIntent(intent: CategoriesIntent) {
        when (intent) {
            is CategoriesIntent.SelectType -> {
                _state.update { it.copy(selectedType = intent.type, isLoading = true) }
                observeCategories()
            }
            CategoriesIntent.Refresh -> observeCategories()
        }
    }
}
