package com.example.mykart.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mykart.R
import com.example.mykart.activities.HomeActivity
import com.example.mykart.adapter.AddressAdapter
import com.example.mykart.classes.MyViewModel
import com.example.mykart.database.*
import com.example.mykart.databinding.AddressDialogBinding
import com.example.mykart.databinding.FragmentPaymentBinding
import com.example.mykart.utils.AppConstants
import com.example.mykart.utils.MyPreferences
import java.util.Calendar


class PaymentFragment : Fragment(), View.OnClickListener {

    //lateinit variables
    private lateinit var binding: FragmentPaymentBinding
    private lateinit var dataBase: MyDataBase
    private lateinit var allCart: List<Cart>
    private var cartList = ArrayList<ProductList>()
    private var itemTotal: Float = 0.00F
    private var delivery: Float = 20.00F
    private var taxes: Float = 0.00F
    private var orderTotal: Float = 0.00F
    private var addressList = ArrayList<MultiAddress>()
    private var adapter = AddressAdapter()
    private lateinit var viewModel: MyViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //getting databse and viewmodel instance
        dataBase = MyDataBase.getInstance(requireContext())
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        allCart =
            dataBase.Dao().getCart(MyPreferences.getValueString(AppConstants.Name, "").toString())

        //binding method
        binding.apply {

            for (i in allCart.indices) {
                cartList.add(dataBase.Dao().getProduct(allCart[i].productId))
                itemTotal += (cartList[i].price * allCart[i].qty)
            }
            taxes = (itemTotal * 0.1).toFloat()
            orderTotal = itemTotal + taxes + delivery

            //setting all values to textview
            tvItemAmount.text = "$$itemTotal"
            tvDeliverytotal.text = "$$delivery"
            tvTaxesamt.text = "$$taxes"
            tvOrdertotal.text = "$$orderTotal"

            //getting specific users saved address
            if (dataBase.Dao()
                    .getAllAddress(MyPreferences.getValueString(AppConstants.Name, "").toString())
                    .isEmpty()
            ) {
                viewModel.setNewAddress(showAddress())
            } else {
                addressList.addAll(
                    dataBase.Dao().getAllAddress(
                        MyPreferences.getValueString(AppConstants.Name, "").toString()
                    )
                )
                addressList[0].isSelected = true
                adapter.setAddress(addressList)
            }

            //setting adapter
            val layoutManager = LinearLayoutManager(requireContext())
            rlListAddress.layoutManager = layoutManager
            rlListAddress.adapter = adapter
            addnewAddress.setOnClickListener(this@PaymentFragment)
            btnPayment.setOnClickListener(this@PaymentFragment)

            observer()

            //on address click interface called
            adapter.onItemClick(object : AddressAdapter.onItemClickListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onClick(multiAddress: ArrayList<MultiAddress>, position: Int) {
                    for (i in multiAddress.indices) {
                        multiAddress[i].isSelected = false
                    }
                    multiAddress[position].isSelected = true
                    dataBase.Dao().addAddressAll(multiAddress)
                    adapter.notifyDataSetChanged()
                }

            })
        }
    }

    //all methods used
    private fun showAddress(): MultiAddress {
        val user = dataBase.Dao()
            .getUserDetails(MyPreferences.getValueString(AppConstants.Name, "").toString())
        val address = MultiAddress(
            MyPreferences.getValueString(AppConstants.Name, "").toString(),
            MyPreferences.getValueString(AppConstants.Name, "").toString(),
            user.address.number + ", " + user.address.street + "\n" + user.address.city + "\n" + user.address.zipcode
        )

        dataBase.Dao().addAddress(address)
        return address
    }

    override fun onClick(v: View?) {
        if (v == binding.addnewAddress) {
            btnShowMessage()
        }
        if (v == binding.btnPayment) {
            Toast.makeText(
                requireContext(),
                "Purchase Successful.Thank you for using MyKart.",
                Toast.LENGTH_SHORT
            ).show()
            dataBase.Dao().deleteUserCart(MyPreferences.getValueString(AppConstants.Name,"").toString())
            (activity as HomeActivity).getCartCount()
            val random = (10000000..99999999).random()
            var item = ""
            for (i in cartList.indices) {
                item += cartList[i].title + " x${allCart[i].qty}\n"
            }
            dataBase.Dao().addOrder(
                Orders(
                    MyPreferences.getValueString(AppConstants.Name, "").toString(),
                    random,
                    orderTotal,
                    item,
                    getDate()
                )
            )
            if (parentFragmentManager.backStackEntryCount > 1) {
                for (i in 1 until parentFragmentManager.backStackEntryCount) {
                    parentFragmentManager.popBackStack()
                    (activity as HomeActivity).setMenuBtn(R.drawable.baseline_menu_24)
                    (activity as HomeActivity).setCartBtn(R.drawable.baseline_shopping_cart_24, 1)
                }
            }

        }
    }

    fun btnShowMessage() {
        val alert = AlertDialog.Builder(requireContext())
        val binding: AddressDialogBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.address_dialog, null, false)

        alert.setView(binding.root)
        val alertDialog = alert.create()
        alertDialog.setCanceledOnTouchOutside(true)

        binding.addBtn.setOnClickListener {
            if (binding.edtnumber.text.isNotEmpty() && binding.edtStreet.text.isNotEmpty() && binding.edtCity.text.isNotEmpty() && binding.edtZipcode.text.isNotEmpty()) {
                val address = MultiAddress(
                    MyPreferences.getValueString(AppConstants.Name, "").toString(),
                    binding.edtName.text.toString(),
                    binding.edtnumber.text.toString() + ", " + binding.edtStreet.text.toString() + "\n" + binding.edtCity.text.toString() + "\n" + binding.edtZipcode.text.toString()
                )

                dataBase.Dao().addAddress(address)
                viewModel.setNewAddress(address)

            }
            alertDialog.dismiss()
        }
        alertDialog.show()

    }

    private fun observer() {
        viewModel.newAddress.observe(viewLifecycleOwner) {
            if (it.name.isNotEmpty()) {
                addressList.add(it)
                addressList[0].isSelected = true
                adapter.setAddress(addressList)
            }
        }
    }

    private fun getDate(): String {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) +1
        val year = calendar.get(Calendar.YEAR)
        val date = calendar.get(Calendar.DATE)

        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        var am_pm = ""
        if (hour>=12){
            if (hour==12){
                am_pm = "PM"
            }else{
            hour -= 12
            am_pm = "PM"}
        }else am_pm = "AM"

        return "$date/$month/$year, $hour:$minute $am_pm"
    }


}