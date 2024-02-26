package com.example.mykart.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mykart.R
import com.example.mykart.activities.HomeActivity
import com.example.mykart.adapter.MainAdapter
import com.example.mykart.changeFragments
import com.example.mykart.classes.MyViewModel
import com.example.mykart.database.MyDataBase
import com.example.mykart.database.ProductList
import com.example.mykart.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    //lateinit variables
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: MyViewModel
    private lateinit var database: MyDataBase
    private lateinit var adapter: MainAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initializing viewmodel and database
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        database = MyDataBase.getInstance(requireContext())

        (activity as HomeActivity).setToolbarText("Home")

        //setting up adapter
        adapter = MainAdapter()
        Log.d("count", parentFragmentManager.backStackEntryCount.toString())

        //apply method
        binding.apply {
            if (viewModel.productlist.value.isNullOrEmpty()) {
                viewModel.getAllProducts(loading)
            }
            recyclerview.adapter = adapter


            //onitem click iterface called
            adapter.onItemClick(object : MainAdapter.onItemClickListener {
                override fun onClick(model: ProductList) {
                    val list = ArrayList<ProductList>()
                    list.add(model)
                    Log.d("product", model.category.toString())
                    val fragment = ViewProductFragment()
                    val bundle = Bundle()
                    bundle.putSerializable("clickedList", list)
                    fragment.arguments = bundle
                    changeFragments(R.id.frame, fragment, parentFragmentManager, "Product")
                    (activity as HomeActivity).setToolbarText("Product Detail")
                    (activity as HomeActivity).setMenuBtn(R.drawable.back)

                }
            })

            //calling observer
            observer()
        }

    }

    private fun observer() {
        viewModel.productlist.observe(viewLifecycleOwner) {
            adapter.setProductList(it)
            database.Dao().addAllProduct(it)
        }
    }


}