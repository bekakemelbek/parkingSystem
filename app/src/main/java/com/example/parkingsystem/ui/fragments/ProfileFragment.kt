package com.example.parkingsystem.ui.fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListPopupWindow
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parkingsystem.R
import com.example.parkingsystem.databinding.FragmentProfileBinding
import com.example.parkingsystem.network.model.Order
import com.example.parkingsystem.network.repositories.UserRepo
import com.example.parkingsystem.ui.fragments.listeners.ProfileListener
import com.example.parkingsystem.ui.fragments.viewmodels.ProfileViewModel
import com.example.parkingsystem.util.ReportsAdapter
import com.example.parkingsystem.util.User
import com.example.parkingsystem.util.indeterminateProgressDialog
import com.example.parkingsystem.util.toast
import kotlinx.android.synthetic.main.car_number_change_popup.view.*
import kotlinx.android.synthetic.main.user_infochange_popup.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileFragment : Fragment(),ProfileListener {

    private var progressBar: ProgressDialog? = null
    private var binding:FragmentProfileBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        val viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        binding!!.viewmodel = viewModel
        viewModel.listener = this
        viewModel.makeRequest()

        return binding!!.root
    }

    override fun onProgress() {
        progressBar = activity!!.indeterminateProgressDialog(R.string.processing_3xdot)
        progressBar!!.show()
    }

    override fun onFailureList() {
        activity!!.toast("Ошибка загрузки списка отчетов!!")
    }

    override fun onSuccessList(list:List<Order?>) {
        binding!!.reportList.layoutManager = LinearLayoutManager(context)
        binding!!.reportList.addItemDecoration(
            DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL)
        );
        if(list.size != 0) {
            binding!!.reportList.adapter = ReportsAdapter(list,context!!)
        }else{
            binding!!.reportList.adapter = ReportsAdapter(mutableListOf(),context!!)
        }
    }

    override fun onFailureUserText() {
        progressBar!!.dismiss()
        context!!.toast("Ошибка загрузки информации о пользователе!!")
    }

    override fun onSuccessUserText(wallet:String,carModel:String,carNumber:String) {
        progressBar!!.dismiss()
        binding!!.name.text = "имя: "+User.name
        binding!!.balance.text = "баланс: $wallet"
        binding!!.carMark.text = "марка: $carModel"
        binding!!.carNumber.text = "номер машины: $carNumber"
    }

    override fun userChangeListener(type:String) {
        val inflater: LayoutInflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.user_infochange_popup,null)

        val popupWindow = PopupWindow()
        popupWindow.contentView = view
        popupWindow.width = ListPopupWindow.WRAP_CONTENT
        popupWindow.height = ListPopupWindow.WRAP_CONTENT
        view.edit.hint = type
        view.ok.setOnClickListener{
            if (view.edit.text.length > 0) {
                val progress = context!!.indeterminateProgressDialog()
                progress.show()
                if (type == "Введите имя") {
                    binding!!.name.text =  view.edit.text
                    progress.dismiss()
                    popupWindow.dismiss()
                }else if (type == "Введите модель машины"){
                    binding!!.carMark.text =  view.edit.text
                    progress.dismiss()
                    popupWindow.dismiss()
                }else if (type == "Сумма пополнения") {
                    CoroutineScope(IO).launch {
                        val response = UserRepo().updateBalance(User.id,User.token,view.edit.text.toString())
                        if(response.isSuccessful){
                            val body = response.body()
                            if(body!!.success){
                                withContext(Main){
                                    binding!!.balance.text =  body.data!!.wallet
                                    User.balance = body.data.wallet
                                    progress.dismiss()
                                    popupWindow.dismiss()
                                }
                            }else{
                                withContext(Main){
                                    context!!.toast("Ошибка пополнения!")
                                    popupWindow.dismiss()
                                }
                            }
                        }else {
                            withContext(Main){
                                context!!.toast("Отключен интернет!")
                                popupWindow.dismiss()
                            }

                        }
                    }

                }
                popupWindow.dismiss()
            }else{

            }


        }
        view.cancel.setOnClickListener{
            popupWindow.dismiss()
        }

        popupWindow.showAtLocation(
            binding!!.profileConstraint,
            Gravity.CENTER,
            0,
            0
        )
    }

    @SuppressLint("SetTextI18n")
    override fun carNumberChangeListener() {
        var isOld = false
        val inflater: LayoutInflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.car_number_change_popup,null)

        val popupWindow = PopupWindow()
        popupWindow.contentView = view
        popupWindow.width = ListPopupWindow.WRAP_CONTENT
        popupWindow.height = ListPopupWindow.WRAP_CONTENT
        view.okCar.setOnClickListener {
            if (checkIsFormCorrect(context!!,isOld,view.newCarNumberRegion.text.toString(),
                    view.newCarNumberNumbers.text.toString(),view.newCarNumberText.text.toString(),
                    view.oldCarNumberText.text.toString(),view.oldCarNumberNumbers.text.toString(),
                    view.oldCarNumberTexts.text.toString())) {
                //zapros

                if(isOld){
                    binding!!.carNumber.text = view.oldCarNumberText.text.toString()+view.oldCarNumberNumbers.text+ view.oldCarNumberTexts.text
                    popupWindow.dismiss()
                }else{
                    binding!!.carNumber.text = view.newCarNumberRegion.text.toString()+view.newCarNumberNumbers.text.toString()+ view.newCarNumberText.text.toString()
                    popupWindow.dismiss()
                }
            }
        }

        view.refresh.setOnClickListener{
            if(!isOld){
                isOld = true
                view.newCarNumberConstraint.visibility = View.GONE
                view.oldCarNumberConstraint.visibility = View.VISIBLE
            }else {
                isOld = false
                view.newCarNumberConstraint.visibility = View.VISIBLE
                view.oldCarNumberConstraint.visibility = View.GONE
            }

        }

        view.cancelCar.setOnClickListener{
            popupWindow.dismiss()
        }

        popupWindow.showAtLocation(
            binding!!.profileConstraint,
            Gravity.CENTER,
            0,
            0
        )
    }

    private fun checkIsFormCorrect(context: Context,isOld:Boolean,newRegion:String,
                                   newNumber:String,newText:String,oldRegion:String,
                                   oldNumber:String,oldText:String):Boolean{

        if(!isOld && (newRegion.isEmpty() || newNumber.isEmpty()
                    || newText.isEmpty() || (newRegion.length != 2) &&
                    (newText.length != 3) || (newNumber.length != 3))) {
            context.toast("Введите правильный номер машины")
            return false
        }

        if(isOld && (oldRegion.isEmpty() || oldNumber.isEmpty()
                    || oldText.isEmpty() || (oldRegion.length != 1) &&
                    (newText.length != 3) || (newNumber.length != 3))) {
            context.toast("Введите правильный номер машины")
            return false
        }


        return  true
    }
}
