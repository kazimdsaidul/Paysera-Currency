package com.saidul.paysera.domain.usecase

import com.saidul.paysera.core.Resource
import com.saidul.paysera.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CurrencyConvertUseCase(
    private val repository: CurrencyRepository
) {

    operator fun invoke(from: String, to: String, amount: Double): Flow<Resource<Any>> = flow {
        try {
            emit(Resource.loading(true))
            val latest = repository.convert(from, to, amount)
            emit(Resource.loading(false))
            emit(Resource.success(data = latest))
        } catch (e: Exception) {
            emit(Resource.error(e.localizedMessage, e))
        }
    }
}