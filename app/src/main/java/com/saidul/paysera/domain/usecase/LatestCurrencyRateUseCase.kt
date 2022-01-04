package com.saidul.paysera.domain.usecase

import com.saidul.paysera.core.Resource
import com.saidul.paysera.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LatestCurrencyRateUseCase(
    private val repository: CurrencyRepository
) {

    operator fun invoke(): Flow<Resource<Any>> = flow {
        try {
            emit(Resource.loading(true))
            val latest = repository.latest()
            emit(Resource.success(data = latest))
        } catch (e: Exception) {
            emit(Resource.error(e.localizedMessage, e))
        }
    }

}