package com.example.mykart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.mykart.R
import java.util.*

class ViewPagerAdapter(private val image: String, private val context: Context): PagerAdapter() {
    override fun getCount(): Int {
        return 1
    }



    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val itemView = LayoutInflater.from(context).inflate(R.layout.images_layout,container,false)

        val imageView = itemView.findViewById<ImageView>(R.id.imageViewPager)
        Glide.with(context).load(image).into(imageView)

        Objects.requireNonNull(container).addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as CardView)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {

        return view == `object` as CardView
    }



}