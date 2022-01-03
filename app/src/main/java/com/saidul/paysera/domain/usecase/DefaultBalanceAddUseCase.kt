package com.saidul.paysera.domain.usecase

import com.saidul.paysera.core.Resource
import com.saidul.paysera.domain.model.Balance
import com.saidul.paysera.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DefaultBalanceAddUseCase(val repository: CurrencyRepository) {
    operator fun invoke(isNeedClearData: Boolean, defalutList: List<Balance>): Flow<Resource<Any>> =
        flow {
            try {
                emit(Resource.loading(true))
                if (isNeedClearData) {
                    repository.deleteBalanceData()
                    insertDefalutData(defalutList)
                } else {
                    insertDefalutData(defalutList)
                }
                emit(Resource.loading(false))
                emit(Resource.success(data = true))
            } catch (e: Exception) {
                emit(Resource.error(e.localizedMessage, e))
            } as Unit
        } as Flow<Resource<Any>>

    private suspend fun insertDefalutData(defalutList: List<Balance>) {
        return repository.insertBalances(dataList = defalutList)
    }
}