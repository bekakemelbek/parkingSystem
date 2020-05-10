package com.example.parkingsystem.ui.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.parkingsystem.R
import com.example.parkingsystem.databinding.FragmentLoginBinding
import com.example.parkingsystem.ui.activity.BaseActivity
import com.example.parkingsystem.ui.fragments.listeners.LoginListener
import com.example.parkingsystem.ui.fragments.viewmodels.LoginViewModel
import com.example.parkingsystem.util.indeterminateProgressDialog
import com.example.parkingsystem.util.toast


class LoginFragment : Fragment(),LoginListener {

    private var progressBar: ProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding:FragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        val viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding.viewmodellogin = viewModel
        viewModel.listener = this
        return binding.root
    }

    override fun onProgress() {
        progressBar = context!!.indeterminateProgressDialog(R.string.processing_3xdot)
        progressBar!!.show()
        Log.e("ASD","asdprog")
    }

    override fun onSuccess() {
        progressBar!!.dismiss()
        Log.e("ASD","asdprog")
        context!!.toast("Успешно")
        Intent(context, BaseActivity::class.java).also {
            startActivity(it)
        }
    }

    override fun onFailure() {
        progressBar!!.dismiss()
        context!!.toast("Ошибка!! Неправильный логин или пароль")
    }
}
