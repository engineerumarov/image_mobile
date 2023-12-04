package io.soft.imagenee.presentation.main

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import io.soft.imagenee.data.model.Image

@Composable
fun MainScreen(
    viewModel: MainVM = hiltViewModel(),
    onOpenImage: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var uploading = remember { false }
    val deleteMode by viewModel.deleteMode.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            val uri = it ?: return@rememberLauncherForActivityResult
            viewModel.uploadImage(uri)
        }
    )

    LaunchedEffect(key1 = Unit) {
        viewModel
            .effect
            .collect {
                when (it) {
                    is UIEffect.FileUploadFailure -> Toast.makeText(
                        context,
                        it.failureReason,
                        Toast.LENGTH_SHORT
                    ).show()

                    UIEffect.FileUploading -> uploading = true
                    UIEffect.Processed -> uploading = false
                }
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when (val currentState = state) {
                ImagesState.Loading -> CircularProgressIndicator()
                ImagesState.NotLoading -> {}
                is ImagesState.Success -> Success(
                    currentState.images,
                    onImageSelected = viewModel::select,
                    onOpenImage = onOpenImage
                )

                is ImagesState.Error -> Text(text = currentState.message)
            }

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                onClick = {
                    if (uploading.not())
                        launcher.launch("image/*")
                },
            ) {
                if (uploading.not())
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                else CircularProgressIndicator(modifier = Modifier.scale(0.5f))
            }
        }
    }
}

@Composable
fun TopAppBar() {

}

@Composable
fun Success(
    images: List<Image>?,
    onImageSelected: (String) -> Unit,
    onOpenImage: () -> Unit
) {
    images ?: return
    LazyVerticalGrid(
        contentPadding = PaddingValues(4.dp),
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2)
    ) {
        items(images) { image ->
            ImageItem(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(4.dp),
                image = image,
                onSelected = {
                    onImageSelected(image.id)
                },
                onOpenImage = onOpenImage
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageItem(
    modifier: Modifier,
    image: Image,
    onSelected: () -> Unit,
    onOpenImage: () -> Unit
) {
    val painter = rememberAsyncImagePainter(model = image.path)
    Box(modifier = modifier.combinedClickable(
        onClick = {
            if (image.isSelected) {
                onSelected()
            } else {
                onOpenImage()
            }
        },
        onLongClick = {
            onSelected()
        }
    )) {
        if (image.isSelected) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null
            )
        }

        Image(
            painter = painter,
            contentDescription = null,
            modifier = modifier.fillMaxSize(1f),
            contentScale = ContentScale.Crop
        )
    }
}