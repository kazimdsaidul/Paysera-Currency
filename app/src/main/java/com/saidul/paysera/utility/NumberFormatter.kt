package com.saidul.paysera.utility

import java.text.DecimalFormat

object NumberFormatter {

    fun formatTwoDecimalNumber(amount: Double): Float {
        val decimalFormat = DecimalFormat("#.##")
        val twoDigitsF: Float = java.lang.Float.valueOf(decimalFormat.format(amount))
        return twoDigitsF
    }
}