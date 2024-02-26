package com.example.mykart

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import java.util.*

@SuppressLint("RestrictedApi")
//fun myMenu(
//    model: View,
//    context: Activity,
//    binding: ProductListBinding,
//    product: ArrayList<ProductsList>,
//    adapter: MainAdapter,
//    position: Int,
//    viewModel: MyViewModel
//) {
//    val myDatabase = MyDatabase.getInstance(context)
//    val menuBuilder = MenuBuilder(context)
//    val inflater = MenuInflater(context)
//    inflater.inflate(R.menu.newmenu, menuBuilder)
//    val optionsMenu = MenuPopupHelper(context, menuBuilder, model)
//    optionsMenu.setForceShowIcon(true)
//
//    menuBuilder.setCallback(object : MenuBuilder.Callback {
//        @SuppressLint("NotifyDataSetChanged")
//        override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
//
//            return when (item.itemId) {
//                R.id.edit -> {
//                    isCLicked = 1
//                    binding.apply {
//                        btnSave.visibility = View.VISIBLE
//                        showEdittext(heading, price, description)
//
//                        btnSave.setOnClickListener {
//                            isCLicked = 0
//                            myDatabase.userDao().update(
//                                ProductsList(
//                                    product[position].id,
//                                    heading.text.toString(),
//                                    description.text.toString(),
//                                    Integer.parseInt(price.text.toString()),
//                                    product[position].rating,
//                                    product[position].stock,
//                                    product[position].thumbnail,
//                                    product[position].isLiked
//                                )
//                            )
//
//                            hideEdittext(heading, price, description)
//                            btnSave.visibility = View.GONE
//                            adapter.setProductList(
//                                myDatabase.userDao().getProducts(),
//                                context,
//                                viewModel
//                            )
//                            adapter.notifyItemChanged(position)
//                            adapter.notifyItemChanged(position)
//                        }
//
//                    }
//                    true
//                }
//                R.id.delete -> {
//                    isCLicked = 1
//                    if (product[position].isLiked) {
//                        likeCount--
//                        viewModel.setLikeCount(likeCount)
//                    }
//                    myDatabase.userDao().deleteProduct(product[position].id)
//                    product.removeAt(position)
//                    adapter.setProductList(product, context, viewModel)
//                    adapter.notifyDataSetChanged()
//                    isCLicked = 0
//                    true
//                }
//                else ->
//                    false
//
//            }
//        }
//
//        override fun onMenuModeChange(menu: MenuBuilder) {
//
//        }
//    })
//
//    optionsMenu.show()
//
//}
//
//@SuppressLint("MissingInflatedId")
//fun customDialog(
//    model: View,
//    context: Activity,
//    binding: ProductListBinding,
//    product: ArrayList<ProductsList>,
//    adapter: MainAdapter,
//    position: Int,
//    viewModel: MyViewModel
//) {
//    val alert = AlertDialog.Builder(context)
//    val mView: View = context.layoutInflater.inflate(R.layout.custom_alertdialog, null)
//    val yesBtn = mView.findViewById<ImageView>(R.id.yesBtn)
//    val noBtn = mView.findViewById<ImageView>(R.id.noBtn)
//    alert.setView(mView)
//    val alertDialog = alert.create()
//    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//    alertDialog.setCanceledOnTouchOutside(true)
//    yesBtn.setOnClickListener {
//        myMenu(
//            model,
//            context,
//            binding,
//            product,
//            adapter,
//            position,
//            viewModel
//        )
//        alertDialog.dismiss()
//    }
//
//    noBtn.setOnClickListener {
//
//        alertDialog.dismiss()
//    }
//    alertDialog.show()
//}

fun isOnline(context: Activity): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
            return true
        }
    }
    return false
}

fun showEdittext(vararg name: View) {
    name.forEach { view -> view.isFocusableInTouchMode = true }
}

fun hideEdittext(vararg name: View) {
    name.forEach { view -> view.isFocusable = false }
}


fun hideKeyboard(v: View, context: Context) {
    val im = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    im.hideSoftInputFromWindow(v.windowToken, 0)
}


fun showVisibility(vararg name: View) {
    name.forEach { view -> view.visibility = View.VISIBLE }
}

fun hideVisibility(vararg name: View) {
    name.forEach { view -> view.visibility = View.GONE }
}

fun getPath(context: Context, uri: Uri): String? {
    val isKitKat = true

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }

            // TODO handle non-primary volumes
        } else if (isDownloadsDocument(uri)) {
            val id = DocumentsContract.getDocumentId(uri)
            val contentUri: Uri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
            )
            return getDataColumn(context, contentUri, null, null)
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            when (type) {
                "image" -> {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }

                "video" -> {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }

                "audio" -> {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(
                split[1]
            )
            return getDataColumn(context, contentUri, selection, selectionArgs)
        }
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
        return getDataColumn(context, uri, null, null)
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path
    }
    return null
}

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.
 *
 * @param context The context.
 * @param uri The Uri to query.
 * @param selection (Optional) Filter used in the query.
 * @param selectionArgs (Optional) Selection arguments used in the query.
 * @return The value of the _data column, which is typically a file path.
 */
