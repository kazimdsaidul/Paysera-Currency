package com.saidul.paysera.domain.usecase

import com.saidul.paysera.core.Resource
import com.saidul.paysera.domain.model.Balance
import com.saidul.paysera.domain.model.Transaction
import com.saidul.paysera.domain.repository.CurrencyRepository
import com.saidul.paysera.presentation.KEY_IS_SELL_TYPE
import com.saidul.paysera.utility.NumberFormatter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

const val KEY_EXCHANGE_LIMIT = 5
const val KEY_CHARGED = 0.7

class CalculationAndConvertUseCase(
    private val repository: CurrencyRepository
) {

    operator fun invoke(
        keyIsSellType: String,
        sellCurrency: Balance,
        receiverCurrency: Balance,
        amount: String,

        ): Flow<Resource<Any>> = flow {
        try {
            val total: Double
            var transtionFeee = 0.0
            if (keyIsSellType == KEY_IS_SELL_TYPE) {

                if (amount.toDouble() <= 0.0) {
                    emit(Resource.failed(message = "Amount is not greater than zero"))
                    return@flow
                }

                val isRowIsExistRate = repository.isRowIsExistRate().first()
                if (isRowIsExistRate == false) {
                    emit(Resource.error(message = "Check your internet connection"))
                    return@flow
                }
                
                val transactionSize = repository.getTransactionList().first().size
                val sellBalance = repository.getBalance(sellCurrency.currencyName).first()
                val receiverBalance = repository.getBalance(receiverCurrency.currencyName).first()
                val sellRate = repository.getRate(sellCurrency.currencyName).first()
                val receiveRate = repository.getRate(receiverCurrency.currencyName).first()

                total = receiveRate.rate * amount.toDouble()

                if (sellBalance.balance < amount.toDouble()) {
                    emit(Resource.failed(message = "Sell amount is not greater than current balance"))
                    return@flow
                }

                if (transactionSize >= KEY_EXCHANGE_LIMIT) {
                    transtionFeee = ((KEY_CHARGED / total) * 100)
                }

                // update balance
                val updateSellBalance = Balance(
                    currencyName = sellCurrency.currencyName,
                    balance = sellBalance.balance - amount.toDouble(),
                    id = sellBalance.id
                )
                val updateReciverBalance = Balance(
                    currencyName = receiverCurrency.currencyName,
                    balance = receiverBalance.balance + (total - transtionFeee),
                    id = receiverBalance.id
                )

                repository.updateBalance(updateSellBalance)
                repository.updateBalance(updateReciverBalance)

                // add transaction history
                val transaction = Transaction(
                    toCurrency = sellCurrency.currencyName,
                    fromCurrency = receiverCurrency.currencyName,
                    toAmount = sellBalance.balance,
                    fromAmount = receiverBalance.balance,
                    currentBalance = updateSellBalance.balance,
                    commissionFee = transtionFeee
                )
                repository.addTransction(transaction)


                val message =
                    "You have converted ${NumberFormatter.formatTwoDecimalNumber(amount = amount.toDouble())} ${sellCurrency.currencyName} to ${
                        NumberFormatter.formatTwoDecimalNumber(
                            amount = total
                        )
                    } ${receiverCurrency.currencyName}. Commission Fee - ${
                        NumberFormatter.formatTwoDecimalNumber(transtionFeee)
                    } EUR.\n"
                emit(Resource.success(data = message))

            }

        } catch (ex: NumberFormatException) { // handle your exception
            emit(Resource.failed(message = "Invalid number"))
        } catch (e: Exception) {
            emit(Resource.error(message = "Try again"))
        }

    }
}