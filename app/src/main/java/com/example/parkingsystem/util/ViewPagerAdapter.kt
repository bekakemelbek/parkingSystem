package com.example.parkingsystem.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.parkingsystem.ui.fragments.LoginFragment
import com.example.parkingsystem.ui.fragments.RegistrationFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    val titleArrays = mutableListOf<String>("Логин","Регистрация")

    override fun getPageTitle(position: Int): CharSequence? {
        return titleArrays[position]
    }

    override fun getItem(position: Int): Fragment {
        return if(position == 0) {
            LoginFragment()
        }else {
            RegistrationFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }
}