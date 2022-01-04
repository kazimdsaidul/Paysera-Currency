package com.saidul.paysera.core.network.exceptions

import java.io.IOException

class NoInternetException : IOException() {

    override val message: String
        get() = "No Internet Connection"
}