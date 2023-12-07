package io.soft.imagenee.data.network.authorize

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthorizeApi {
    @POST("api/login")
    suspend fun login(@Body loginRequest: LoginRequest)

    @POST("api/signup")
    suspend fun signup(@Body signupRequest: SignupRequest)
}