fun getDataColumn(
    context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(
        column
    )
    try {
        cursor = uri?.let {
            context.contentResolver.query(
                it, projection, selection, selectionArgs, null
            )
        }
        if (cursor != null && cursor.moveToFirst()) {
            val column_index: Int = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(column_index)
        }
    } finally {
        cursor?.close()
    }
    return null
}


/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 */
fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 */
fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}

fun checkPermissions(context: Context): Boolean {
    var result = false
    if (ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    ) {

        result = true
    }
    return result
}


//    fun btnShowMessage(btn1: String, btn2: String, activity: Activity): Uri? {
//        var uri: Uri? = null
//        val alert = AlertDialog.Builder(activity)
//        val mView: View = activity.layoutInflater.inflate(R.layout.custom_dialog, null)
//        val camera = mView.findViewById<ImageView>(R.id.imgcamera2)
//        val gallery = mView.findViewById<ImageView>(R.id.imggallery)
//        val text1 = mView.findViewById<TextView>(R.id.txtselect)
//        val text2 = mView.findViewById<TextView>(R.id.txtselect2)
//
//        alert.setView(mView)
//        val alertDialog = alert.create()
//        alertDialog.setCanceledOnTouchOutside(true)
//        if (btn1 == "") {
//            camera.visibility = View.GONE
//            text1.visibility = View.GONE
//        } else {
//            camera.setOnClickListener {
//                alertDialog.dismiss()
//
//                uri = displayCamera(activity)
//
//            }
//        }
//        if (btn2 == "") {
//            gallery.visibility = View.GONE
//            text2.visibility = View.GONE
//        } else {
//            gallery.setOnClickListener {
//                imageChooser(activity)
//            }
//            alertDialog.dismiss()
//        }
//
//        alertDialog.show()
//        return uri
//    }

fun imageChooser(result: ActivityResultLauncher<Intent>) {
    val i = Intent()
    i.type = "image/*"
    i.action = Intent.ACTION_GET_CONTENT

    result.launch(i)
}


fun requestPermission(result: ActivityResultLauncher<Array<String>>) {

    result.launch(
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )

}

fun goToSettings(activity: Activity) {
    val myAppSettings = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:${activity.application.packageName}")
    )
    myAppSettings.addCategory(Intent.CATEGORY_DEFAULT)
    myAppSettings.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    activity.startActivityForResult(
        myAppSettings, 5
    )
}

//fun btnShowMessage(
//    activity: RegistrationActivity,
//    cameraResult: ActivityResultLauncher<Intent>,
//    galleryResult: ActivityResultLauncher<Intent>
//) {
//
//    val alert = AlertDialog.Builder(activity)
//    val mView: View = activity.layoutInflater.inflate(R.layout.custom_dialog, null)
//    val camera = mView.findViewById<ImageView>(R.id.imgcamera2)
//    val gallery = mView.findViewById<ImageView>(R.id.imggallery)
//
//
//    alert.setView(mView)
//    val alertDialog = alert.create()
//    alertDialog.setCanceledOnTouchOutside(true)
//
//    camera.setOnClickListener {
//
//        alertDialog.dismiss()
//    }
//
//    gallery.setOnClickListener {
//        imageChooser(galleryResult)
//        alertDialog.dismiss()
//    }
//
//    alertDialog.show()
//
//}

fun Context.changeActivity(context: Context, context2: Activity) {
    val intent = Intent(context, context2::class.java)
    startActivity(intent)
}

fun changeActivity(context: Context, context2: Activity) {
    val intent = Intent(context, context2::class.java)
    context.startActivity(intent)
}

fun changeFragment(id: Int, fragment: Fragment, fragmentManager: FragmentManager) {
    fragmentManager.beginTransaction().replace(id, fragment).commit()
}

fun changeFragments(id: Int, fragment: Fragment, fragmentManager: FragmentManager, s: String) {
    fragmentManager.beginTransaction().replace(id, fragment, s).addToBackStack(null).commit()
}

fun pickDate(context: Context, view: EditText) {
    val c = Calendar.getInstance()
    val year = c[Calendar.YEAR]
    val month = c[Calendar.MONTH]
    val date = c[Calendar.DATE]


    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker?, year1: Int, month1: Int, dayOfMonth: Int ->

            view.setText("$dayOfMonth/$month1/$year1")
        }, year, month, date
    )
    datePickerDialog.show()
}

@Suppress("DEPRECATION")
fun ViewPager.autoScroll(delay: Long) {
    val handler = android.os.Handler()
    var scroll = 0

    val runnable = object : Runnable {
        override fun run() {
            val count = adapter?.count ?: 0
            setCurrentItem(scroll++ % count, true)

            handler.postDelayed(this, delay)
        }

    }

    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int, positionOffset: Float, positionOffsetPixels: Int
        ) {
            scroll = position + 1
        }

        override fun onPageSelected(position: Int) {

        }

        override fun onPageScrollStateChanged(state: Int) {

        }

    })

    handler.post(runnable)
}
