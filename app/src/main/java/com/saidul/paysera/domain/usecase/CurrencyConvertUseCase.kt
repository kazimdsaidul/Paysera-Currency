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

class CurrencyConvertUseCase(
    private val repository: CurrencyRepository
) {

    operator fun invoke(
        keyIsSellType: String,
        sellCurrency: String,
        reciverCurrency: String,
        amount: Double,
        convertyAmount: Double
    ): Flow<Resource<Any>> = flow {
        val total: Double
        var transtionFeee: Double = 0.0
        if (keyIsSellType == KEY_IS_SELL_TYPE) {
            val transactionSize = repository.getTransactionList().first().size

            val sellBalace = repository.getBalance(sellCurrency).first()
            val receiverBalance = repository.getBalance(reciverCurrency).first()
            val sellRate = repository.getRate(sellCurrency).first()
            val reciveRate = repository.getRate(reciverCurrency).first()
            total = reciveRate.rate * amount

            if (transactionSize > KEY_EXCHANGE_LIMIT) {
                transtionFeee = ((KEY_CHARGED / total) * 100)
            }

            // update balance
            val updateSellBalance = Balance(
                currencyName = sellCurrency,
                balance = sellBalace.balance - amount,
                id = sellBalace.id
            )
            val updateReciverBalance = Balance(
                currencyName = reciverCurrency,
                balance = receiverBalance.balance + (total - transtionFeee),
                id = receiverBalance.id
            )

            repository.updateBalance(updateSellBalance)
            repository.updateBalance(updateReciverBalance)

            // add transaction history
            val transaction = Transaction(
                toCurrency = sellCurrency,
                fromCurrency = reciverCurrency,
                toAmount = sellBalace.balance,
                fromAmount = receiverBalance.balance,
                currentBalance = updateSellBalance.balance,
                commissionFee = transtionFeee
            )
            repository.addTransction(transaction)


            val message =
                "You have converted ${amount} ${sellCurrency} to ${convertyAmount} ${reciverCurrency}. Commission Fee - ${
                    NumberFormatter.formatTwoDecimalNumber(transtionFeee)
                } EUR.\n"
            emit(Resource.success(data = message))

        }
    }
}