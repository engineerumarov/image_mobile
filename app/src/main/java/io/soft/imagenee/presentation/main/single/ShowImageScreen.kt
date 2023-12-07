package io.soft.imagenee.presentation.main.single

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceEvenly
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import io.soft.imagenee.BuildConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowImageScreen(
    imageId: String,
    imageName: String,
    imagePath: String,
    onBack: () -> Unit,
    viewModel: ShowImageVM = hiltViewModel()
) {
    val painter = rememberAsyncImagePainter(model = BuildConfig.BASE_URL + "../static/$imagePath")
    val loading by viewModel.loading.collectAsState()
    var titleEdited by remember { mutableStateOf(imageName) }
    var title by remember { mutableStateOf(imageName) }
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel
            .effect
            .collect {
                when (it) {
                    is Effect.Error -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                        .show()

                    Effect.UpdateSuccess -> {
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT)
                            .show()
                        title = titleEdited
                    }
                }
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.clickable {
                            showDialog = true
                        },
                        text = title,
                        fontSize = 18.sp,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f),
                painter = painter,
                contentDescription = null,
            )
        }

        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        if (showDialog) {
            Dialog(
                onDismissRequest = {
                    showDialog = false
                },
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.7f)
                ) {
                    Column(horizontalAlignment = CenterHorizontally) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "Image title",
                            style = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.W900
                            )
                        )

                        TextField(
                            modifier = Modifier
                                .fillMaxWidth(0.9f),
                            shape = RoundedCornerShape(8.dp),
                            value = titleEdited,
                            onValueChange = {
                                titleEdited = it
                            },
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = SpaceEvenly
                        ) {
                            Button(onClick = {
                                titleEdited = imageName
                                showDialog = false
                            }) {
                                Text(text = "Cancel")
                            }

                            Button(onClick = {
                                viewModel.updateImage(imageId, titleEdited)
                                showDialog = false
                            }) {
                                Text(text = "Save")
                            }
                        }
                    }
                }
            }
        }
    }
}