package com.example.blood_donation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.blood_donation.navigation.AppNavHost
import com.example.blood_donation.ui.screen.splash.VideoSplashScreen
import com.example.blood_donation.ui.theme.BloodDonationTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        setContent {
            var showSplash by remember { mutableStateOf(true) }
            if (showSplash) {
                VideoSplashScreen { showSplash = false }
            } else {
                BloodDonationTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        AppNavHost(auth, db)
                    }
                }
            }
        }
    }
}