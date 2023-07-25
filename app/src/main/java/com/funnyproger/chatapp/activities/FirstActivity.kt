package com.funnyproger.chatapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.funnyproger.chatapp.constants.Constants.FirstActivityContext
import com.funnyproger.chatapp.databinding.ActivityFirstBinding

class FirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        installSplashScreen()
        setContentView(binding.root)
        FirstActivityContext = this

    }
}