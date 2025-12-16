package com.example.blood_donation.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

// 🔄 Donor detail popup
@Composable
fun DonorDetailDialog(donor: Map<String, String>, onDismiss: () -> Unit) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(donor["name"] ?: "Unknown", style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary)
                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close, contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface)
                }
            }
        },
        text = {
            Column {
                Text("Blood Group: ${donor["blood_group"]}", color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(4.dp))
                Text("Location: ${donor["location"]}", color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(8.dp))
                donor["phone"]?.let { phone ->
                    Text(
                        text = "📞 : $phone",
                        modifier = Modifier
                            .clickable {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Phone Number", phone))
                                Toast.makeText(context, "Phone number copied!", Toast.LENGTH_SHORT).show()
                            }
                            .padding(4.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    )
}