package com.example.mykart.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mykart.R
import com.example.mykart.adapter.OrderAdapter
import com.example.mykart.classes.MyViewModel
import com.example.mykart.database.MyDataBase
import com.example.mykart.database.Orders
import com.example.mykart.databinding.FragmentOrderBinding
import com.example.mykart.utils.AppConstants
import com.example.mykart.utils.MyPreferences

class OrderFragment : Fragment() {
    //lateinit variables
    private lateinit var binding: FragmentOrderBinding
    private lateinit var database: MyDataBase
    private lateinit var viewmodel: MyViewModel
    private var adapter = OrderAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //gettinf instance of databse and viewmodel
        database = MyDataBase.getInstance(requireContext())
        viewmodel = ViewModelProvider(this@OrderFragment)[MyViewModel::class.java]

        if (database.Dao().allOrders(MyPreferences.getValueString(AppConstants.Name, "").toString())
                .isNotEmpty()
        ) {
            viewmodel.setOrderList(
                database.Dao().allOrders(
                    MyPreferences.getValueString(AppConstants.Name, "").toString()
                ) as ArrayList<Orders>
            )
        } else {
            binding.empty.visibility = View.VISIBLE
        }
        //setting adapter
        binding.order.adapter = adapter
        observer()

    }

    private fun observer() {
        viewmodel.orderlist.observe(viewLifecycleOwner) {
            adapter.setOrderList(it)
        }
    }


}