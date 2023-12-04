package io.soft.imagenee.data.repository

import io.soft.imagenee.data.model.Image
import io.soft.imagenee.data.network.image.ImageApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
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

    suspend fun uploadImage(file: File): Result<Unit> {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                api.uploadImage(file)
            }
        }
    }
}