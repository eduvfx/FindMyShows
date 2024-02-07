package com.example.findmyshows

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val handler = Handler(Looper.getMainLooper())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val splashImg: ImageView = findViewById(R.id.splashImg)
        splashImg.alpha = 0f
        splashImg.animate().setDuration(500).alpha(1f).withEndAction {
            handler.postDelayed({
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                @Suppress("DEPRECATION")
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }, 2000)
        }
    }
}