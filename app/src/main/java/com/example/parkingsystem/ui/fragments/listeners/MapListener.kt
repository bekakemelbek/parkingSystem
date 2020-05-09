package com.example.parkingsystem.ui.fragments.listeners

import com.example.parkingsystem.network.model.Parkings

interface MapListener {
    fun onFailure()
    fun onSuccess(list:List<Parkings?>,markers:HashMap<String,Parkings>)
    fun onProgress()
}