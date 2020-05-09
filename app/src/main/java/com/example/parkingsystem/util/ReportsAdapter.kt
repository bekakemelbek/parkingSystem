package com.example.parkingsystem.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.parkingsystem.R
import com.example.parkingsystem.network.model.Order
import kotlinx.android.synthetic.main.report_item.view.*

class ReportsAdapter(val items : List<Order?>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.report_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = items[position]!!.parkId
        holder.comeTime.text = "приехали: "+items[position]!!.incomingTime
        holder.endTime.text = "отьехали: "+items[position]!!.outgoingTime
        if(items[position]!!.outgoingTime == ""){
            holder.status.setImageDrawable(context.resources.getDrawable(R.drawable.wait_status))
        }else{
            holder.status.setImageDrawable(context.resources.getDrawable(R.drawable.done_status))
        }
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val name = view.name
    val comeTime = view.comeTime
    val endTime = view.endTime
    val status = view.status
}