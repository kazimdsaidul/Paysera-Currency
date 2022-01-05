package com.saidul.paysera.data.repository


import com.saidul.paysera.core.BaseService
import com.saidul.paysera.core.Resource
import com.saidul.paysera.core.network.Result
import com.saidul.paysera.data.local.LocalDBDao
import com.saidul.paysera.data.remote.ApiService
import com.saidul.paysera.data.remote.model.ResposLatest
import com.saidul.paysera.di.APIModule
import com.saidul.paysera.domain.model.Balance
import com.saidul.paysera.domain.model.Rate
import com.saidul.paysera.domain.model.Transaction
import com.saidul.paysera.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject

const val KEY_SUCCESS = "success"
const val KEY_RATES = "rates"


class CurrencyRepositoryImpl(
    private val dao: LocalDBDao, private val apiService: ApiService
) : BaseService(), CurrencyRepository {
    override suspend fun convert(from: String, to: String, amount: Double): Result<ResposLatest> {
        return createCall {
            apiService.convert(APIModule.API_TOKEN, from = from, to = to, amount = amount)
        }
    }

    override suspend fun latest(): Resource<HashMap<String, Double>> {
        val rateHashMap = HashMap<String, Double>()

        val createCall = createCall {
            apiService.latest(APIModule.API_TOKEN)
        }
        val rootJsonObject = JSONObject((createCall as Result.Success).data.string())
        val success = rootJsonObject.getBoolean(KEY_SUCCESS)
        if (success) {
            val tempData = mutableListOf<Rate>()
            val ratesJsonObject = rootJsonObject.getJSONObject(KEY_RATES)
            val keys = ratesJsonObject.keys()
            while (keys.hasNext()) {
                val key: String = keys.next()
                val value: Double = ratesJsonObject.get(key).toString().toDouble()
                rateHashMap.put(key, value)
                tempData.add(Rate(currencyName = key, rate = value))
            }
            dao.deleteRateData()
            dao.insertRateList(tempData)
        }
        return Resource.success(data = rateHashMap)
    }

    override suspend fun insertBalances(dataList: List<Balance>) {
        dao.insertBalance(dataList)
    }

    override suspend fun insertBalance(data: Balance) {
        dao.insertBalance(data)
    }

    override fun getBalanceList(): Flow<List<Balance>> {
        return dao.getBalance()
    }

    override fun getBalance(sellCurrency: String): Flow<Balance> {
        return dao.getBalance(sellCurrency)
    }

    override fun getRate(currency: String): Flow<Rate> {
        return dao.getRates(currency)
    }


    override suspend fun deleteBalanceData() {
        dao.deleteBalanceData()
    }

    override fun isRowIsExistRate(): Flow<Boolean> {
        return dao.isRowIsExistRate(1)
    }


    override suspend fun updateBalance(updateSellBalance: Balance) {
        dao.updateBalance(updateSellBalance)
    }

    override suspend fun addTransction(transaction: Transaction) {
        dao.addTransction(transaction)
    }

    override fun getTransactionList(): Flow<List<Transaction>> {
        return dao.getTransction()
    }

    override suspend fun deleteTranstion() {
        dao.deleteTranstion()
    }


}