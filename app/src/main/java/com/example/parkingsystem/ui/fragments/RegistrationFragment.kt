package com.example.parkingsystem.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.parkingsystem.R
import com.example.parkingsystem.databinding.FragmentRegistrationBinding
import com.example.parkingsystem.ui.fragments.listeners.RegisterListener
import com.example.parkingsystem.ui.fragments.viewmodels.RegistrationViewModel
import com.example.parkingsystem.util.toast
import kotlinx.android.synthetic.main.fragment_registration.*


class RegistrationFragment : Fragment(), RegisterListener {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding:FragmentRegistrationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false)
        val viewModel = ViewModelProviders.of(this).get(RegistrationViewModel::class.java)
        binding.viewmodel = viewModel
        viewModel.listener = this

        return binding.root
    }

    companion object {

    }

    override fun onProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        progressBar.visibility = View.GONE
        codeEdit.visibility = View.VISIBLE
        numberEdit.isEnabled = false
        registerButton.text = "Далее"
    }

    override fun onFailure() {
        progressBar.visibility = View.GONE
        context!!.toast("Ошибка по пробуйте позже")
    }
}
