package com.example.parkingsystem.ui.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.parkingsystem.R
import com.example.parkingsystem.databinding.ActivityCurrentStatusBinding
import com.example.parkingsystem.network.model.CurrentParking
import com.example.parkingsystem.ui.activity.listeners.CurrentStatusListener
import com.example.parkingsystem.ui.activity.viewmodels.CurrentViewModel
import com.example.parkingsystem.util.User
import com.example.parkingsystem.util.indeterminateProgressDialog
import com.example.parkingsystem.util.toast

class CurrentStatusActivity : AppCompatActivity(), CurrentStatusListener {

    var binding:ActivityCurrentStatusBinding? = null
    private var progressBar: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_current_status)
        val viewModel = ViewModelProviders.of(this).get(CurrentViewModel::class.java)
        binding!!.viewmodel = viewModel
        viewModel.listener = this
        viewModel.initActivity(this)
    }

    override fun onFailure() {
        binding!!.cancelButton.visibility = View.GONE
        binding!!.emptyConstraint.visibility = View.VISIBLE
        binding!!.statusText.setTextColor(resources.getColor(R.color.red))
        binding!!.statusText.text = "у вас нет активных заявок"
    }

    override fun onProcess() {
        progressBar = indeterminateProgressDialog(R.string.processing_3xdot)
        progressBar!!.show()
    }

    override fun onSuccess(data:CurrentParking) {
        if(progressBar != null && progressBar!!.isShowing){
            progressBar!!.dismiss()
        }
        binding!!.emptyConstraint.visibility = View.GONE
        if(data.incomingTime == "0"){
            binding!!.cancelButton.visibility = View.VISIBLE
            binding!!.statusText.setTextColor(resources.getColor(R.color.red))
            binding!!.statusText.text = "вы еще не приехали на парковку"
        }else{
            binding!!.cancelButton.visibility = View.GONE
            binding!!.statusText.setTextColor(resources.getColor(R.color.green))
            binding!!.statusText.text = "вы еще не приехали на парковку"
        }
        binding!!.parkingName.text = data.name
        binding!!.parkingDescription.text = data.description
    }

    override fun showDialog() {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage("У вас на счету ${User.balance}тг. Если вы превысите время которое позволяет вам баланс" +
                ", счет будет уходить в минус ")
            .setPositiveButton("Ок") { dialog, _ ->
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        //alert.setIcon(R.drawable.exit_icon)
        alert.setTitle("Сообщение!")
        alert.show()
    }

    override fun onCancelSuccess() {
        binding!!.cancelButton.visibility = View.GONE
        binding!!.emptyConstraint.visibility = View.VISIBLE
        binding!!.statusText.setTextColor(resources.getColor(R.color.red))
        binding!!.statusText.text = "у вас нет активных заявок"
    }

    override fun onCancelFailure() {
        toast("Ошибка отмены заявки!!")
    }
}
