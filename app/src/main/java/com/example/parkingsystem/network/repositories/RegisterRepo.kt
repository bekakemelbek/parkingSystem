package com.example.parkingsystem.network.repositories

import com.example.parkingsystem.network.Api
import com.example.parkingsystem.network.model.RegisterInsideModel
import com.example.parkingsystem.network.model.RegisterQueryModel
import com.example.parkingsystem.network.model.RegisterResponseModel
import retrofit2.Response

class RegisterRepo {
    suspend fun register(data:RegisterQueryModel) : Response<RegisterResponseModel<RegisterInsideModel>> {
        return Api.create().register(data)
    }
}