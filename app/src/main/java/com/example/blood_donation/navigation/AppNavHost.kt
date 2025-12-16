package com.example.blood_donation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.blood_donation.ui.screen.auth.AuthScreen
import com.example.blood_donation.ui.screen.home.HomeScreen
import com.example.blood_donation.ui.screen.auth.RegisterScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// 🔄 Navigation setup
@Composable
fun AppNavHost(auth: FirebaseAuth, db: FirebaseFirestore) {
    val navController = rememberNavController()
    val currentUser = auth.currentUser
    NavHost(
        navController = navController,
        startDestination = if (currentUser == null) "auth" else "home"
    ) {
        composable("auth") {
            AuthScreen(
                onAuthSuccess = { isNewUser ->
                    if (isNewUser) {
                        navController.navigate("register") {
                            popUpTo("auth") { inclusive = true }
                        }
                    } else {
                        navController.navigate("home") {
                            popUpTo("auth") { inclusive = true }
                        }
                    }
                },
                auth = auth
            )
        }
        composable("register") {
            RegisterScreen(auth, db) { navController.navigate("home") }
        }
        composable("home") {
            HomeScreen(
                auth = auth,
                db = db,
                fetchDonors = { bloodGroup, location, onResult ->
                    db.collection("donors")
                        .get()
                        .addOnSuccessListener { result ->
                            val donors = result.documents.map { doc ->
                                mapOf(
                                    "name" to (doc.getString("name") ?: "Unknown"),
                                    "blood_group" to (doc.getString("bloodGroup") ?: "-"),
                                    "location" to (doc.getString("location") ?: "-"),
                                    "phone" to (doc.getString("phone") ?: "-")
                                )
                            }.filter { (bloodGroup == "All" || it["blood_group"] == bloodGroup) &&
                                    (location == "All" || it["location"] == location) }
                            onResult(donors)
                        }
                        .addOnFailureListener { e ->
                            onResult(
                                listOf(
                                    mapOf(
                                        "name" to "Error",
                                        "blood_group" to "-",
                                        "location" to (e.message ?: "Unknown error"),
                                        "phone" to "-"
                                    )
                                )
                            )
                        }
                },
                onLogout = {
                    auth.signOut()
                    navController.navigate("auth") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

    }
}
