package com.saidul.paysera.domain.usecase

import com.saidul.paysera.core.Resource
import com.saidul.paysera.domain.model.Balance
import com.saidul.paysera.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

const val KEY_DEFAULT_CURRENCY = "EUR"
const val KEY_AMOUNT = 1000.00

class CurrencyLatestUseCase(
    private val repository: CurrencyRepository
) {

    operator fun invoke(): Flow<Resource<Any>> = flow {
        try {
            emit(Resource.loading(true))
            val latest = repository.latest()
            if (repository.getBalance().first().isEmpty()) {
                repository.insertBalance(
                    Balance(
                        currencyName = KEY_DEFAULT_CURRENCY,
                        balance = KEY_AMOUNT
                    )
                )
                val toList = latest.data?.keys?.toList()
                val mutableListOf = mutableListOf<Balance>()
                toList?.forEach {
                    mutableListOf.add(Balance(currencyName = it, balance = 0.0))
                }
                repository.insertBalances(mutableListOf)
            }
            emit(Resource.loading(false))

            emit(Resource.success(data = latest))
        } catch (e: Exception) {
            emit(Resource.error(e.localizedMessage, e))
        } as Unit
    } as Flow<Resource<Any>>

}