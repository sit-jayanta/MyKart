package com.example.mykart.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

open class BindingAdapter {
    companion object{
        @JvmStatic
        @BindingAdapter("urlToImages")
        fun ImageView.urlToImage(url : String){
            Glide.with(this.context).load(url).into(this)
        }
    }
}