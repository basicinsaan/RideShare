package com.biswaraj.rideshare.model

data class RideRequest(
    val requester: User,
    val requestedStart: String,
    val requestedEnd: String,
    val status: RideRequestStatus = RideRequestStatus.PENDING
)

enum class RideRequestStatus {
    PENDING, ACCEPTED, REJECTED
}