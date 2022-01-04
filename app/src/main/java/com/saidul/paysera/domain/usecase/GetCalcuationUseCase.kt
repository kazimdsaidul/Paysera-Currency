package com.saidul.paysera.domain.usecase

import com.saidul.paysera.domain.model.Balance
import com.saidul.paysera.domain.repository.CurrencyRepository
import com.saidul.paysera.presentation.KEY_IS_SELL_TYPE
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class GetCalcuationUseCase(
    private val repository: CurrencyRepository
) {
    suspend fun calculation(
        keyIsSellType: String,
        sellBalance: Balance,
        reciverBalance: Balance,
        amount: Double
    ) = flow {

        var total = 0.0
        if (keyIsSellType == KEY_IS_SELL_TYPE) {
            val sellRate = repository.getRate(sellBalance.currencyName).first()
            val reciveRate = repository.getRate(reciverBalance.currencyName).first()
            total = reciveRate.rate * amount
            emit(total)
        }
    }
}