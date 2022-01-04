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
    val getBalanceUseCase: GetBalanceUseCase,
    val defaultBalanceAddUseCase: DefaultBalanceAddUseCase,
    val calculationUseCase: CalculationUseCase,
    val convertUseCase: CalculationAndConvertUseCase
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

    fun getBalanceList(): Flow<List<Balance>> {
        return getBalanceUseCase.invoke()
    }

    private val _convertAmount = MutableSharedFlow<Resource<Any>>()
    val convertAmount: SharedFlow<Resource<Any>> = _convertAmount

    private val _defaultBalanceAdded = MutableSharedFlow<Resource<Any>>()
    val defaultBalanceAdded: SharedFlow<Resource<Any>> = _defaultBalanceAdded
    fun addDefaultBalance() {
        viewModelScope.launch {

            defaultBalanceAddUseCase.invoke(true).collect {
                _defaultBalanceAdded.emit(it)
            }
        }
    }

    fun calculation(
        keyIsSellType: String,
        sellBalance: Balance,
        reciveBanalce: Balance,
        amount: Double
    ) {

        viewModelScope.launch {
            calculationUseCase.calculation(keyIsSellType, sellBalance, reciveBanalce, amount)
                .collect {
                    _convertAmount.emit(it)
                }
        }

    }

    private val _convertMessage = MutableSharedFlow<Resource<Any>>()
    val convertMessage: SharedFlow<Resource<Any>> = _convertMessage

    fun submit(
        keyIsSellType: String,
        selectedItemPosition: Int,
        selectedItemPosition1: Int,
        amount: Double,
        convertyAmount: Double
    ) {

        val sellCurrency = currencyTypeList.get(selectedItemPosition).currencyName
        val reciverCurrency = currencyTypeList.get(selectedItemPosition1).currencyName

        viewModelScope.launch {
            convertUseCase.invoke(
                keyIsSellType,
                sellCurrency,
                reciverCurrency,
                amount,
                convertyAmount
            )
                .collect {
                    _convertMessage.emit(it);
                }
        }

    }

}