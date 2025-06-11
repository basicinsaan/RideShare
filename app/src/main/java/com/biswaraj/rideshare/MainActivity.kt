package com.biswaraj.rideshare

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.biswaraj.rideshare.ui.theme.RideShareTheme
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

data class Ride(
    val driver: String = "You",
    val from: String,
    val to: String,
    val dateTime: String,
    val seats: Int,
    val cost: Double
)

val ridesList = mutableStateListOf<Ride>()

class MainActivity : ComponentActivity() {
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> loadApp()
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> loadApp()
            else -> loadAppWithNoLocation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            loadApp()
        }
    }

    private fun loadApp() {
        setContent {
            RideShareTheme {
                RideShareApp()
            }
        }
    }

    private fun loadAppWithNoLocation() {
        setContent {
            RideShareTheme {
                Text("Location permission denied. Please enable it in settings.")
            }
        }
    }
}

@Composable
fun RideShareApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("postRide") { PostRideScreen(navController) }
        composable(
            "rideDetails/{rideIndex}",
            arguments = listOf(navArgument("rideIndex") { type = NavType.IntType })
        ) { backStackEntry ->
            val rideIndex = backStackEntry.arguments?.getInt("rideIndex") ?: 0
            RideDetailsScreen(navController, rideIndex)
        }
        composable("rideStatus") { RideStatusScreen(navController) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("RideShare") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            GoogleMap(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxWidth()
            ) {
                Marker(
                    state = MarkerState(position = LatLng(37.7749, -122.4194)),
                    title = "Ride Offer"
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { /* TODO */ }) { Text("Time") }
                Button(onClick = { /* TODO */ }) { Text("Distance") }
                Button(onClick = { /* TODO */ }) { Text("Cost") }
            }
            LazyColumn {
                items(ridesList) { ride ->
                    Text(
                        text = "Nearby Rides: ${ride.driver}, ${ride.from}-${ride.to}, $${ride.cost}",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                navController.navigate("rideDetails/${ridesList.indexOf(ride)}")
                            }
                    )
                }
            }
            Button(
                onClick = { navController.navigate("postRide") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            ) {
                Text("Offer a Ride")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostRideScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Offer a Ride") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val from = remember { mutableStateOf("") }
            val to = remember { mutableStateOf("") }
            val dateTime = remember { mutableStateOf("") }
            val seats = remember { mutableStateOf("") }
            val cost = remember { mutableStateOf("") }

            OutlinedTextField(
                value = from.value,
                onValueChange = { from.value = it },
                label = { Text("From") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = to.value,
                onValueChange = { to.value = it },
                label = { Text("To") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = dateTime.value,
                onValueChange = { dateTime.value = it },
                label = { Text("Date & Time (e.g., March 20, 10:00 AM)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = seats.value,
                onValueChange = { seats.value = it },
                label = { Text("Seats Available") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = cost.value,
                onValueChange = { cost.value = it },
                label = { Text("Cost per Rider ($)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val ride = Ride(
                        from = from.value,
                        to = to.value,
                        dateTime = dateTime.value,
                        seats = seats.value.toIntOrNull() ?: 1,
                        cost = cost.value.toDoubleOrNull() ?: 0.0
                    )
                    ridesList.add(ride)
                    navController.navigate("rideStatus")
                },
                modifier = Modifier.align(Alignment.End),
                enabled = from.value.isNotEmpty() && to.value.isNotEmpty() &&
                        dateTime.value.isNotEmpty() && seats.value.isNotEmpty() && cost.value.isNotEmpty()
            ) {
                Text("Post Ride")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RideDetailsScreen(navController: NavController, rideIndex: Int) {
    val ride = ridesList.getOrNull(rideIndex) ?: return
    Scaffold(
        topBar = { TopAppBar(title = { Text("Ride Details") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Driver: ${ride.driver} (4.8★)", style = MaterialTheme.typography.titleMedium)
            GoogleMap(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            ) {
                Marker(
                    state = MarkerState(position = LatLng(37.7749, -122.4194)),
                    title = "Start"
                )
            }
            Text("From: ${ride.from}")
            Text("To: ${ride.to}")
            Text("When: ${ride.dateTime}")
            Text("Seats Left: ${ride.seats}")
            Text("Cost: $${ride.cost} per person")
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("Back")
                }
                Button(onClick = { navController.navigate("rideStatus") }) {
                    Text("Join Ride")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RideStatusScreen(navController: NavController) {
    val ride = ridesList.lastOrNull() ?: return
    Scaffold(
        topBar = { TopAppBar(title = { Text("Ride Confirmed") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding) // Fixed typo: was "pading"
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Your Ride is Confirmed!", style = MaterialTheme.typography.headlineSmall)
            Text("From: ${ride.from}")
            Text("To: ${ride.to}")
            Text("When: ${ride.dateTime}")
            Text("Cost: $${ride.cost}")
            Text("Riders: You + ${ride.driver} + Alex")
            Text("Departs in: 2 hours")
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Back to Home")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    RideShareTheme {
        HomeScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun PostRideScreenPreview() {
    RideShareTheme {
        PostRideScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun RideDetailsScreenPreview() {
    RideShareTheme {
        RideDetailsScreen(navController = rememberNavController(), rideIndex = 0)
    }
}

@Preview(showBackground = true)
@Composable
fun RideStatusScreenPreview() {
    RideShareTheme {
        RideStatusScreen(navController = rememberNavController())
    }
}