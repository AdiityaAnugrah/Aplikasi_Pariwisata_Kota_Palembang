package com.adityaanugrah.skripsiku.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adityaanugrah.skripsiku.retrofit.FirebaseService
import com.adityaanugrah.skripsiku.retrofit.ResultState
import com.adityaanugrah.skripsiku.retrofit.SharedPref
@Composable
fun LoginScreen(
    context: Context = LocalContext.current,
    goToRegister: () -> Unit,
    goToBeranda: () -> Unit,
) {
    val pref = SharedPref(context)

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisibility by remember {
        mutableStateOf(false)
    }
    var showError by remember {
        mutableStateOf(false)
    }

    Scaffold {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Login", style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { value ->
                    email = value
                },
                label = {
                    Text(text = "Email")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { value ->
                    password = value
                },
                visualTransformation = if (passwordVisibility) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                label = {
                    Text(text = "Password")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisibility) "Hide password" else "Show password"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (showError) {
                Text(
                    text = "Password Anda salah",
                    color = Color.Red,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Button(onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Harap lengkapi semua from", Toast.LENGTH_SHORT).show()
                } else {
                    FirebaseService.login(email, password) { result ->
                        when (result) {
                            is ResultState.Success -> {
                                pref.isLogin = true
                                goToBeranda()
                            }

                            is ResultState.Error -> {
                                Toast.makeText(context, "Email atau Password Anda salah", Toast.LENGTH_SHORT).show()
                            }

                            else -> {}
                        }
                    }
                }
            }) {
                Text(text = "Login")
            }

            Spacer(modifier = Modifier.height(30.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Belum punya akun?")
                TextButton(onClick = {
                    goToRegister()
                }) {
                    Text(text = "Register")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    LoginScreen(goToBeranda = {

    }, goToRegister = {

    })
}