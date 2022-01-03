package com.saidul.paysera.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saidul.paysera.core.Resource
import com.saidul.paysera.domain.model.Balance
import com.saidul.paysera.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val latestCurrencyRateUseCase: LatestCurrencyRateUseCase,
    val currencyConvertUseCase: CurrencyConvertUseCase,
    val getBalanceUseCase: GetBalanceUseCase,
    val defaultBalanceAddUseCase: DefaultBalanceAddUseCase,
    val getCalcuationUseCase: GetCalcuationUseCase
) : ViewModel() {

    lateinit var currencyTypeList: List<Balance>
    private val _latestCurrencyFlow = MutableSharedFlow<Resource<Any>>()
    val latestCurrency: SharedFlow<Resource<Any>> = _latestCurrencyFlow

    fun getLatest() {
        viewModelScope.launch {
            latestCurrencyRateUseCase.invoke().collect {
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

    private val _convertAmount = MutableSharedFlow<Double>()
    val convertAmount: SharedFlow<Double> = _convertAmount

    fun calculation(
        keyIsSellType: String,
        selectedItemPosition: Int,
        selectedItemPosition1: Int,
        amount: Double
    ) {
        val sellCurrency = currencyTypeList.get(selectedItemPosition).currencyName
        val reciverCurrency = currencyTypeList.get(selectedItemPosition1).currencyName

        viewModelScope.launch {
            getCalcuationUseCase.calculation(keyIsSellType, sellCurrency, reciverCurrency, amount)
                .collect {
                    _convertAmount.emit(it)

                }
        }

    }


    private val _defaultBalanceAdded = MutableSharedFlow<Resource<Any>>()
    val defaultBalanceAdded: SharedFlow<Resource<Any>> = _defaultBalanceAdded
    fun addDefaultBalance() {
        viewModelScope.launch {
            val data = mutableListOf<Balance>()
            data.add(Balance("EUR", 1000.00))
            data.add(Balance("USD", 0.00))
            data.add(Balance("JPY", 0.00))
            data.add(Balance("GBP", 0.00))
            defaultBalanceAddUseCase.invoke(true, data).collect {
                _defaultBalanceAdded.emit(it)
            }
        }
    }

}