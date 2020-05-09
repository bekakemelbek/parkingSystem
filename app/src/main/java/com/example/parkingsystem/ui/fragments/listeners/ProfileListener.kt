package com.example.parkingsystem.ui.fragments.listeners

import com.example.parkingsystem.network.model.Order

interface ProfileListener {
    fun onProgress()
    fun onFailureList()
    fun onSuccessList(list:List<Order?>)
    fun onFailureUserText()
    fun onSuccessUserText(wallet:String,carModel:String,carNumber:String)
    fun userChangeListener(type:String)
    fun carNumberChangeListener()
}