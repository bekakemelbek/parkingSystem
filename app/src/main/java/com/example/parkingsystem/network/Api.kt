package com.example.parkingsystem.network

import com.example.parkingsystem.network.model.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface Api {

    @POST("createUser")
    suspend fun register(
        @Body data:RegisterQueryModel
    ): Response<RegisterResponseModel<RegisterInsideModel>>

    @POST("createOrder")
    suspend fun createOrder(
        @Body body:CreateOrder
    ): Response<RegisterResponseModel<ResponseCreateOrder>>

    @POST("login")
    suspend fun login(
        @Body body:LoginModel
    ): Response<RegisterResponseModel<User>>

    @GET("getAllParks")
    suspend fun parkings(
        @Query("token") token:String,
        @Query("id") id:Int
    ): Response<RegisterResponseModel<List<Parkings?>>>

    @GET("getAllOrders")
    suspend fun getAllOrders(
        @Query("token") token:String,
        @Query("id") id:Int
    ): Response<RegisterResponseModel<List<Order?>>>

    @GET("getUserStatus")
    suspend fun getCurrentParking(
        @Query("token") token:String,
        @Query("id") id:Int
    ): Response<RegisterResponseModel<CurrentParking>>

    @POST("cancelOrder")
    suspend fun cancelOrder(
        @Body body:CancelOrder
    ): Response<RegisterResponseModel<CurrentParking?>>

    @PATCH("updateBalance")
    suspend fun updateBalance(
        @Query("id") id:Int,
        @Query("token") token:String,
        @Query("wallet") wallet:String
    ): Response<RegisterResponseModel<User>>

    @GET("getUserInfo")
    suspend fun getUserInfo(
        @Query("token") token:String,
        @Query("id") id:Int
    ): Response<RegisterResponseModel<User>>

    companion object {
        fun create():Api{
            return Retrofit.Builder()
                .baseUrl("https://19e0105d.ngrok.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
        }
    }
}