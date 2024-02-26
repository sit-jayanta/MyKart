package com.example.mykart.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mykart.database.Cart
import com.example.mykart.database.ProductList
import com.example.mykart.databinding.CartListLayoutBinding


open class CartAdapter : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(var binding: CartListLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private var cartList = ArrayList<ProductList>()
    private var cartQty = ArrayList<Cart>()
    private var listener: OnCartItemClick? = null
    private var plistener: OnCartPlusClick? = null
    private var mlistener: OnCartMinusClick? = null
    private var context: Context? = null


    fun setCartList(
        cartList: ArrayList<ProductList>,
        allCart: List<Cart>, context: Context
    ) {
        this.cartList = cartList
        this.cartQty = allCart as ArrayList<Cart>
        this.context = context
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CartListLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            binding.tvQuantity.text = cartQty[position].qty.toString()
            binding.cart = cartList[position]
            binding.ivDelete.setOnClickListener {
                listener?.onClick(cartList[position],cartQty[position])
            }

            binding.ivPlus.setOnClickListener {
                val qty = Integer.parseInt(binding.tvQuantity.text.toString())
                if (qty in 1..9) {
                    binding.tvQuantity.text =
                        ((Integer.parseInt(binding.tvQuantity.text.toString()) + 1).toString())
                    plistener?.onClick(cartList[position], binding.tvQuantity.text.toString(),position)
                } else if (qty == 10) {
                    Toast.makeText(context, "Only 10 units per user is allowed", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            binding.ivMinus.setOnClickListener {
                val qty = Integer.parseInt(binding.tvQuantity.text.toString())
                if (qty in 2..10) {
                    binding.tvQuantity.text =
                        ((Integer.parseInt(binding.tvQuantity.text.toString()) - 1).toString())
                    mlistener?.onClick(cartList[position], binding.tvQuantity.text.toString(),position)
                } else if (qty == 1) {
                    Toast.makeText(
                        context,
                        "Quantity cannot be 0. Delete if you want to remove item from cart",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    interface OnCartItemClick {
        fun onClick(productList: ProductList, cart: Cart)
    }

    interface OnCartPlusClick {
        fun onClick(productList: ProductList, text: String, position: Int)
    }

    interface OnCartMinusClick {
        fun onClick(productList: ProductList, text: String, position: Int)
    }

    fun onItemDeleteClick(listener: OnCartItemClick) {
        this.listener = listener
    }

    fun onItemPlusClick(listener: OnCartPlusClick) {
        this.plistener = listener
    }

    fun onItemMinusClick(listener: OnCartMinusClick) {
        this.mlistener = listener
    }
}