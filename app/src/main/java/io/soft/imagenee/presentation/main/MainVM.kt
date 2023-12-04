package io.soft.imagenee.presentation.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.soft.imagenee.data.model.Image
import io.soft.imagenee.data.repository.ImageRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {
    private val _effect = MutableSharedFlow<UIEffect>()
    val effect = _effect.asSharedFlow()

    private val _state = MutableStateFlow<ImagesState>(ImagesState.NotLoading)
    val state = _state.asStateFlow()

    private val _deleteMode = MutableStateFlow(false)
    val deleteMode = _deleteMode.asStateFlow()

    init {
        getAllImages()
    }

    private fun getAllImages() {
        _state.update { ImagesState.Loading }
        viewModelScope.launch {
            repository
                .getAllImages()
                .onSuccess { list -> _state.update { ImagesState.Success(list) } }
                .onFailure { error ->
                    _state.update {
                        ImagesState.Error(
                            error.message ?: "Unknown error"
                        )
                    }
                }
        }
        _state.update { ImagesState.NotLoading }
    }

    fun uploadImage(uri: Uri) {
        viewModelScope.launch {
            val file = uri.path?.let {
                File(it)
            } ?: return@launch

            _effect.emit(UIEffect.FileUploading)
            repository.uploadImage(file)
                .onFailure {
                    _effect.emit(
                        UIEffect.FileUploadFailure(
                            it.message ?: "Unknown error"
                        )
                    )
                }
            _effect.emit(UIEffect.Processed)

            // refresh
            getAllImages()
        }
    }

    fun select(id: String) {
        val state = _state.value

        if (state !is ImagesState.Success) {
            return
        }

        val newImages = state.images?.map {
            it
        }

        _deleteMode.update {
            newImages?.any {
                it.isSelected
            } ?: false
        }

        _state.update {
            state.copy(
                images = newImages
            )
        }
    }
}

sealed interface ImagesState {
    object Loading : ImagesState
    object NotLoading : ImagesState
    data class Success(val images: List<Image>? = null) : ImagesState
    data class Error(val message: String) : ImagesState
}

sealed interface UIEffect {
    object FileUploading : UIEffect
    object Processed : UIEffect
    data class FileUploadFailure(val failureReason: String) : UIEffect
}