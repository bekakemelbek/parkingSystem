package com.example.parkingsystem.ui.activity.viewmodels

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.multidex.MultiDex
import com.chibatching.kotpref.Kotpref
import com.example.parkingsystem.ui.activity.BaseActivity
import com.example.parkingsystem.util.User
import com.example.parkingsystem.util.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainViewModel:ViewModel() {

    fun initActivity(context: Context) {
        val adapter = ViewPagerAdapter((context as AppCompatActivity).supportFragmentManager)
        context.tabs.setupWithViewPager(context.viewPager)
        context.viewPager.adapter = adapter
        Kotpref.init(context)
        MultiDex.install(context)
        if(User.id != 0){
            Intent(context,BaseActivity::class.java).also {
                context.startActivity(it)
            }
        }
    }
}