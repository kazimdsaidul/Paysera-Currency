package com.saidul.paysera.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saidul.paysera.R
import com.saidul.paysera.domain.model.Balance
import com.saidul.paysera.presentation.adapter.AdapterBalance.MyView
import com.saidul.paysera.utility.NumberFormatter.formatTwoDecimalNumber

class AdapterBalance
    (
    private val list: List<Balance>
) : RecyclerView.Adapter<MyView>() {

    inner class MyView(view: View) : RecyclerView.ViewHolder(view) {

        var textView: TextView

        init {
            textView = view
                .findViewById<View>(R.id.tvBalance) as TextView
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyView {

        // Inflate item.xml using LayoutInflator
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.item_balaces,
                parent,
                false
            )

        // return itemView
        return MyView(itemView)
    }


    override fun onBindViewHolder(
        holder: MyView,
        position: Int
    ) {

        val item = list[position]
        val twoDigitsF: Float = formatTwoDecimalNumber(item.balance)


        holder.textView.text = "${twoDigitsF} ${item.currencyName}"
    }


    override fun getItemCount(): Int {
        return list.size
    }
}