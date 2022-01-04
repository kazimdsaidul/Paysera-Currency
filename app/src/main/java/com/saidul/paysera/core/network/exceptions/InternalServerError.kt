package com.saidul.paysera.core.network.exceptions

import java.io.IOException

class InternalServerError : IOException() {

    override val message: String
        get() = "Internal Server Error"
}