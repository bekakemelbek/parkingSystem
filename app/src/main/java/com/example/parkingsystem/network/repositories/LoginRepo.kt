package com.example.parkingsystem.network.repositories

import com.example.parkingsystem.network.Api
import com.example.parkingsystem.network.model.LoginModel
import com.example.parkingsystem.network.model.RegisterResponseModel
import com.example.parkingsystem.network.model.User
import retrofit2.Response

class LoginRepo {
    suspend fun login(data: LoginModel) : Response<RegisterResponseModel<User>> {
        return Api.create().login(data)
    }
}