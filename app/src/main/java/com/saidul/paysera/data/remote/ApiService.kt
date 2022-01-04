package com.saidul.paysera.data.remote

import com.saidul.paysera.data.remote.model.ResposLatest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("latest")
    suspend fun latest(@Query("access_key") accessKey: String): Response<ResponseBody>

    @GET("convert")
    suspend fun convert(
        @Query("access_key") accessKey: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double
    ): Response<ResposLatest>
}