package io.soft.imagenee.data.network.image

import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ImageApi {
    @GET("/api/image")
    suspend fun getAllImages(): ImagesResponse

    @Multipart
    @POST("/api/image")
    suspend fun uploadImage(
        @Part name: MultipartBody.Part,
        @Part image: MultipartBody.Part,
        @Part userId: MultipartBody.Part,
    )

    @PUT("api/image/{id}")
    suspend fun updateImage(
        @Path("id") id: String,
        @Body updateImageRequest: UpdateImageRequest
    )

    @DELETE("api/image/{id}")
    suspend fun deleteImage(@Path("id") id: String)
}