package com.example.mykart.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mykart.R
import com.example.mykart.classes.Address
import com.example.mykart.classes.Geolocation
import com.example.mykart.classes.MyViewModel
import com.example.mykart.classes.Name
import com.example.mykart.database.MyDataBase
import com.example.mykart.database.UserDetails
import com.example.mykart.databinding.ActivityRegistrationBinding
import com.example.mykart.getPath
import com.example.mykart.hideKeyboard
import java.io.File
import java.util.Date
import java.util.Objects

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private var path: String? = ""
    private var uri: Uri? = null
    private lateinit var viewModel: MyViewModel
    private lateinit var database: MyDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration)
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        database = MyDataBase.getInstance(this)

        binding.apply {
            profileImage.setOnClickListener {
                btnShowMessage()
            }

            btnSignup.setOnClickListener {
                val id = database.Dao().getAllUsers().count() + 1
                if (validateSignUp()) {
                    viewModel.registerUser(
                        pbLoading, UserDetails(
                            id,
                            edtEmail.text.toString(),
                            edtUsername.text.toString(),
                            edtpwd.text.toString(),
                            Name(edtfirstName.text.toString(), edtLastName.text.toString()),
                            Address(
                                edtCity.text.toString(),
                                edtStreet.text.toString(),
                                edtnumber.text.toString(),
                                edtZipcode.text.toString(),
                                Geolocation(
                                    edtlatitude.text.toString(),
                                    edtlongitude.text.toString()
                                )
                            ),
                            edtPhone.text.toString()
                        ), this@RegistrationActivity
                    )

                }

            }
            cardviewRegistration.setOnClickListener {
                hideKeyboard(cardviewRegistration, this@RegistrationActivity)
            }
            observer()
        }
    }

    private fun observer() {
        viewModel.loggedInUser.observe(this) {
            if (viewModel.loggedInUser.value?.email!!.isNotEmpty()) {
                database.Dao().addUserToDataBase(it)
            }
        }
    }


    private val galleryResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            path = result.data?.data?.let { getPath(this, it).toString() }
            binding.profileImage.setImageURI(result.data?.data)

        }
    }
    private val cameraResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            path = result.data?.data.toString()
            binding.profileImage.setImageURI(uri)

        }
    }

    private fun validateSignUp(): Boolean {

        var bool = false
        binding.apply {
            if (Patterns.EMAIL_ADDRESS.matcher(edtEmail.text.toString()).matches()) {
                if (edtUsername.text.length > 3) {
                    if (edtpwd.text.length > 3) {
                        if (edtfirstName.text.length > 3) {
                            if (edtLastName.text.length > 3) {
                                if (edtCity.text.length > 3) {
                                    if (edtStreet.text.isNotEmpty()) {
                                        if (edtnumber.text.isNotEmpty()) {
                                            if (edtZipcode.text.length > 3) {
                                                if (edtlatitude.text.length > 3) {
                                                    if (edtlongitude.text.length > 3) {
                                                        bool = true
                                                    } else {
                                                        edtlongitude.error =
                                                            "Longitude format should be xx.xxxx. eg- 78.9876"
                                                    }
                                                } else {
                                                    edtlatitude.error =
                                                        "Latitude format should be xx.xxxx. eg- 78.9876"
                                                }
                                            } else {
                                                edtZipcode.error =
                                                    "Zipcode cannot be less than 4 numbers"
                                            }
                                        } else {
                                            edtnumber.error =
                                                "house number cannot be less than 4 numbers"
                                        }
                                    } else {
                                        edtStreet.error = "Street number cannot be empty"
                                    }
                                } else {
                                    edtCity.error = "City name cannot be less than 4 characters"
                                }
                            } else {
                                edtLastName.error = "Lastname cannot be less than 4 characters"
                            }
                        } else {
                            edtfirstName.error = "Firstname cannot be less than 4 characters"
                        }
                    } else {
                        edtpwd.error = "Password cannot be less than 4 characters"
                    }
                } else {
                    edtUsername.error = "Username cannot be less than 4 characters"
                }
            } else {
                edtEmail.error = "Enter a valid email address"
            }
        }
        return bool
    }

    private fun btnShowMessage() {

        val alert = AlertDialog.Builder(this)
        val mView: View = this.layoutInflater.inflate(R.layout.custom_dialog, null)
        val camera = mView.findViewById<ImageView>(R.id.imgcamera2)
        val gallery = mView.findViewById<ImageView>(R.id.imggallery)

        alert.setView(mView)
        val alertDialog = alert.create()
        alertDialog.setCanceledOnTouchOutside(true)

        camera.setOnClickListener {

            displayCamera()
            alertDialog.dismiss()

        }

        gallery.setOnClickListener {
            imageChooser()
            alertDialog.dismiss()
        }


        alertDialog.show()

    }

    private fun imageChooser() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        galleryResult.launch(i)
    }

    private fun displayCamera() {
        val destPath: String? = Objects.requireNonNull(
            Objects.requireNonNull(
                this.getExternalFilesDir(null)!!
            )
        ).absolutePath
        val imagesFolder = File(destPath, resources.getString(R.string.app_name))
        try {
            imagesFolder.mkdirs()
            val imgFile = File(imagesFolder, Date().time.toString() + ".jpg")
            val imagePath = FileProvider.getUriForFile(
                this,
                "com.example.mykart" + ".provider",
                imgFile
            )
            uri = imagePath
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePath)
            cameraResult.launch(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}