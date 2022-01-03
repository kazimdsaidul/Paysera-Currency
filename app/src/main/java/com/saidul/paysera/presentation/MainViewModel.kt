package com.saidul.paysera.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saidul.paysera.core.Resource
import com.saidul.paysera.domain.model.Balance
import com.saidul.paysera.domain.usecase.CurrencyConvertUseCase
import com.saidul.paysera.domain.usecase.CurrencyLatestUseCase
import com.saidul.paysera.domain.usecase.GetBalanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val currencyLatestUseCase: CurrencyLatestUseCase,
    val currencyConvertUseCase: CurrencyConvertUseCase,
    val getBalanceUseCase: GetBalanceUseCase
) : ViewModel() {

    private val _latestCurrencyFlow = MutableSharedFlow<Resource<Any>>()
    val latestCurrency: SharedFlow<Resource<Any>> = _latestCurrencyFlow

    fun getLatest() {
        viewModelScope.launch {
            currencyLatestUseCase.invoke().collect {
                _latestCurrencyFlow.emit(it)
            }
        }
    }

    fun convert(from: String, to: String, amount: Double) {
        viewModelScope.launch {
            currencyConvertUseCase.invoke(from = from, to = to, amount = amount).collect {

            }
        }
    }

    fun getBalanceList(): Flow<List<Balance>> {
        return getBalanceUseCase.invoke()
    }

}