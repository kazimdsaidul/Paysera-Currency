package com.saidul.paysera.presentation


import android.os.Bundle
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


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

            myViewModel.latestCurrency.collect {
                if (it.status == Resource.Status.SUCCESS) {
                    myViewModel.getBalanceList().collect {
                        setAapter(it)
                        setSellSpiner(it)
                    }
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

    private fun setSellSpiner(it: List<Balance>) {
        spinnerSell.setOnItemSelectedListener(this)

        val customAdapter = CustomDropDownAdapter(applicationContext, it);
        spinnerSell.setAdapter(customAdapter)

    }

    private fun initialControls() {

    }

    fun setAapter(list: List<Balance>) {

        val RecyclerViewLayoutManager = LinearLayoutManager(
            applicationContext
        )

        // Set LayoutManager on Recycler View

        // Set LayoutManager on Recycler View
        rvBalanceList.setLayoutManager(
            RecyclerViewLayoutManager
        )

        val adapter = AdapterBalance(list)


        val HorizontalLayout = LinearLayoutManager(
            this@MainActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        rvBalanceList.setLayoutManager(HorizontalLayout)

        // Set adapter on recycler view

        // Set adapter on recycler view
        rvBalanceList.setAdapter(adapter)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}