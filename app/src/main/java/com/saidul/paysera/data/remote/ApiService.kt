package com.saidul.paysera.data.remote

import com.saidul.paysera.data.remote.model.ResposLatest
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("latest")
    suspend fun getCountries(@Query("access_key") access_key: String): Response<ResposLatest>

    @GET("convert")
    suspend fun convert(
        @Query("access_key") access_key: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: String
    ): Response<ResposLatest>
}