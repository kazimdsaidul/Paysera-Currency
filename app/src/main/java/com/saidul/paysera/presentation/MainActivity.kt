package com.saidul.paysera.presentation


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.saidul.paysera.R
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
class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val myViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myViewModel.getLatest()

        initiOwnViewState()
        initialControls()

    }

    private fun initiOwnViewState() {
        lifecycleScope.launch {
            myViewModel.getBalanceList().collect {
                myViewModel.currencyTypeList = it
                if (it.isNotEmpty()) {
                    textViewMyBalances.text = getString(R.string.my_balances)
                    cLSell.visibility = View.VISIBLE
                    clRecive.visibility = View.VISIBLE
                    tvCurrencyLabel.visibility = View.VISIBLE
                    btnSumbit.visibility = View.VISIBLE

                    setAapter(it)
                    setSellerSpiner(it)
                    setReceiveSpiner(it)
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
                etTextRecive.setText("${NumberFormatter.formatTwoDecimalNumber(it)}")
            }
        }
    }


    private fun setSellerSpiner(it: List<Balance>) {
        val first = it.first()
        val onlyFirstItemList = mutableListOf<Balance>()
        onlyFirstItemList.add(first)
        spinnerSell.onItemSelectedListener = this

        val customAdapter = CustomDropDownAdapter(applicationContext, onlyFirstItemList)
        spinnerSell.adapter = customAdapter

    }

    private fun setReceiveSpiner(it: List<Balance>) {
        spinnerReciver.onItemSelectedListener = this
        val customAdapter = CustomDropDownAdapter(applicationContext, it)
        spinnerReciver.adapter = customAdapter

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
    }

    private fun calculation(s: String) {
        if (s != "" && s.isNotEmpty()) {
            myViewModel.calculation(
                KEY_IS_SELL_TYPE,
                spinnerSell.selectedItemPosition,
                spinnerReciver.selectedItemPosition,
                s.toDouble()
            )
        } else {
            etTextRecive.setText("")
        }
    }

    fun setAapter(list: List<Balance>) {

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