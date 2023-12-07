package io.soft.imagenee.data.repository

import io.soft.imagenee.data.model.Image
import io.soft.imagenee.data.network.image.AddImageRequest
import io.soft.imagenee.data.network.image.ImageApi
import io.soft.imagenee.data.network.image.UpdateImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val api: ImageApi
) {
    suspend fun getAllImages(): Result<List<Image>> {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                api
                    .getAllImages()
                    .data
            }
        }
    }

    suspend fun uploadImage(addImageRequest: AddImageRequest): Result<Unit> {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                api.uploadImage(
                    name = MultipartBody.Part.createFormData("name", addImageRequest.title),
                    image = MultipartBody.Part.createFormData(
                        "image",
                        addImageRequest.file.name,
                        addImageRequest.file.asRequestBody()
                    ),
                    userId = MultipartBody.Part.createFormData("userId", addImageRequest.userId),
                )
            }
        }
    }

    suspend fun updateImage(id: String, title: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                api.updateImage(
                    id,
                    UpdateImageRequest(title)
                )
            }
        }
    }

    suspend fun deleteImage(id: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                api.deleteImage(id)
            }
        }
    }
}