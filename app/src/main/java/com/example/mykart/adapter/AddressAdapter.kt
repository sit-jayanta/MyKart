package com.example.mykart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mykart.database.MultiAddress
import com.example.mykart.databinding.AddressLayoutBinding

open class AddressAdapter : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    class ViewHolder(var binding: AddressLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private var address = ArrayList<MultiAddress>()
    private var listener: onItemClickListener? = null


    fun setAddress(
        address: List<MultiAddress>
    ) {
        this.address = address as ArrayList<MultiAddress>
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AddressLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.apply {
            binding.chkAdsress.isChecked = address[position].isSelected

            binding.address = address[position]
            binding.chkAdsress.setOnClickListener {
                listener?.onClick(address,position)
            }
        }

    }

    override fun getItemCount(): Int {
        return address.size
    }

    interface onItemClickListener {
        fun onClick(multiAddress: ArrayList<MultiAddress>, position: Int)
    }

    fun onItemClick(listener: onItemClickListener) {
        this.listener = listener
    }


}
