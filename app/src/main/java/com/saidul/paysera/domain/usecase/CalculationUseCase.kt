package com.saidul.paysera.domain.usecase

import com.saidul.paysera.core.Resource
import com.saidul.paysera.domain.model.Balance
import com.saidul.paysera.domain.repository.CurrencyRepository
import com.saidul.paysera.presentation.KEY_IS_SELL_TYPE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class CalculationUseCase(
    private val repository: CurrencyRepository
) {
    suspend fun calculation(
        keyIsSellType: String,
        sellBalance: Balance,
        receiverBalance: Balance,
        amount: String
    ): Flow<Resource<Any>> = flow {

        try {
            val total: Double
            if (keyIsSellType == KEY_IS_SELL_TYPE) {

                if (amount.toDouble() <= 0.0) {
                    emit(Resource.failed(message = "Amount is less than zero"))
                    return@flow
                }

                val sellRate = repository.getRate(sellBalance.currencyName).first()
                val receiveRate = repository.getRate(receiverBalance.currencyName).first()
                total = receiveRate.rate * amount.toDouble()

                if (sellBalance.balance < amount.toDouble()) {
                    emit(Resource.failed(message = "Amount is not greater than current balance"))
                    return@flow
                }

                emit(Resource.success(data = total))
            }
        } catch (ex: NumberFormatException) { // handle your exception
            emit(Resource.failed(message = "Invalid number"))
        } catch (e: Exception) {
            emit(Resource.error(message = "Try again"))
        }


    }
}