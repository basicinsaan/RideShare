package com.biswaraj.rideshare.model

data class Ride(
    val id: String = "",
    val driver: String = "You",
    val from: String,
    val to: String,
    val dateTime: String,
    val seats: Int,
    val cost: Double,
    val fromLat: Double = 37.7749,  // dummy default
    val fromLng: Double = -122.4194,
    val toLat: Double = 37.7849,
    val toLng: Double = -122.4094,
    val requests: List<RideRequest> = emptyList()
)

