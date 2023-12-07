package io.soft.imagenee.data.network.image

import java.io.File

data class AddImageRequest(
    val file: File,
    val title: String,
    val userId: String = "6565d731c7138a8dfb6a8461"
)
