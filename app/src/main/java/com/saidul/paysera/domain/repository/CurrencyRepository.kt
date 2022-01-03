package com.saidul.paysera.domain.repository

import com.saidul.paysera.core.Resource
import com.saidul.paysera.core.network.Result
import com.saidul.paysera.data.remote.model.ResposLatest
import com.saidul.paysera.domain.model.Balance
import com.saidul.paysera.domain.model.Rate
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun convert(from: String, to: String, amount: Double): Result<ResposLatest>
    suspend fun latest(): Resource<HashMap<String, Double>>
    suspend fun insertBalances(dataList: List<Balance>)
    suspend fun insertBalance(data: Balance)
    fun getBalance(): Flow<List<Balance>>
    fun getRate(currency: String): Flow<Rate>
    suspend fun deleteBalanceData()
    fun addLatestCurrency(data: java.util.HashMap<String, Double>?)
}