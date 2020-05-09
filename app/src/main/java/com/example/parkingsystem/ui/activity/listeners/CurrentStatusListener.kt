package com.example.parkingsystem.ui.activity.listeners

import com.example.parkingsystem.network.model.CurrentParking

interface CurrentStatusListener {
    fun onFailure()
    fun onProcess()
    fun onSuccess(data :CurrentParking)
    fun showDialog()
    fun onCancelSuccess()
    fun onCancelFailure()
}