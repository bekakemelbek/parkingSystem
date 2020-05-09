package com.example.parkingsystem.ui.fragments.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.parkingsystem.network.model.LoginModel
import com.example.parkingsystem.network.repositories.LoginRepo
import com.example.parkingsystem.ui.fragments.listeners.LoginListener
import com.example.parkingsystem.util.User
import com.example.parkingsystem.util.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel:ViewModel() {

    var number:String? = null
    var password:String? = null

    var listener:LoginListener? = null

    fun onRegisterClicked(view: View) {
        if(!number.isNullOrEmpty() && !password.isNullOrEmpty()) {
            CoroutineScope(IO).launch {
                val data = LoginModel(number!!,password!!)
                val response = LoginRepo().login(data)
                withContext(Main){
                    listener!!.onProgress()
                }
                if(response.isSuccessful){
                    if(response.body()!!.success){//server request is successfull
                        User.name = ""// save on kotpref
                        User.carNumber = response.body()!!.data!!.carNumber
                        User.phone = number!!
                        User.id = response.body()!!.data!!.id
                        User.carModel = response.body()!!.data!!.carModel
                        User.token = response.body()!!.data!!.token
                        User.balance = response.body()!!.data!!.wallet
                        withContext(Main){
                            listener!!.onSuccess()
                        }
                    }else{
                        withContext(Main){
                            listener!!.onFailure()
                        }
                    }
                }else {
                    withContext(Main){
                        listener!!.onFailure()
                    }
                }

            }
        }else{
            view.context.toast("Заполните все поля!")
        }
    }
}