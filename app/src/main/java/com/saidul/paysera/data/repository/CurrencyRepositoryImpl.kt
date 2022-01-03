package com.saidul.paysera.data.repository


import com.saidul.paysera.core.BaseService
import com.saidul.paysera.core.Resource
import com.saidul.paysera.core.network.Result
import com.saidul.paysera.data.data_source.NoteDao
import com.saidul.paysera.data.remote.ApiService
import com.saidul.paysera.data.remote.model.ResposLatest
import com.saidul.paysera.di.ApiModule
import com.saidul.paysera.domain.model.Balance
import com.saidul.paysera.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject

const val KEY_SUCCESS = "success"
const val KEY_RATES = "rates"


class CurrencyRepositoryImpl(
    private val dao: NoteDao, private val apiService: ApiService
) : BaseService(), CurrencyRepository {
    override suspend fun convert(from: String, to: String, amount: Double): Result<ResposLatest> {
        return createCall {
            apiService.convert(ApiModule.API_TOKEN, from = from, to = to, amount = amount)
        }
    }

    override suspend fun latest(): Resource<HashMap<String, Double>> {
        val rateHashMap = HashMap<String, Double>()

        val createCall = createCall {
            apiService.latest(ApiModule.API_TOKEN)
        }

        val rootJsonObject = JSONObject((createCall as Result.Success).data.string())
        val success = rootJsonObject.getBoolean(KEY_SUCCESS)
        if (success) {
            val ratesJsonObject = rootJsonObject.getJSONObject(KEY_RATES)
            val keys = ratesJsonObject.keys()
            while (keys.hasNext()) {
                val key: String = keys.next()
                val value: Double = ratesJsonObject.get(key).toString().toDouble()
                rateHashMap.put(key, value)
            }
        }
        return Resource.success(data = rateHashMap)
    }

    override suspend fun insertBalances(second: List<Balance>) {
        dao.insertNotes(second)
    }

    override suspend fun insertBalance(second: Balance) {
        dao.insertNote(second)
    }

    override fun getBalance(): Flow<List<Balance>> {
        return dao.getBalance()
    }


}