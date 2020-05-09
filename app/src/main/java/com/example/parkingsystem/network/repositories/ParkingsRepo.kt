package com.example.parkingsystem.network.repositories

import com.example.parkingsystem.network.Api
import com.example.parkingsystem.network.model.*
import retrofit2.Response

class ParkingsRepo {
    suspend fun parkings(id:Int,token: String) : Response<RegisterResponseModel<List<Parkings?>>> {
        return Api.create().parkings(token,id)
    }

    suspend fun createOrder(order:CreateOrder) : Response<RegisterResponseModel<ResponseCreateOrder>> {
        return Api.create().createOrder(order)
    }

    suspend fun getOrders(id:Int,token: String) : Response<RegisterResponseModel<List<Order?>>> {
        return Api.create().getAllOrders(token,id)
    }

    suspend fun getCurrentParking(id:Int,token: String) : Response<RegisterResponseModel<CurrentParking>> {
        return Api.create().getCurrentParking(token,id)
    }

    suspend fun cancelOrder(data:CancelOrder) : Response<RegisterResponseModel<CurrentParking?>> {
        return Api.create().cancelOrder(data)
    }
}