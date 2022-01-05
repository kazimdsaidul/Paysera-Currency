package com.saidul.paysera.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction(
    val toCurrency: String,
    val fromCurrency: String,
    val amount: Double,
    val previousBanlace: Double,
    val currentBalance: Double,
    val commissionFee: Double,


    @PrimaryKey val id: Int? = null
)

