package com.example.a306simulation1.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import android.net.Uri

@Composable
fun MapScreen(
    photoUris: List<Uri>,
    onCameraButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Your map implementation goes here
        Text("Map View Placeholder")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onCameraButtonClick) {
            Text("Open Camera")
        }

        // Display saved photos if needed
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            photoUris.forEach { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = "Saved photo",
                    modifier = Modifier
                        .size(100.dp)
                )
            }
        }
    }
}