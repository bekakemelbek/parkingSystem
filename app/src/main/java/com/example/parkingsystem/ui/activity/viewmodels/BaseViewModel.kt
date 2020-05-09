package com.example.parkingsystem.ui.activity.viewmodels

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.parkingsystem.R
import com.example.parkingsystem.ui.fragments.MapFragment
import com.example.parkingsystem.ui.fragments.ProfileFragment
import kotlinx.android.synthetic.main.activity_base.*

class BaseViewModel: ViewModel() {
    private val mapFragment = MapFragment()
    private val profileFragment = ProfileFragment()
    private var active:Fragment? = MapFragment()

    fun initActivity(context: Context){

        val fm = (context as AppCompatActivity).supportFragmentManager
        fm.beginTransaction().add(R.id.main_content, mapFragment,"1").commit()
        fm.beginTransaction().add(R.id.main_content, profileFragment,"2").hide(profileFragment).commit()
        (context as Activity).menu.setOnNavigationItemSelectedListener {
            it.setChecked(true)
             if (it.itemId == R.id.action_map) {
                 Log.e("asd",active.toString())
                 fm.beginTransaction()
                     .hide(active!!)
                     .show(mapFragment)
                     .commit()
                 active = mapFragment
                 return@setOnNavigationItemSelectedListener true
             }else if (it.itemId == R.id.action_profile){
                 Log.e("asd1",active.toString())
                 fm.beginTransaction()
                     .hide(active!!)
                     .show(profileFragment)
                     .commit()
                 active = profileFragment
                 return@setOnNavigationItemSelectedListener true
             }
            return@setOnNavigationItemSelectedListener false
        }
    }

}