package com.saidul.paysera.domain.repository

import com.saidul.paysera.core.Resource
import com.saidul.paysera.core.network.Result
import com.saidul.paysera.data.remote.model.ResposLatest
import com.saidul.paysera.domain.model.Balance
import com.saidul.paysera.domain.model.Rate
import com.saidul.paysera.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun convert(from: String, to: String, amount: Double): Result<ResposLatest>
    suspend fun latest(): Resource<HashMap<String, Double>>
    suspend fun insertBalances(dataList: List<Balance>)
    suspend fun insertBalance(data: Balance)
    fun getBalanceList(): Flow<List<Balance>>
    fun getBalance(sellCurrency: String): Flow<Balance>
    fun getRate(currency: String): Flow<Rate>
    suspend fun deleteBalanceData()
    fun isRowIsExistRate(): Flow<Boolean>
    suspend fun updateBalance(updateSellBalance: Balance)
    suspend fun addTransction(transaction: Transaction)
    fun getTransactionList(): Flow<List<Transaction>>
    suspend fun deleteTranstion()

}