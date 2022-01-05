package com.saidul.paysera.presentation


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.saidul.paysera.R
import com.saidul.paysera.core.Resource
import com.saidul.paysera.core.base.BaseActivity
import com.saidul.paysera.domain.model.Balance
import com.saidul.paysera.presentation.adapter.AdapterBalance
import com.saidul.paysera.presentation.adapter.CustomDropDownAdapter
import com.saidul.paysera.utility.NumberFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_balaces.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

const val KEY_IS_SELL_TYPE = "KEY_IS_SELL_TYPE"

@AndroidEntryPoint
class MainActivity : BaseActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var customAdapterReceive: CustomDropDownAdapter
    private lateinit var customAdapterSell: CustomDropDownAdapter
    private val myViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initOwnViewState()
        initialControls()

    }

    private fun initOwnViewState() {
        lifecycleScope.launch {
            myViewModel.getBalanceList().collect {
                myViewModel.currencyTypeList = it
                if (it.isNotEmpty()) {
                    textViewMyBalances.text = getString(R.string.my_balances)
                    cLSell.visibility = View.VISIBLE
                    clRecive.visibility = View.VISIBLE
                    tvCurrencyLabel.visibility = View.VISIBLE
                    btnSumbit.visibility = View.VISIBLE

                    setBalanceAdapter(it)
                    setSellerSpinner(it)
                    setReceiveSpinner(it)
                } else {
                    textViewMyBalances.text = getString(R.string.click_to_show_balances)
                    cLSell.visibility = View.GONE
                    clRecive.visibility = View.GONE
                    tvCurrencyLabel.visibility = View.GONE
                    btnSumbit.visibility = View.GONE

                    runOnUiThread {
                        editTextSell.requestFocus()
                        val imm: InputMethodManager =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(editTextSell, InputMethodManager.SHOW_IMPLICIT)
                    }

                }
            }
        }

        lifecycleScope.launch {
            myViewModel.convertAmount.collect {
                if (it.status == Resource.Status.SUCCESS) {
                    etTextRecive.setText("${NumberFormatter.formatTwoDecimalNumber(it.data as Double)}")
                }

                handleFailureAndError(it)
            }
        }

        lifecycleScope.launch {
            myViewModel.convertMessage.collect {
                if (it.status == Resource.Status.SUCCESS) {
                    showSimpleFailureDialog(title = getString(R.string.currency_converted),
                        content = it.data.toString(),
                        getString(R.string.done),
                        false,
                        object : PositiveCallBack {
                            override fun onClick() {
                                editTextSell.setText("")
                            }
                        })
                }
                handleFailureAndError(it)
            }
        }

        lifecycleScope.launch {
            myViewModel.latestCurrency.collect {
                handleFailureAndError(it)
            }
        }


    }

    private fun handleFailureAndError(it: Resource<Any>) {
        if (it.status == Resource.Status.FAILURE) {
            showSnackBarMessage(it.message)
            etTextRecive.setText("")
        }
        if (it.status == Resource.Status.ERROR) {
            showSnackBarMessage(it.message)
            etTextRecive.setText("")
        }
    }


    private fun setSellerSpinner(it: List<Balance>) {
        val first = it.first()
        val onlyFirstItemList = mutableListOf<Balance>()
        onlyFirstItemList.add(first)
        spinnerSell.onItemSelectedListener = this

        customAdapterSell = CustomDropDownAdapter(applicationContext, onlyFirstItemList)
        spinnerSell.adapter = customAdapterSell

    }

    private fun setReceiveSpinner(it: List<Balance>) {
        val subList = it.subList(1, it.size)
        spinnerReciver.onItemSelectedListener = this
        customAdapterReceive = CustomDropDownAdapter(applicationContext, subList)
        spinnerReciver.adapter = customAdapterReceive
    }

    private fun initialControls() {
        textViewMyBalances.setOnClickListener {
            myViewModel.addDefaultBalance()
        }


        editTextSell.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable) {
                calculation(s.toString())
            }

        })

        btnSumbit.setOnClickListener {
            if (editTextSell.text.isNotBlank()) {

                val balanceSell =
                    customAdapterSell.getItem(spinnerSell.selectedItemPosition) as Balance
                val balanceReceive =
                    customAdapterReceive.getItem(spinnerReciver.selectedItemPosition) as Balance

                myViewModel.submit(
                    KEY_IS_SELL_TYPE,
                    balanceSell,
                    balanceReceive,
                    editTextSell.text.toString(),
                )
            } else {
                showSnackBarMessage("Invalid input")
            }


        }
    }

    private fun calculation(s: String) {
        if (s.isNotEmpty()) {
            val balanceSell =
                customAdapterSell.getItem(spinnerSell.selectedItemPosition) as Balance
            val balanceReceive =
                customAdapterReceive.getItem(spinnerReciver.selectedItemPosition) as Balance

            myViewModel.calculation(
                KEY_IS_SELL_TYPE,
                balanceSell,
                balanceReceive,
                amount = s
            )
        } else {
            etTextRecive.setText("")
        }
    }

    private fun setBalanceAdapter(list: List<Balance>) {
        val recyclerViewLayoutManager = LinearLayoutManager(
            applicationContext
        )
        rvBalanceList.layoutManager = recyclerViewLayoutManager

        val adapter = AdapterBalance(list)
        val horizontalLayout = LinearLayoutManager(
            this@MainActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        rvBalanceList.layoutManager = horizontalLayout
        rvBalanceList.adapter = adapter
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        calculation(editTextSell.text.toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>?) = Unit
}