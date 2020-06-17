package com.example.parkingsystem.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.parkingsystem.R
import com.example.parkingsystem.databinding.ActivityBaseBinding
import com.example.parkingsystem.ui.activity.viewmodels.BaseViewModel
import com.example.parkingsystem.util.FirebaseService
import com.example.parkingsystem.util.User
import com.google.firebase.messaging.FirebaseMessaging


class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding:ActivityBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base)
        val viewModel = ViewModelProviders.of(this).get(BaseViewModel::class.java)
        binding.viewmodel = viewModel
        viewModel.initActivity(this)
        startService(Intent(this, FirebaseService::class.java))
        FirebaseMessaging.getInstance().subscribeToTopic(User.id.toString())
            .addOnCompleteListener { task ->
                var msg = getString(R.string.msg_subscribed)
                if (!task.isSuccessful) {
                    msg = getString(R.string.msg_subscribe_failed)
                    Log.e("ASD123",User.id.toString())
                }
            }
        //(map as SupportMapFragment).getMapAsync(this)
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}
