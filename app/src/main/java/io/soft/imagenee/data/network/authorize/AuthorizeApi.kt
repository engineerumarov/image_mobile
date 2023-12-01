package io.soft.imagenee.data.network.authorize

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthorizeApi {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest)

    @POST("signup")
    suspend fun signup(@Body signupRequest: SignupRequest)
}