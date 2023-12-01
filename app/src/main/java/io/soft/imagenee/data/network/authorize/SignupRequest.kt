package io.soft.imagenee.data.network.authorize

data class SignupRequest(
    val name: String,
    val surname: String,
    val email: String,
    val password: String
)
