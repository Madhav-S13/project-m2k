@file:Suppress("DEPRECATION")

package com.example.blood_donation.ui.screen.home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.core.content.ContextCompat.startActivity
import com.example.blood_donation.data.model.Camp
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("UseKtx")
@Composable
fun DonationCampScreen() {
    val context = LocalContext.current
    var camps by remember { mutableStateOf<List<Camp>>(emptyList()) }
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        db.collection("camps")
            .get()
            .addOnSuccessListener { result ->
                camps = result.documents.mapNotNull { it.toObject(Camp::class.java) }
            }
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            Text("Donation Camps", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
        }

        if (camps.isEmpty()) {
            item { Text("No camps available") }
        } else {
            items(camps) { camp ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Name: ${camp.name}")
                        Text("Organizer: ${camp.organizer}")
                        Text("Phone: ${camp.phone}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(onClick = {
                            camp.location?.let { loc ->
                                val gmmIntentUri = Uri.parse(
                                    "geo:${loc.latitude},${loc.longitude}?q=${loc.latitude},${loc.longitude}(${camp.name})"
                                )
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                mapIntent.setPackage("com.google.android.apps.maps")
                                startActivity(context, mapIntent, null)
                            }
                        }) {
                            Text("Open in Maps")
                        }
                    }
                }
            }
        }
    }
}