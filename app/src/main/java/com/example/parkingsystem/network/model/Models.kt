package com.example.parkingsystem.network.model

import com.google.gson.annotations.SerializedName

data class RegisterQueryModel(val phone:String,
                              val password:String,
                              val carNumber: String,
                              val carModel: String)

data class RegisterResponseModel<out T>(val success:Boolean,
                                 val message:String,
                                 val data:T?)

data class RegisterInsideModel(val phone:String,
                               val password: String,
                               val token:String,
                               val carModel: String,
                               val carNumber: String,
                               val wallet: String,
                               val updated_at: String,
                               val created_at: String,
                               val id: Int)

data class Parkings(val id:Int,
                    val name:String,
                    val image:String,
                    val price:String,
                    val places:String,
                    val occupied_places:String,
                    val longtitude:String,
                    val latitude:String,
                    val description:String,
                    val created_at:String,
                    val updated_at:String)

data class Order(val id:Int,
                 val userId:String,
                 val parkId:String,
                 val incomingTime:String,
                 val outgoingTime:String?,
                 val created_at:String,
                 val updated_at:String)

data class CreateOrder(val userId:String,
                 val parkId:String,
                 val token:String,
                 val hours:String)

data class CancelOrder(val userId:String,
                       val orderId:String,
                       val token:String)

data class CurrentParking(val parkId:String,
                       val name:String,
                       val description:String,
                       val incomingTime:String)

data class ResponseCreateOrder(val userId:String,
                       val parkId:String,
                       val date:String,
                       val updated_at: String,
                       val created_at: String,
                       val id :Int)

data class User(val id:Int,
                 val phone:String,
                 val token:String,
                 val carModel:String,
                 val carNumber:String,
                 val wallet:String)

data class LoginModel(val phone:String,
                val password:String)

data class Orders(
    @SerializedName("orders")
    val orders:MutableList<Order?>)