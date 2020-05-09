package com.example.parkingsystem.ui.fragments

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListPopupWindow
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.parkingsystem.R
import com.example.parkingsystem.databinding.FragmentMapsBinding
import com.example.parkingsystem.network.model.CreateOrder
import com.example.parkingsystem.network.model.Parkings
import com.example.parkingsystem.network.repositories.ParkingsRepo
import com.example.parkingsystem.ui.activity.CurrentStatusActivity
import com.example.parkingsystem.ui.fragments.listeners.MapListener
import com.example.parkingsystem.ui.fragments.viewmodels.MapViewModel
import com.example.parkingsystem.util.Order
import com.example.parkingsystem.util.User
import com.example.parkingsystem.util.indeterminateProgressDialog
import com.example.parkingsystem.util.toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.parking_popup_view.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MapFragment : Fragment(),MapListener{
    private var parkingId = 0
    var googleMap:GoogleMap? = null
    var mapView: MapView? = null
    private var progressBar: ProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMapsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false)
        val viewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        binding.viewmodelmap = viewModel
        viewModel.listener = this
        mapView = binding.mapView
        mapView!!.onCreate(savedInstanceState)
        mapView!!.onResume()
        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mapView!!.getMapAsync(OnMapReadyCallback { mMap ->
            googleMap = mMap
            // For showing a move to my location button
            //googleMap!!.setMyLocationEnabled(true)
            // For dropping a marker at a point on the Map
            viewModel.loadAllParkings()
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onFailure() {
        if(progressBar != null && progressBar!!.isShowing){
            progressBar!!.dismiss()
        }
        context!!.toast("Ошибка загрузки парковок!")
    }

    override fun onProgress() {
        progressBar = context!!.indeterminateProgressDialog(R.string.processing_3xdot)
        progressBar!!.show()
    }

    override fun onSuccess(list:List<Parkings?>,markers:HashMap<String, Parkings>) {
        if(progressBar != null && progressBar!!.isShowing){
            progressBar!!.dismiss()
        }
        list.forEach {
            val cooridnates = LatLng(it!!.latitude.toDouble(), it.longtitude.toDouble())
            val markerOption = MarkerOptions().position(cooridnates)
            val marker = googleMap!!.addMarker(markerOption)
            markers.put(marker.id,it)
        }

        val cameraPosition =
            CameraPosition.Builder().target(LatLng(43.238949, 76.889709)).zoom(12f).build()
        googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        googleMap!!.setOnMarkerClickListener {
            val inflater: LayoutInflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = inflater.inflate(R.layout.parking_popup_view,null)

            val popupWindow = PopupWindow()
            popupWindow.contentView = view
            popupWindow.width = ListPopupWindow.MATCH_PARENT
            popupWindow.height = ListPopupWindow.MATCH_PARENT

            view.parkingName.text = markers.get(it.id)!!.name
            view.parkingDescription.text = markers.get(it.id)!!.description
            view.parkingOccupiedPlaces.text = "занято: "+markers.get(it.id)!!.occupied_places+"мест"
            view.parkingPlaces.text = "общий: "+markers.get(it.id)!!.places+"мест"
            view.parkingPrice.text = "цена: "+markers.get(it.id)!!.price+"тг/час"

            if(markers.get(it.id)!!.occupied_places == markers.get(it.id)!!.places){
                view.status.text = "мест нет"
                view.status.setTextColor(context!!.resources.getColor(R.color.red))
            }else{
                view.status.text = "есть места"
                view.status.setTextColor(context!!.resources.getColor(R.color.green))
            }
            parkingId = markers.get(it.id)!!.id
            view.close.setOnClickListener{
                popupWindow.dismiss()
            }
            Glide.with(context!!).load(markers.get(it.id)!!.image).into(view.image)

            view.query.setOnClickListener{
                if(view.status.text.toString() != "мест нет"){
                    val dialogBuilder = AlertDialog.Builder(context)

                    dialogBuilder.setMessage("Уверены подать заявку на эту парковку? Если после заявки вы не " +
                            "яветесь на парковку в течении 24 часов, заявка онулируется!")
                        .setPositiveButton("Да") { dialog, _ ->
                            val progressBar = context!!.indeterminateProgressDialog(R.string.processing_3xdot)
                            progressBar.show()
                            CoroutineScope(Dispatchers.IO).launch {
                                val data = CreateOrder(User.id.toString(),parkingId.toString(),User.token,"1")
                                val response = ParkingsRepo().createOrder(data)
                                val body = response.body()
                                Log.e("ASD",response.toString())
                                Log.e("ASD",response.errorBody().toString())
                                if(response.isSuccessful){
                                    Log.e("ASD",body!!.toString())
                                    if(body!!.success) {
                                        withContext(Dispatchers.Main) {
                                            progressBar.dismiss()
                                            Intent(context, CurrentStatusActivity::class.java).also {
                                                Order.id=body.data!!.id
                                                it.putExtra("fromMap","true")
                                                context!!.startActivity(it)
                                            }
                                        }
                                    }else {
                                        withContext(Dispatchers.Main){
                                            progressBar.dismiss()
                                            context!!.toast("Ошибка!Повторите позже!")
                                        }
                                    }
                                }else{
                                    withContext(Dispatchers.Main){
                                        progressBar.dismiss()
                                        context!!.toast("Ошибка!Повторите позже!")
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
                }else{
                    context!!.toast("Мест нет")
                }

            }

            popupWindow.showAtLocation(
                mapView,
                Gravity.CENTER,
                0,
                0
            )

            false
        }
    }


}
