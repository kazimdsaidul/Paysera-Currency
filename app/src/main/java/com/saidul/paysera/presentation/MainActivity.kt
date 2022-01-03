package com.saidul.paysera.presentation


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saidul.paysera.core.Resource
import com.saidul.paysera.domain.model.Balance
import com.saidul.paysera.presentation.adapter.AdapterBalance
import com.saidul.paysera.presentation.adapter.CustomDropDownAdapter
import com.saidul.paysera.utility.NumberFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

const val KEY_IS_SELL_TYPE = "KEY_IS_SELL_TYPE"
const val KEY_IS_RECEIVE_TYPE = "KEY_IS_RECIVE_TYPE"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    private lateinit var recyclerView: RecyclerView
    private val myViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.saidul.paysera.R.layout.activity_main)


        myViewModel.getLatest()


        initiOwnViewState()
        initialControls()


    }

    private fun initiOwnViewState() {
        lifecycleScope.launch {
            myViewModel.getBalanceList().collect {
                myViewModel.currencyTypeList = it
                setAapter(it)
                setSellerSpiner(it)
                setReceiveSpiner(it)
            }


        }

        lifecycleScope.launch {
            myViewModel.convertAmount.collect {
                etTextRecive.setText("" + NumberFormatter.formatTwoDecimalNumber(it))
            }
        }









        lifecycleScope.launch {

            myViewModel.latestCurrency.collect {
                if (it.status == Resource.Status.SUCCESS) {

                }

                if (it.status == Resource.Status.FAILURE) {
//                    when (it.data) {
//
//                        else -> {
//                            dismissProgressDialog()
//                            showSimpleFailureDialogNewV2(it.message ?: "")
//                        }
//                    }
                }

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
                if (!s.equals("")) {
                    myViewModel.calculation(
                        KEY_IS_SELL_TYPE,
                        spinnerSell.selectedItemPosition,
                        spinnerReciver.selectedItemPosition,
                        s.toString().toDouble()
                    )
                }

            }

        })

        editTextSell.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                myViewModel.calculation(
                    KEY_IS_RECEIVE_TYPE,
                    spinnerSell.selectedItemPosition,
                    spinnerReciver.selectedItemPosition,
                    s.toString().toDouble()
                )
            }

        })
    }

    fun setAapter(list: List<Balance>) {

        val RecyclerViewLayoutManager = LinearLayoutManager(
            applicationContext
        )

        rvBalanceList.layoutManager = RecyclerViewLayoutManager

        val adapter = AdapterBalance(list)


        val HorizontalLayout = LinearLayoutManager(
            this@MainActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        rvBalanceList.layoutManager = HorizontalLayout

        rvBalanceList.adapter = adapter
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}