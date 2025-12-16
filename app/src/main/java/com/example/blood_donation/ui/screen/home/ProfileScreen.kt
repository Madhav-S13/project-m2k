package com.example.blood_donation.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun ProfileScreen(
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    onLogout: () -> Unit
) {
    LocalContext.current
    var userData by remember { mutableStateOf<Map<String, String>?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    // Fetch user data once when the screen is first composed
    LaunchedEffect(auth.currentUser?.uid) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection("donors").document(uid).get()
                .addOnSuccessListener { doc ->
                    userData = mapOf(
                        "name" to (doc.getString("name") ?: "Unknown"),
                        "bloodGroup" to (doc.getString("bloodGroup") ?: "-"),
                        "location" to (doc.getString("location") ?: "-"),
                        "phone" to (doc.getString("phone") ?: "-")
                    )
                }
                .addOnFailureListener { e ->
                    error = e.message ?: "Error fetching profile"
                }
        } else {
            error = "User not logged in"
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Profile", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (error != null) {
            Text("Error: $error", color = MaterialTheme.colorScheme.error)
        } else if (userData == null) {
            Text("Loading...", style = MaterialTheme.typography.bodyLarge)
        } else {
            Text("Name: ${userData!!["name"]}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Blood Group: ${userData!!["bloodGroup"]}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Location: ${userData!!["location"]}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Phone: ${userData!!["phone"]}", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) { Text("Logout") }
    }
}