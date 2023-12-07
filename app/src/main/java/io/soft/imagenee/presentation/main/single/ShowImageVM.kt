package io.soft.imagenee.presentation.main.single

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.soft.imagenee.data.repository.ImageRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowImageVM @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _effect = MutableSharedFlow<Effect>()
    val effect = _effect.asSharedFlow()

    fun updateImage(id: String, title: String) {
        _loading.update { true }
        viewModelScope.launch {
            repository.updateImage(id, title)
                .onSuccess { _effect.emit(Effect.UpdateSuccess) }
                .onFailure { error -> _effect.emit(Effect.Error(error.message ?: "Unknown error")) }
        }
        _loading.update { false }
    }
}

sealed interface Effect {
    data class Error(val message: String) : Effect
    object UpdateSuccess : Effect
}