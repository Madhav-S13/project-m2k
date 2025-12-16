package com.example.blood_donation.ui.screen.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.blood_donation.data.model.Hospital
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun HospitalScreen() {
    val context = LocalContext.current
    var hospitals by remember { mutableStateOf<List<Hospital>>(emptyList()) }
    var selectedHospital by remember { mutableStateOf<Hospital?>(null) }
    val db = FirebaseFirestore.getInstance()

    // ✅ Coroutine-safe Firestore fetch
    LaunchedEffect(Unit) {
        try {
            val snapshot = db.collection("hospitals").get().await()
            snapshot.documents.forEach { doc ->
                Log.d("HospitalDebug", "Raw doc: ${doc.data}")
            }
            hospitals = snapshot.documents.mapNotNull { it.toObject(Hospital::class.java) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            Text("Hospitals", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
        }

        if (hospitals.isEmpty()) {
            item { Text("No hospitals available") }
        } else {
            items(hospitals) { hospital ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { selectedHospital = hospital }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(hospital.name, style = MaterialTheme.typography.titleMedium)
                        Text("Location : ${hospital.location}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }

    selectedHospital?.let { hospital ->
        AlertDialog(
            onDismissRequest = { selectedHospital = null },
            confirmButton = {},
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(hospital.name, style = MaterialTheme.typography.titleLarge)
                    IconButton(onClick = { selectedHospital = null }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            },
            text = {
                val context = LocalContext.current
                Column {
                    Text("Location: ${hospital.location}")

                    // Phone number is tappable to copy
                    Text(
                        text = "Phone: ${hospital.phone}",
                        modifier = Modifier
                            .clickable {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Phone Number", hospital.phone)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Phone number copied!", Toast.LENGTH_SHORT).show()
                            }
                            .padding(vertical = 4.dp)
                    )

                    Spacer(Modifier.height(8.dp))
                    Text("Blood Units Available:", style = MaterialTheme.typography.titleMedium)

                    // Proper ordering of blood groups
                    val orderedTypes = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
                    orderedTypes.forEach { type ->
                        hospital.bloodStock[type]?.let { units ->
                            Row {
                                Text(
                                    text = type,
                                    modifier = Modifier.width(40.dp),
                                    textAlign = TextAlign.Start
                                )
                                Text(": $units units")
                            }
                        }
                    }
                }
            }
        )
    }
}