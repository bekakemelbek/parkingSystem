package com.example.parkingsystem.ui.activity.viewmodels

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.parkingsystem.R
import com.example.parkingsystem.network.model.CancelOrder
import com.example.parkingsystem.network.repositories.ParkingsRepo
import com.example.parkingsystem.ui.activity.listeners.CurrentStatusListener
import com.example.parkingsystem.util.Order
import com.example.parkingsystem.util.User
import com.example.parkingsystem.util.indeterminateProgressDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrentViewModel: ViewModel() {

    var listener:CurrentStatusListener? = null

    fun initActivity(context: Context) {
        try {
            if ((context as Activity).intent.getStringExtra("fromMap") == "true"){
                listener!!.showDialog()
            }
        }catch (e:Exception){ }

        loadData()
    }

    fun refreshClicked(view: View){
        listener!!.onProcess()
        loadData()
    }

    fun loadData(){
        CoroutineScope(IO).launch {
            val responseOrder = ParkingsRepo().getCurrentParking(User.id,User.token)
            val bodyOrder = responseOrder.body()
            if(responseOrder.isSuccessful){
                if (bodyOrder!!.success) {
                    withContext(Main) {
                        listener!!.onSuccess(bodyOrder.data!!)

                    }
                } else {
                    withContext(Main) {
                        listener!!.onFailure()
                    }
                }
            }else{
                withContext(Main) {
                    listener!!.onFailure()
                }
            }


        }
    }

    fun cancelClicked(view:View){
        val dialogBuilder = AlertDialog.Builder(view.context)

        dialogBuilder.setMessage("Уверены что хотите отменить заявку?")
            .setPositiveButton("Да") { dialog, _ ->
                val progressBar = view.context.indeterminateProgressDialog(R.string.processing_3xdot)
                progressBar.show()
                CoroutineScope(IO).launch {
                    val data = CancelOrder(User.id.toString(), Order.id.toString(),User.token)
                    val responseOrder = ParkingsRepo().cancelOrder(data)
                    val bodyOrder = responseOrder.body()
                    if (bodyOrder!!.success) {
                        withContext(Main) {
                            listener!!.onCancelSuccess()
                            progressBar.dismiss()
                        }
                    } else {
                        withContext(Main) {
                            listener!!.onCancelFailure()
                            progressBar.dismiss()
                        }
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Нет") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = dialogBuilder.create()
        //alert.setIcon(R.drawable.exit_icon)
        alert.setTitle("Сообщение!")
        alert.show()

    }

    fun backClicked(view:View){
        val context = view.context
        (context as Activity).finish()
    }
}