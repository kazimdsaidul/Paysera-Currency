package com.saidul.paysera.core

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    enum class Status {
        START,
        SUCCESS,
        ERROR,
        LOADING,
        FAILURE
    }

    companion object {

        fun <T> start(data: T?): Resource<T> {
            return Resource(Status.START, data, null)
        }

        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, message)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T> failed(message: String, data: T? = null): Resource<T> {
            return Resource(Status.FAILURE, data, message)
        }
    }

    enum class FailStatus


}