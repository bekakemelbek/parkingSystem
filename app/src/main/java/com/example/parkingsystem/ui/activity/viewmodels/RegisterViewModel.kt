package com.example.parkingsystem.ui.activity.viewmodels

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.parkingsystem.network.model.RegisterQueryModel
import com.example.parkingsystem.network.repositories.RegisterRepo
import com.example.parkingsystem.ui.activity.listeners.RegistrationListener
import com.example.parkingsystem.util.User
import com.example.parkingsystem.util.toEditable
import com.example.parkingsystem.util.toast
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel: ViewModel() {
    var isOld = false
    var oldRegion:String? = null
    var oldNumber:String? = null
    var oldText:String? = null
    var newRegion:String? = null
    var newNumber:String? = null
    var newText:String? = null
    var name:String? = null
    var carmodel:String? = null
    var password:String? = null
    var repeatPassword:String? = null
    var number:String?= null

    var listener:RegistrationListener? = null

    fun initActivity(context:Context) {
        number = (context as Activity).intent.getStringExtra("number")!!
        context.numberEdit.text = context.intent.getStringExtra("number")!!.toEditable()
        context.numberEdit.isEnabled = false
    }


    fun registerButtonClick(view: View) {
        if (checkIsFormCorrect(view.context)) {
            listener!!.onProgress()
            var carNumber = ""
            if(!isOld){
                carNumber = newNumber+newText+newRegion
            }else{
                carNumber = oldRegion+oldNumber+oldText
            }
            CoroutineScope(IO).launch {
                val data = RegisterQueryModel(number!!,password!!,carNumber,carmodel!!)
                val response = RegisterRepo().register(data)
                if(response.isSuccessful){
                    if(response.body()!!.success){//server request is successfull
                        User.name = name!!// save on kotpref
                        User.carNumber = carNumber
                        User.phone = number!!
                        User.balance = response.body()!!.data!!.wallet
                        User.id = response.body()!!.data!!.id
                        User.carModel = carmodel!!
                        User.token = response.body()!!.data!!.token
                        withContext(Main) {
                            listener!!.onSuccess()
                        }
                    } else {
                        withContext(Main) {
                            listener!!.onFailure()
                        }
                    }
                }else{
                    withContext(Main) {
                        listener!!.onFailure()
                    }
                }

            }
        }

    }

    private fun checkIsFormCorrect(context: Context):Boolean{

        if(!isOld && (newRegion.isNullOrEmpty() || newNumber.isNullOrEmpty()
                    || newText.isNullOrEmpty() || (newRegion!!.length != 2) &&
            (newText!!.length != 3) || (newNumber!!.length != 3))) {
            context.toast("Введите правильный номер машины")
            return false
        }

        if(isOld && (oldRegion.isNullOrEmpty() || oldNumber.isNullOrEmpty()
                    || oldText.isNullOrEmpty() || (oldRegion!!.length != 1) &&
                    (newText!!.length != 3) || (newNumber!!.length != 3))) {
            context.toast("Введите правильный номер машины")
            return false
        }

        if(name.isNullOrEmpty() ||
            password.isNullOrEmpty() || repeatPassword.isNullOrEmpty() ||
            carmodel.isNullOrEmpty()){
            context.toast("Заполните все поля")
            return false
        }
        if(password!!.length < 5){
            context.toast("Пароль должен иметь минимум 5 символов")
            return false
        }
        if(password != repeatPassword ) {
            context.toast("Пароль не совпадают")
            return false
        }

        return  true
    }

    fun refreshButtonClicked(view:View) {
        val context = view.context
        if(!isOld){
            isOld = true
            (context as Activity).oldCarNumberConstraint.visibility = View.VISIBLE
            context.newCarNumberConstraint.visibility = View.GONE
        }else{
            isOld = false
            (context as Activity).oldCarNumberConstraint.visibility = View.GONE
            context.newCarNumberConstraint.visibility = View.VISIBLE
        }
    }}