package com.example.mykart.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mykart.R
import com.example.mykart.activities.HomeActivity
import com.example.mykart.adapter.CartAdapter
import com.example.mykart.changeFragments
import com.example.mykart.classes.MyViewModel
import com.example.mykart.database.Cart
import com.example.mykart.database.MyDataBase
import com.example.mykart.database.ProductList
import com.example.mykart.databinding.FragmentCartBinding
import com.example.mykart.hideVisibility
import com.example.mykart.showVisibility
import com.example.mykart.utils.AppConstants.Companion.Name
import com.example.mykart.utils.MyPreferences


open class CartFragment : Fragment() {

    //lateinit variable
    private lateinit var binding: FragmentCartBinding
    private lateinit var viewModel: MyViewModel
    private lateinit var database: MyDataBase
    private var allCart= ArrayList<Cart>()
    private var carLlist = ArrayList<ProductList>()
    private val adapter = CartAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //getting database instance
        database = MyDataBase.getInstance(requireContext())
        allCart.addAll(database.Dao().getCart(MyPreferences.getValueString(Name, "").toString()))
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]


        binding.apply {
            viewModel.setTotal(setItemTotal())

            //adding products saved in card by product id
            if (carLlist.isEmpty()) {
                if (allCart.isEmpty()) {
                    showVisibility(binding.tvEmptyMsg)
                    hideVisibility(binding.cardChkout)
                } else {
                    for (i in allCart.indices) {
                        carLlist.add(database.Dao().getProduct(allCart[i].productId))
                    }
                }
            }

            val layoutManager = LinearLayoutManager(requireContext())
            cartList.layoutManager = layoutManager
            adapter.setCartList(carLlist, allCart, requireContext())
            cartList.adapter = adapter

            //checkout button onclick listener
            btnCheckOut.setOnClickListener {
                changeFragments(R.id.frame, PaymentFragment(), parentFragmentManager, "Payment")
                (activity as HomeActivity).setCartBtn(1, 0)
                (activity as HomeActivity).setToolbarText("Payment")
            }
            observer()


        }
        //detete clicked operation
        adapter.onItemDeleteClick(object : CartAdapter.OnCartItemClick {
            override fun onClick(productList: ProductList, cart: Cart) {
                carLlist.remove(productList)
                allCart.remove(cart)
                adapter.setCartList(carLlist, allCart, requireContext())
                database.Dao().deleteProductFromCart(productList.id)
                (activity as HomeActivity).getCartCount()
                viewModel.setTotal(setItemTotal())
            }

        })

        //cart quantity plus button
        adapter.onItemPlusClick(object : CartAdapter.OnCartPlusClick {
            override fun onClick(productList: ProductList, text: String, position: Int) {

                database.Dao().addProductToCart(
                    Cart(
                        MyPreferences.getValueString(Name, "").toString(),
                        productList.id,
                        Integer.parseInt(text)
                ))
                allCart[position].qty = Integer.parseInt(text)
                viewModel.setTotal(setItemTotal())
            }

        })

        //cart quantity minus button iinterface called
        adapter.onItemMinusClick(object : CartAdapter.OnCartMinusClick {
            override fun onClick(productList: ProductList, text: String, position: Int) {
                database.Dao().addProductToCart(
                    Cart(
                        MyPreferences.getValueString(Name, "").toString(),
                        productList.id,
                        Integer.parseInt(text)
                    )
                )
                allCart[position].qty = Integer.parseInt(text)
                viewModel.setTotal(setItemTotal())
            }

        })

    }

    //delete all cart function
    open fun deleteCart() {
        database.Dao().deleteUserCart(MyPreferences.getValueString(Name, "").toString())
        carLlist.removeAll(carLlist)
        adapter.setCartList(carLlist, allCart, requireContext())
        showVisibility(binding.tvEmptyMsg)
        hideVisibility(binding.cardChkout)

    }

    fun setItemTotal(): Float {
        var itemTotal: Float = 0.00F
        val cartlist = ArrayList<ProductList>()
        val allcart = database.Dao().getCart(MyPreferences.getValueString(Name, "").toString())
        for (i in allcart.indices) {
            cartlist.add(database.Dao().getProduct(allcart[i].productId))
            itemTotal += (cartlist[i].price * allcart[i].qty)
        }
        return itemTotal
    }

    fun observer() {
        viewModel.total.observe(viewLifecycleOwner) {
            binding.tvTotalAmt.text = "$" + "%.2f".format(it)
        }
    }


}