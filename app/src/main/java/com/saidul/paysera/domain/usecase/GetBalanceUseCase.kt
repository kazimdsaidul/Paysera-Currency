package com.saidul.paysera.domain.usecase

import com.saidul.paysera.domain.model.Balance
import com.saidul.paysera.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow

class GetBalanceUseCase(
    private val repository: CurrencyRepository
) {

    operator fun invoke(): Flow<List<Balance>> {
        return repository.getBalance()
    }
}