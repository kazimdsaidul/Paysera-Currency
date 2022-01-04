package com.saidul.paysera.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction(
    val toCurrency: String,
    val fromCurrency: String,
    val toAmount: Double,
    val fromAmount: Double,
//    val date: Date,
    val currentBalance: Double,
    val commissionFee: Double,


    @PrimaryKey val id: Int? = null
)

