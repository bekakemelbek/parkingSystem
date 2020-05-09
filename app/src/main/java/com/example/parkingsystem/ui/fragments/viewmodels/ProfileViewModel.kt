package com.example.parkingsystem.ui.fragments.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.parkingsystem.network.repositories.ParkingsRepo
import com.example.parkingsystem.network.repositories.UserRepo
import com.example.parkingsystem.ui.fragments.listeners.ProfileListener
import com.example.parkingsystem.util.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel:ViewModel() {

    var listener:ProfileListener? = null

    fun makeRequest() {
        listener!!.onProgress()
        CoroutineScope(IO).launch {
            val responseOrder = ParkingsRepo().getOrders(User.id,User.token)

            val bodyOrder = responseOrder.body()
            if(responseOrder.isSuccessful){
                if(bodyOrder!!.success){
                    withContext(Main){
                        listener!!.onSuccessList(bodyOrder.data!!)
                    }
                }else{
                    withContext(Main){
                        listener!!.onFailureList()
                    }
                }
            }else{
                withContext(Main){
                    listener!!.onFailureList()
                }
            }


            val responseUser = UserRepo().getUser(User.token,User.id)
            val bodyUser = responseUser.body()
            if(responseUser.isSuccessful){
                if(bodyUser!!.success){
                    withContext(Main){
                        listener!!.onSuccessUserText(bodyUser.data!!.wallet,bodyUser.data.carModel,bodyUser.data.carNumber)
                    }
                }else{
                    withContext(Main){
                        listener!!.onFailureUserText()
                    }
                }
            }else{
                withContext(Main){
                    listener!!.onFailureUserText()
                }
            }

        }
    }

    fun nameChangeClicked (view:View){
        listener!!.userChangeListener("Введите имя")
    }

    fun balanceClicked (view: View){
        listener!!.userChangeListener("Сумма пополнения")
    }

    fun carNumberChange (view: View){
        listener!!.carNumberChangeListener()
    }

    fun carModelChange (view:View){
        listener!!.userChangeListener("Введите модель машины")
    }

    fun refreshClicked (view: View){
        makeRequest()
    }
}