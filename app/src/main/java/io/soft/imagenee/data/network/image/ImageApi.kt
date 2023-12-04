package io.soft.imagenee.data.network.image

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.io.File

interface ImageApi {
    @GET("/api/image")
    suspend fun getAllImages(): ImagesResponse

    @POST("image")
    suspend fun uploadImage(@Body file: File)
}