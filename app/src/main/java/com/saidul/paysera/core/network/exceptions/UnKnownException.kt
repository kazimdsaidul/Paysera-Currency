package com.saidul.paysera.core.network.exceptions

class UnKnownException(val messageBody: String?) : Exception() {

    override val message: String?
        get() = messageBody
}