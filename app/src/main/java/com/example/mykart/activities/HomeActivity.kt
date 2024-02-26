package com.example.mykart.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mykart.R
import com.example.mykart.changeActivity
import com.example.mykart.changeFragments
import com.example.mykart.classes.MyViewModel
import com.example.mykart.database.MyDataBase
import com.example.mykart.database.ProductList
import com.example.mykart.databinding.ActivityHomeBinding
import com.example.mykart.fragments.CartFragment
import com.example.mykart.fragments.HomeFragment
import com.example.mykart.fragments.OrderFragment
import com.example.mykart.fragments.ProfileFragment
import com.example.mykart.hideKeyboard
import com.example.mykart.utils.AppConstants.Companion.Flag
import com.example.mykart.utils.AppConstants.Companion.Name
import com.example.mykart.utils.MyPreferences

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: MyViewModel
    private lateinit var headerName: TextView
    private lateinit var database: MyDataBase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        database = MyDataBase.getInstance(this)
        getCartCount()


        //databinding apply method
        binding.apply {
            toolbar.tooltitle.text = "Home"
            val homeFragment = HomeFragment()
            changeFragments(R.id.frame, homeFragment, supportFragmentManager, "Home")

            //disable drawer opening through gestures
            drawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)


            //header
            val header: View = navView.getHeaderView(0)
            headerName = header.findViewById(R.id.myprofile)
            val headerImg: ImageView = header.findViewById(R.id.img)
            headerName.text = MyPreferences.getValueString(Name, "")

            //setting header image
            headerImg.setOnClickListener {
                changeFragments(R.id.frame, ProfileFragment(), supportFragmentManager, "Profile")
                setMenuBtn(R.drawable.back)
                toolbar.tooltitle.text = "Profile"
                drawerlayout.closeDrawers()

            }

            //toolbar menu click listener
            toolbar.menu.setOnClickListener {

                if (toolbar.tooltitle.text == "Home") {
                    drawerlayout.openDrawer(GravityCompat.START)
                } else {
                    backToHome()
                }
            }


            //cart on click listener
            toolbar.cart.setOnClickListener {

                if (toolbar.tooltitle.text == "Home" || toolbar.tooltitle.text == "Product Detail") {
                    changeFragments(R.id.frame, CartFragment(), supportFragmentManager, "Cart")
                    setMenuBtn(R.drawable.back)
                    setCartBtn(R.drawable.baseline_delete_sweep_24, 0)
                    binding.toolbar.tooltitle.text = "Cart"

                } else if (toolbar.tooltitle.text == "Cart") {
                    val fragment = supportFragmentManager.findFragmentByTag("Cart") as CartFragment
                    fragment.deleteCart()
                    getCartCount()
                }
            }

            //navigation view set onclick listener
            navView.setNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.home -> {
                        navClicked(homeFragment, "Home")
                        setMenuBtn(R.drawable.baseline_menu_24)
                        setCartBtn(R.drawable.baseline_shopping_cart_24, 1)

                    }
                    R.id.cart -> {
                        navClicked(CartFragment(), "Cart")
                        setMenuBtn(R.drawable.back)
                        setCartBtn(R.drawable.baseline_delete_sweep_24, 0)

                    }
                    R.id.orders->{
                        navClicked(OrderFragment(), "Your orders")
                        setMenuBtn(R.drawable.back)
                        setCartBtn(1, 0)
                    }
                    R.id.logout -> {
                        changeActivity(this@HomeActivity, LoginActivity())
                        MyPreferences.setValueInt(Flag, 0)
                        finish()
                    }
                }
                true
            }



            observer()

        }

    }

    //observe method
    fun observer() {
        viewModel.allcart.observe(this) {
            binding.toolbar.favCount = it.size.toString()
        }
    }


    //function to change fragment after click of navigation menu
    fun navClicked(homeFragment: Fragment, text: String) {
        changeFragments(R.id.frame, homeFragment, supportFragmentManager, text)
        binding.toolbar.tooltitle.text = text
        binding.drawerlayout.closeDrawers()
    }

    //setting toolbar text
    fun setToolbarText(text: String) {
        binding.toolbar.tooltitle.text = text
    }


    //funvtion to get cart count
    fun getCartCount() {
        val list = ArrayList<ProductList>()
        if (database.Dao().getCart(MyPreferences.getValueString(Name, "").toString())
                .isNotEmpty()
        ) {
            for (i in database.Dao()
                .getCart(MyPreferences.getValueString(Name, "").toString()).indices) {
                list.add(database.Dao().getProduct(database.Dao().getAllCart()[i].productId))
            }
        }
        viewModel.allcart.postValue(list)
    }

    //handling onbackpress method
    override fun onBackPressed() {
        backToHome()
    }

    fun backToHome() {
        if (binding.toolbar.tooltitle.text == "Payment"){
            supportFragmentManager.popBackStack()
            binding.toolbar.tooltitle.text = "Cart"
            setMenuBtn(R.drawable.back)
            setCartBtn(R.drawable.baseline_delete_sweep_24, 0)

        }else{
            if (supportFragmentManager.backStackEntryCount > 1) {
                for (i in 1 until supportFragmentManager.backStackEntryCount) {
                    supportFragmentManager.popBackStack()
                    setMenuBtn(R.drawable.baseline_menu_24)
                    setCartBtn(R.drawable.baseline_shopping_cart_24, 1)
                }
            } else {
                finish()
            }
        }
    }

    //setting image of menu
    fun setMenuBtn(id: Int) {
        binding.toolbar.menu.setImageResource(id)
    }

    //function to hide or show cart count and button
    fun setCartBtn(id: Int, visibility: Int) {
        if (id != 1) {
            binding.toolbar.cart.setImageResource(id)
            binding.toolbar.cart.visibility = View.VISIBLE
        } else {
            binding.toolbar.cart.visibility = View.INVISIBLE
        }
        if (visibility == 1) {
            binding.toolbar.cartcount.visibility = View.VISIBLE
        } else
            binding.toolbar.cartcount.visibility = View.INVISIBLE
    }


}