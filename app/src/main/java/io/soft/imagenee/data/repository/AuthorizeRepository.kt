package io.soft.imagenee.data.repository

import io.soft.imagenee.data.network.authorize.AuthorizeApi
import io.soft.imagenee.data.network.authorize.LoginRequest
import io.soft.imagenee.data.network.authorize.SignupRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthorizeRepository @Inject constructor(
    private val authorizeApi: AuthorizeApi
) {
    suspend fun login(loginRequest: LoginRequest): Result<Unit> {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                authorizeApi.login(loginRequest)
            }
        }
    }

    suspend fun signup(signupRequest: SignupRequest): Result<Unit> {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                authorizeApi.signup(signupRequest)
            }
        }
    }
}