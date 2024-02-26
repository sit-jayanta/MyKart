package com.example.mykart.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mykart.R
import com.example.mykart.activities.HomeActivity
import com.example.mykart.adapter.ViewPagerAdapter
import com.example.mykart.classes.MyViewModel
import com.example.mykart.database.Cart
import com.example.mykart.database.MyDataBase
import com.example.mykart.database.ProductList
import com.example.mykart.databinding.FragmentViewProductBinding
import com.example.mykart.utils.AppConstants.Companion.Name
import com.example.mykart.utils.MyPreferences

class ViewProductFragment : Fragment(), View.OnClickListener {

    //lateinit variable
    private lateinit var binding: FragmentViewProductBinding
    private lateinit var viewModel: MyViewModel
    private lateinit var database: MyDataBase
    private var imageList = ArrayList<ProductList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_view_product, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //getting database and viewmodel instance
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        database = MyDataBase.getInstance(requireContext())
        binding.apply {
            val bundle = arguments
            if (bundle != null) {
                imageList = bundle.getSerializable("clickedList") as ArrayList<ProductList>
                products = imageList[0]
                imgViewPager.adapter = ViewPagerAdapter(imageList[0].image, requireContext())

            }
            btnAddToCart.setOnClickListener(this@ViewProductFragment)

        }
    }

    override fun onClick(v: View?) {
        if (v == binding.btnAddToCart) {
            if (!(database.Dao().getAllCartId().contains(
                    imageList[0].id
                ))
            ) {
                database.Dao().addProductToCart(
                    Cart(
                        MyPreferences.getValueString(Name, "").toString(), imageList[0].id,1
                    )
                )
                (activity as HomeActivity).getCartCount()
                Toast.makeText(requireContext(), "Product added to cart", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    requireContext(), "Product already added. Go to cart :)", Toast.LENGTH_SHORT
                ).show()
            }


        }
    }


}



