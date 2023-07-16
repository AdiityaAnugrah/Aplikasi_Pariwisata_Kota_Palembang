package com.adityaanugrah.skripsiku.activity

import android.content.Intent
import androidx.activity.ComponentActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.adityaanugrah.skripsiku.R

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // LOOP KE MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this,EntryActivity::class.java)
            startActivity(intent)
            finish()
        },5000)
    }
}