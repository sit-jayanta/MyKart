package com.example.mykart.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mykart.database.Orders
import com.example.mykart.databinding.MyOrdersBinding

@SuppressLint("NotifyDataSetChanged")
open class OrderAdapter : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    class ViewHolder(var binding: MyOrdersBinding) : RecyclerView.ViewHolder(binding.root)

    private var order = ArrayList<Orders>()



    fun setOrderList(
        order: ArrayList<Orders>,

        ) {
        this.order = order


        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MyOrdersBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            binding.order = order[position]
        }
    }


    override fun getItemCount(): Int {
        return order.size
    }

}