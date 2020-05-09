package com.example.parkingsystem.ui.fragments.viewmodels

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.parkingsystem.network.model.Parkings
import com.example.parkingsystem.network.repositories.ParkingsRepo
import com.example.parkingsystem.ui.activity.CurrentStatusActivity
import com.example.parkingsystem.ui.fragments.listeners.MapListener
import com.example.parkingsystem.util.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel: ViewModel(){

    private val markers = hashMapOf<String,Parkings>()

    var listener:MapListener? = null

    fun loadAllParkings() {
        CoroutineScope(IO).launch {
            val response = ParkingsRepo().parkings(User.id,User.token)
            val body = response.body()
            if(response.isSuccessful){
                if(body!!.success){
                    withContext(Main){
                        listener!!.onSuccess(body.data!!,markers)
                        Log.e("ASD",body.data.toString())
                    }
                }else{
                    withContext(Main){
                        listener!!.onFailure()
                    }
                }
            } else {
                withContext(Main) {
                    listener!!.onFailure()
                }
            }
        }
    }

    fun currentStatusClicked(view: View){
        Intent(view.context,CurrentStatusActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    fun refreshClicked(view: View){
        listener!!.onProgress()
        loadAllParkings()
    }
}