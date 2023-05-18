package com.example.apiapplication.data.api

import com.example.apiapplication.data.models.DotaUserRaspberry
import com.example.apiapplication.data.models.FirebaseUserRaspberry
import com.example.apiapplication.data.models.Hero
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RaspberryAPI {

    @GET("request")
    suspend fun getSteamIDProfile(@Query("key") key: CharSequence, @Query("id") id: CharSequence): DotaUserRaspberry

    @GET("request")
    suspend fun getFirebaseProfile(@Query("key") key: CharSequence, @Query("id") id: CharSequence): FirebaseUserRaspberry

    @Headers("Content-Type: application/json")
    @POST("insert")
    suspend fun postSteamIDProfile(@Body steamProfile: DotaUserRaspberry?): Response<DotaUserRaspberry>
}