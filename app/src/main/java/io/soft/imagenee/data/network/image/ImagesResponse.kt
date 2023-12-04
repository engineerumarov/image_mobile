package io.soft.imagenee.data.network.image

import io.soft.imagenee.data.model.Image

data class ImagesResponse(
    val status: String,
    val data: List<Image>
)