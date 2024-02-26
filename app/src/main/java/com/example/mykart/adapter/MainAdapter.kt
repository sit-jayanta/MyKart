package com.example.mykart.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mykart.database.ProductList
import com.example.mykart.databinding.ProductLayoutBinding

@SuppressLint("NotifyDataSetChanged")
open class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    class ViewHolder(var binding: ProductLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private var productList = ArrayList<ProductList>()

    private var listener: onItemClickListener? = null


    fun setProductList(
        productsList: List<ProductList>?
    ) {
        productList = productsList as ArrayList<ProductList>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ProductLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.apply {
            binding.product = productList[position]

            itemView.setOnClickListener {
                listener?.onClick(productList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    interface onItemClickListener {
        fun onClick(model: ProductList)
    }

    fun onItemClick(listener: onItemClickListener) {
        this.listener = listener
    }


}