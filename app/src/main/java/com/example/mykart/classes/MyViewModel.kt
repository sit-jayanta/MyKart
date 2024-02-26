package com.example.mykart.classes

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mykart.activities.HomeActivity
import com.example.mykart.changeActivity
import com.example.mykart.database.*
import com.example.mykart.utils.AppConstants.Companion.Flag
import com.example.mykart.utils.AppConstants.Companion.Name
import com.example.mykart.utils.AppConstants.Companion.fakeStoreAPI
import com.example.mykart.utils.MyPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyViewModel : ViewModel() {

    var toastmsg = MutableLiveData("")
    var userList = MutableLiveData<List<UserDetails>>()
    var loggedInUser = MutableLiveData<UserDetails>()
    var productlist = MutableLiveData<List<ProductList>>()
    var allcart = MutableLiveData<List<ProductList>>()

    var newAddress = MutableLiveData<MultiAddress>()
    var orderlist = MutableLiveData<ArrayList<Orders>>()
    var total = MutableLiveData<Float>()


    //Api calling function for login validation
    fun validateLogin(
        progressBar: View,
        loginDetails: LoginDetails,
        loginActivity: Activity,
        database: MyDataBase,
    ) {
        progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiCall().getService(fakeStoreAPI)?.authUser(loginDetails)
            withContext(Dispatchers.Main){
                if (response!= null){
                    progressBar.visibility = View.INVISIBLE
                    if (!response.isSuccessful){
                        var found = false
                        val allusers = database.Dao().getAllUsers()
                        for (i in allusers.indices) {
                            if (loginDetails.username == allusers[i].username && loginDetails.password == allusers[i].password) {
                                found = true
                            }
                        }
                        if (!found) {
                            toastmsg.postValue("username or password is incorrect")
                        } else {
                            successful(database, loginDetails, loginActivity)
                        }
                    }else {
                        successful(database, loginDetails, loginActivity)
                    }
                }
            }
        }
    }

    //register user function and saving it to database
    fun registerUser(progressBar: View, addUser: UserDetails, activity: Activity) {
        progressBar.visibility = View.VISIBLE


        CoroutineScope(Dispatchers.IO).launch{
            val response = ApiCall().getService(fakeStoreAPI)?.addUser(addUser)
            withContext(Dispatchers.Main){
                if (response != null) {
                    if (response.isSuccessful){
                        loggedInUser.postValue(addUser)
                        MyPreferences.setValueString(Name, addUser.username)
                        MyPreferences.setValueInt(Flag, 1)
                        changeActivity(activity, HomeActivity())
                        activity.finish()
                    }
                }else{
                    Log.d("fail2", response.toString())
                }
            }
        }

    }

    fun saveUserToDataBase() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiCall().getService(fakeStoreAPI)?.allUserDetails()
            withContext(Dispatchers.Main){
                if (response != null) {
                    if (response.isSuccessful){
                        userList.postValue(response.body())
                    }else{
                        Log.d("fail3", response.code().toString())
                    }
                }
            }
        }
    }

    fun getAllProducts(loading: ProgressBar) {
        loading.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch{
            val response = ApiCall().getService(fakeStoreAPI)?.getAllProducts()
            withContext(Dispatchers.Main){
                if (response!=null){
                    if (response.isSuccessful){
                        Log.d("productlist", response.body().toString())
                        loading.visibility = View.INVISIBLE
                        productlist.postValue(response.body())
                    }
                }
            }
        }


    }

    fun successful(database: MyDataBase, loginDetails: LoginDetails, loginActivity: Activity) {
        toastmsg.postValue("Login Successful")
        loggedInUser.postValue(database.Dao().getUserDetails(loginDetails.username))
        MyPreferences.setValueString(Name, loginDetails.username)
        MyPreferences.setValueInt(Flag, 1)
        changeActivity(loginActivity, HomeActivity())
        loginActivity.finish()
    }


    fun setNewAddress(text: MultiAddress) {
        newAddress.postValue(text)
    }

    fun setOrderList(text: ArrayList<Orders>) {
        orderlist.postValue(text)
    }

    fun setTotal(total: Float) {
        this.total.postValue(total)
    }

}