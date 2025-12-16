package com.example.blood_donation.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun HomeScreen(
    auth: FirebaseAuth,
    db: FirebaseFirestore,
    fetchDonors: (String, String, (List<Map<String, String>>) -> Unit) -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("Donors") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(18.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // App title
        Text(
            text = "Project: M2K",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Blocky navigation panel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // reduce row height too (70 → 56 like Material default)
        ) {
            listOf("Profile", "Donors", "Camps", "Hospitals").forEach { tab ->
                Button(
                    onClick = { selectedTab = tab },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == tab)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondaryContainer
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp) // 👈 trimmed padding
                ) {
                    Text(
                        text = tab,
                        color = if (selectedTab == tab)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSecondaryContainer,
                        maxLines = 1 // 👈 ensures no wrapping
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Show tab content
        when (selectedTab) {
            "Profile" -> ProfileScreen(auth, db, onLogout)
            "Donors" -> DonorSearchScreen(fetchDonors)
            "Camps" -> DonationCampScreen()
            "Hospitals" -> HospitalScreen()
        }
    }
}
