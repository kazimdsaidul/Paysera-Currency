package com.saidul.paysera.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Balance(
    val currencyName: String,
    val balance: Double,
    @PrimaryKey val id: Int? = null
)

