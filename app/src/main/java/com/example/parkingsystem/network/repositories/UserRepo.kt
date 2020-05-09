package com.example.parkingsystem.network.repositories

import com.example.parkingsystem.network.Api
import com.example.parkingsystem.network.model.RegisterResponseModel
import com.example.parkingsystem.network.model.User
import retrofit2.Response

class UserRepo {
    suspend fun getUser(token:String,id:Int) : Response<RegisterResponseModel<User>> {
        return Api.create().getUserInfo(token,id)
    }

    suspend fun updateBalance(id:Int,token: String,wallet:String) : Response<RegisterResponseModel<User>> {
        return Api.create().updateBalance(id,token,wallet)
    }
}