package com.example.parkingsystem.ui.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.parkingsystem.R
import com.example.parkingsystem.databinding.ActivityRegistrationBinding
import com.example.parkingsystem.ui.activity.listeners.RegistrationListener
import com.example.parkingsystem.ui.activity.viewmodels.RegisterViewModel
import com.example.parkingsystem.util.indeterminateProgressDialog
import com.example.parkingsystem.util.toast

class RegistrationActivity : AppCompatActivity(),RegistrationListener {

    private var progressBar:ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding:ActivityRegistrationBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration)
        val viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        binding.viewmodel = viewModel

        viewModel.listener = this
        viewModel.initActivity(this)
    }

    override fun onSuccess() {

        progressBar!!.dismiss()
        applicationContext.toast("Успешно")
        Intent(this,BaseActivity::class.java).also {
            startActivity(it)
        }
    }

    override fun onFailure() {
        progressBar!!.dismiss()
        applicationContext.toast("Ошибка отправки данных. Повторите по позже")
    }

    override fun onProgress() {
        progressBar = indeterminateProgressDialog(R.string.processing_3xdot)
        progressBar!!.show()
    }
}
