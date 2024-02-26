package com.example.mykart.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mykart.R
import com.example.mykart.changeActivity
import com.example.mykart.classes.LoginDetails
import com.example.mykart.classes.MyViewModel
import com.example.mykart.database.MyDataBase
import com.example.mykart.databinding.ActivityLoginBinding
import com.example.mykart.hideKeyboard

class LoginActivity : AppCompatActivity() {
    //lateinit variable declaration
    private lateinit var binding : ActivityLoginBinding
    lateinit var viewModel : MyViewModel
    private lateinit var database : MyDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this@LoginActivity)[MyViewModel::class.java]
        database = MyDataBase.getInstance(this)

        //aplly method
        binding.apply {

            //register button onclick Listener
            btnRegister.setOnClickListener {
                changeActivity(this@LoginActivity, RegistrationActivity())
                finish()
            }

            //login button onclick listener
            btnLogin.setOnClickListener {
                if (validate()){
                    viewModel.saveUserToDataBase()
                    viewModel.validateLogin(loading, LoginDetails(edtusername.text.toString(),edtpwd.text.toString()),this@LoginActivity,database)
                }
            }

            rlLoginlayout.setOnClickListener {
                    hideKeyboard(rlLoginlayout,this@LoginActivity)
            }
            //calling observer
            getObserver()
        }
    }

    //validate login
    private fun validate():Boolean{
        var bool = false
        binding.apply {
            if(edtusername.text.isNotEmpty() && edtusername.text.length>3){
                if (edtpwd.text.isNotEmpty() && edtpwd.text.length>3){
                    bool = true
                }else {
                    edtpwd.error = getString(R.string.str_pedValidation)
                }
            }else {
                edtusername.error = getString(R.string.username_validation)
            }
        }
        return bool
    }

    //observer function
    fun getObserver(){
        viewModel.toastmsg.observe(this){
            if (it.isNotEmpty()){
                Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
            }

        }
        viewModel.userList.observe(this){
            database.Dao().insertAllUsers(it)
            Log.d("users",database.Dao().getAllUsers().toString())
        }
    }
}