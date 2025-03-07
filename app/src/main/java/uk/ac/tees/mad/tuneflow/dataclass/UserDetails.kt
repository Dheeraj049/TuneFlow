package uk.ac.tees.mad.tuneflow.dataclass

data class UserData(
    val userDetails: UserDetails? = null, val userId: String? = null
)

data class UserDetails(
    val userId: String,
    val email: String?,
    val displayName: String? = "Not Available",
    val isEmailVerified: Boolean,
    val phoneNumber: String? = "Not Available"
)