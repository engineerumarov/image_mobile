package io.soft.imagenee.data.model

import com.google.gson.annotations.SerializedName

data class Image(
    val name: String,
    val image: String,
    val userId: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("__v")
    val v: Int
) {
    var isSelected = false
}