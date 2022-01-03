package com.saidul.paysera.domain.repository

import com.saidul.paysera.core.Resource
import com.saidul.paysera.core.network.Result
import com.saidul.paysera.data.remote.model.ResposLatest
import com.saidul.paysera.domain.model.Balance
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun convert(from: String, to: String, amount: Double): Result<ResposLatest>
    suspend fun latest(): Resource<HashMap<String, Double>>
    suspend fun insertBalances(second: List<Balance>)
    suspend fun insertBalance(second: Balance)
    fun getBalance(): Flow<List<Balance>>
}