package com.biswaraj.rideshare.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.biswaraj.rideshare.model.RideRequest
import com.biswaraj.rideshare.model.RideRequestStatus
import com.biswaraj.rideshare.model.User
import com.biswaraj.rideshare.viewmodel.RideViewModel

@Composable
fun RequestRideScreen(
    rideId: String,
    currentUser: User,
    rideViewModel: RideViewModel
) {
    var start by remember { mutableStateOf("") }
    var end by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Request to Join Ride", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = start,
            onValueChange = { start = it },
            label = { Text("Your Start Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = end,
            onValueChange = { end = it },
            label = { Text("Your End Location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (start.isNotBlank() && end.isNotBlank()) {
                val request = RideRequest(
                    requester = currentUser,
                    requestedStart = start,
                    requestedEnd = end,
                    status = RideRequestStatus.PENDING
                )
                rideViewModel.addRequest(rideId, request)
                statusMessage = "Request sent!"
                start = ""
                end = ""
            } else {
                statusMessage = "Please fill in both fields"
            }
        }) {
            Text("Send Request")
        }

        Spacer(modifier = Modifier.height(12.dp))
        if (statusMessage.isNotEmpty()) {
            Text(text = statusMessage)
        }
    }
}
