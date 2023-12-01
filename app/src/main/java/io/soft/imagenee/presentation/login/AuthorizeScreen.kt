package io.soft.imagenee.presentation.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@ExperimentalMaterial3Api
@Composable
fun AuthorizeScreen(
    onBack: () -> Unit,
    isSignIn: Boolean,
    onAuthorizeSuccess: () -> Unit,
    viewModel: AuthorizeVM = hiltViewModel()
) {
    val context = LocalContext.current
    val loading by viewModel.loading.collectAsState()

    var name = remember { "" }
    var surname = remember { "" }
    var email = remember { "" }
    var password = remember { "" }

    LaunchedEffect(key1 = Unit) {
        viewModel
            .effect
            .collect {
                when (it) {
                    is Effect.AuthorizeSuccess -> onAuthorizeSuccess()
                    is Effect.Error -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = name,
            onValueChange = {
                name = it
            },
            label = {
                Text(text = "Name...")
            }
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = surname,
            onValueChange = {
                surname = it
            },
            label = {
                Text(text = "Surname...")
            }
        )

        TextField(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(text = "Email...")
            }
        )

        TextField(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(text = "Password...")
            }
        )

        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(0.4f)
            ) {
                Text(text = "Back")
            }

            Button(
                modifier = Modifier.fillMaxWidth(0.4f),
                onClick = {
                    if (loading.not()) {
                        if (isSignIn) {
                            viewModel.login(email, password)
                        } else {
                            viewModel.signup(name, surname, email, password)
                        }
                    }
                }
            ) {
                if (loading)
                    CircularProgressIndicator(modifier = Modifier.scale(0.5f))
                else
                    Text(text = if (isSignIn) "Login" else "Sign Up")
            }
        }
    }
}