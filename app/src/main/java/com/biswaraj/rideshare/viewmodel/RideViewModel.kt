package com.biswaraj.rideshare.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.biswaraj.rideshare.model.Ride
import com.biswaraj.rideshare.model.RideRequest
import javax.inject.Inject

@HiltViewModel
class RideViewModel @Inject constructor() : ViewModel() {

    private val _rides = MutableStateFlow<List<Ride>>(emptyList())
    val rides: StateFlow<List<Ride>> = _rides

    fun addRide(ride: Ride) {
        _rides.value = _rides.value + ride
    }

    fun addRequest(rideId: String, request: RideRequest) {
        val updatedRides = _rides.value.map { ride ->
            if (ride.id == rideId) {
                ride.copy(requests = ride.requests + request)
            } else {
                ride
            }
        }
        _rides.value = updatedRides
    }

    fun getRideById(id: String): Ride? {
        return _rides.value.find { it.id == id }
    }

    fun getLastRide(): Ride? {
        return _rides.value.lastOrNull()
    }
}
