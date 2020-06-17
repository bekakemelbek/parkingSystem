package com.example.parkingsystem.ui.fragments.viewmodels

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.parkingsystem.ui.activity.RegistrationActivity
import com.example.parkingsystem.ui.fragments.listeners.RegisterListener
import com.example.parkingsystem.util.toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_registration.view.*
import java.util.concurrent.TimeUnit

class RegistrationViewModel:ViewModel() {

    var number:String?=null
    var code:String?=null
    var listener: RegisterListener? = null
    var receivedCode:String? = null
    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            receivedCode = credential.smsCode
            listener!!.onSuccess()
        }

        override fun onVerificationFailed(e: FirebaseException) {
            listener!!.onFailure()
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            Log.e("ASD","Send")
        }
    }

    fun onButtonClicked(view: View) {
        if(view.registerButton.text == "Отправить сообщение"){

            if(!number.isNullOrEmpty() && number!!.contains("+7") && (number!!.length == 12)){
                listener!!.onProgress()
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    number!!, // Phone number to verify
                    60, // Timeout duration
                    TimeUnit.SECONDS, // Unit of timeout
                    view.context as Activity, // Activity (for callback binding)
                    callbacks)
            }else{
                view.context.toast("Введите правильный номер!")
            }
        }else{
            if(!code.isNullOrEmpty() && !receivedCode.isNullOrEmpty() && (code == receivedCode)){
                view.context.toast("Успешно")
                Intent(view.context, RegistrationActivity::class.java).also {
                    it.putExtra("number",number!!)
                    view.context.startActivity(it)
                }
            }else{
                view.context.toast("Неправильно введенный данные!")
            }
        }
    }


}