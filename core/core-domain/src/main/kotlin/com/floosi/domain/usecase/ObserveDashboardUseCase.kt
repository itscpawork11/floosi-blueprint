package com.floosi.domain.usecase

import com.floosi.domain.model.Dashboard
import com.floosi.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class ObserveDashboardUseCase(
    private val transactionRepository: TransactionRepository,
) {

    operator fun invoke(): Flow<Dashboard> {
        return transactionRepository.observeDashboard()
    }
}
