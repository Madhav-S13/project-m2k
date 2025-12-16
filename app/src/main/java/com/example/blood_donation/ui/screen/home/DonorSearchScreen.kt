package com.example.blood_donation.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.blood_donation.ui.components.DonorDetailDialog
import com.example.blood_donation.ui.components.DropdownSelector

@Composable
fun DonorSearchScreen(fetchDonors: (String, String, (List<Map<String, String>>) -> Unit) -> Unit) {
    var bloodGroup by remember { mutableStateOf("All") }
    var location by remember { mutableStateOf("All") }
    var donors by remember { mutableStateOf(listOf<Map<String, String>>()) }
    var selectedDonor by remember { mutableStateOf<Map<String, String>?>(null) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
    ) {
        DropdownSelector(
            label = "Blood Group",
            selected = bloodGroup,
            options = listOf("All", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"),
            onSelect = { bloodGroup = it }
        )
        Spacer(modifier = Modifier.height(8.dp))
        DropdownSelector(
            label = "Location",
            selected = location,
            options = listOf("All", "Chennai", "Madurai", "Coimbatore", "Erode", "Tirupur"),
            onSelect = { location = it }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { fetchDonors(bloodGroup, location) { donors = it } },
            modifier = Modifier.fillMaxWidth()) { Text("Search Donors") }

        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(donors) { donor ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { selectedDonor = donor },
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        donor["name"] ?: "Unknown",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        selectedDonor?.let { donor ->
            DonorDetailDialog(donor = donor, onDismiss = { selectedDonor = null })
        }
    }
}