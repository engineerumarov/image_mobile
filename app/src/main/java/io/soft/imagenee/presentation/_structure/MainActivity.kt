package io.soft.imagenee.presentation._structure

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.soft.imagenee.presentation._structure.ui.theme.ImageneeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageneeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SplashScreen {
                        ImageneeApp(onBoarding = it)
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(
    viewModel: MainViewModel = hiltViewModel(),
    block: @Composable (Boolean) -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val email by viewModel.email.collectAsState()

    if (isLoading.not()) {
        block(email == null)
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    )
}