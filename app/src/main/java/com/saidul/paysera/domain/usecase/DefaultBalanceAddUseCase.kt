package com.saidul.paysera.domain.usecase

import com.saidul.paysera.core.Resource
import com.saidul.paysera.domain.model.Balance
import com.saidul.paysera.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DefaultBalanceAddUseCase(val repository: CurrencyRepository) {
    operator fun invoke(isNeedClearData: Boolean): Flow<Resource<Any>> =
        flow {

            val defaultList = mutableListOf<Balance>()
            defaultList.add(Balance("EUR", 1000.00))
            defaultList.add(Balance("USD", 0.00))
            defaultList.add(Balance("JPY", 0.00))
            defaultList.add(Balance("GBP", 0.00))


            try {
                emit(Resource.loading(true))
                if (isNeedClearData) {
                    repository.deleteBalanceData()
                    repository.deleteTransition()
                    insertDefalutData(defaultList)
                } else {
                    insertDefalutData(defaultList)
                }
                emit(Resource.loading(false))
                emit(Resource.success(data = true))
            } catch (e: Exception) {
                emit(Resource.error(e.localizedMessage, e))
            }
        }

    private suspend fun insertDefalutData(defalutList: List<Balance>) {
        return repository.insertBalances(dataList = defalutList)
    }
}