package com.example.blood_donation.ui.screen.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.blood_donation.ui.components.DropdownSelector
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// 🔄 Registration Screen
@Composable
fun RegisterScreen(auth: FirebaseAuth, db: FirebaseFirestore, onRegistered: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("A+") }
    var location by remember { mutableStateOf("Chennai") }
    var phone by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Complete Your Profile", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        DropdownSelector(
            label = "Blood Group",
            selected = bloodGroup,
            options = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"),
            onSelect = { bloodGroup = it }
        )
        Spacer(Modifier.height(8.dp))
        DropdownSelector(
            label = "Location",
            selected = location,
            options = listOf("Chennai", "Madurai", "Coimbatore", "Erode", "Tirupur"),
            onSelect = { location = it }
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    val donor = hashMapOf(
                        "name" to name,
                        "bloodGroup" to bloodGroup,
                        "location" to location,
                        "email" to auth.currentUser?.email,
                        "phone" to phone
                    )
                    db.collection("donors").document(userId).set(donor)
                        .addOnSuccessListener { onRegistered() }
                        .addOnFailureListener { e -> error = e.message }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Register") }
        error?.let {
            Spacer(Modifier.height(8.dp))
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }
    }
}
