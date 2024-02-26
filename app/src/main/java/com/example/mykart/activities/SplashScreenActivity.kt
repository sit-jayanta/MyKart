package com.example.mykart.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.example.mykart.R
import com.example.mykart.changeActivity
import com.example.mykart.utils.AppConstants.Companion.Flag
import com.example.mykart.utils.MyPreferences.Companion.getValueInt

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Handler().postDelayed({
        Handler(Looper.getMainLooper()).postDelayed({

            if (getValueInt(Flag,0)==1){
                changeActivity(this,HomeActivity())
                finish()
            }else {

                changeActivity(this,LoginActivity())
                finish()
            }
        }, 4000)
    }
}