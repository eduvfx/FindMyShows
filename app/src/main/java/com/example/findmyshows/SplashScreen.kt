package com.example.findmyshows

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val splashImg: ImageView = findViewById(R.id.splashImg)
        splashImg.alpha = 0f
        splashImg.animate().setDuration(500).alpha(1f).withEndAction {
            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }, SPLASH_DELAY)
        }
    }

    companion object {
        private const val SPLASH_DELAY = 2000L // 2 seconds delay
    }
}