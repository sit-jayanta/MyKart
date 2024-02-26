package com.example.mykart.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.mykart.R
import com.example.mykart.classes.Name
import com.example.mykart.database.MyDataBase
import com.example.mykart.databinding.FragmentProfileBinding
import com.example.mykart.utils.AppConstants
import com.example.mykart.utils.MyPreferences


class ProfileFragment : Fragment() {
    //lateinit variables
private lateinit var binding :FragmentProfileBinding
private lateinit var database : MyDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //getting database instance
        database = MyDataBase.getInstance(requireContext())
        val user = database.Dao().getUserDetails(MyPreferences.getValueString(AppConstants.Name,"").toString())

        binding.profile = user
    }


}