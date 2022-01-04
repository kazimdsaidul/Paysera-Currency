package com.saidul.paysera.data.remote.model

data class ResposLatest(
    val base: String,
    val date: String,
    val rates: Rates,
    val success: Boolean,
    val timestamp: Int
)