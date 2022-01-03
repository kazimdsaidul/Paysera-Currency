package com.saidul.paysera.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rate(
    val currencyName: String,
    val rate: Double,
    @PrimaryKey val id: Int? = null
)

