package com.adityaanugrah.skripsiku.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adityaanugrah.skripsiku.retrofit.FirebaseService
import com.adityaanugrah.skripsiku.retrofit.ResultState
import android.util.Patterns


@Composable
fun RegisterScreen(
    context: Context = LocalContext.current,
    navigator: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) } // Tambahkan variabel untuk melacak validitas password
    var isConfirmPasswordValid by remember { mutableStateOf(true) } // Tambahkan variabel untuk melacak validitas Confirm Password

    Scaffold {

        Column(
            Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Register", style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(16.dp))
            if (!isEmailValid) {
                Text(
                    text = "Masukkan email yang benar",
                    color = Color.Red,
                    style = MaterialTheme.typography.caption
                )
            }
            OutlinedTextField(
                value = email,
                onValueChange = { value ->
                    email = value
                    showError = false
                    isEmailValid = isEmailFormatValid(value)
                },
                label = {
                    Text(text = "Email")
                },isError = !isEmailValid // Menandai TextField sebagai error jika email tidak valid
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (!isPasswordValid) {
                Text(
                    text = "Contoh Password : Aditya10",
                    color = Color.Red,
                    style = MaterialTheme.typography.caption
                )
            }
            OutlinedTextField(
                value = password,
                onValueChange = { value ->
                    password = value
                    showError = false
                    isPasswordValid = value.length >= 8 // Periksa panjang password
                },
                label = {
                    Text(text = "Password")
                },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisibility) "Hide password" else "Show password"
                        )
                    }
                },
                isError = !isPasswordValid // Menandai TextField sebagai error jika password tidak valid
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (!isConfirmPasswordValid) {
                Text(
                    text = "Confirm Password harus sama dengan Password",
                    color = Color.Red,
                    style = MaterialTheme.typography.caption
                )
            }
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { value ->
                    confirmPassword = value
                    showError = false
                    isConfirmPasswordValid = value == password // Periksa apakah Confirm Password sama dengan Password
                    isPasswordValid = isPasswordFormatValid(value) // Periksa apakah Password memenuhi format yang benar
                },
                label = {
                    Text(text = "Confirm Password")
                },
                visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = {
                        confirmPasswordVisibility = !confirmPasswordVisibility
                    }) {
                        Icon(
                            imageVector = if (confirmPasswordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (confirmPasswordVisibility) "Hide password" else "Show password"
                        )
                    }
                },
                isError = !isConfirmPasswordValid // Menandai TextField sebagai error jika Confirm Password tidak valid
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (showError) {
                Text(
                    text = "Harap Isi Semua from",
                    color = Color.Red,
                    style = MaterialTheme.typography.caption
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Button(onClick = {
                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    showError = true
                } else if (!isEmailValid) {
                    Toast.makeText(context, "Masukkan email yang benar", Toast.LENGTH_SHORT).show()
                } else if (!isPasswordValid) { // Periksa validitas password sebelum melakukan registrasi
                    Toast.makeText(context, "Contoh Password : Aditya10", Toast.LENGTH_SHORT).show()
                }else if (!isConfirmPasswordValid) { // Periksa validitas Confirm Password sebelum melakukan registrasi
                    Toast.makeText(context, "Confirm Password harus sama dengan Password", Toast.LENGTH_SHORT).show()
                } else {
                    FirebaseService.register(email, password) { result ->
                        when (result) {
                            is ResultState.Success -> {
                                navigator(Screen.Login.route)
                            }
                            is ResultState.Error -> {
                                Toast.makeText(context, "Akun telah terdaftar", Toast.LENGTH_SHORT).show()
                            }
                            else -> {}
                        }
                    }
                }
            }) {
                Text(text = "Register")
            }
        }
    }
}
fun isEmailFormatValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
fun isPasswordFormatValid(password: String): Boolean {
    val regex = Regex("^(?=.*[A-Z])(?=.*\\d).+\$")
    return regex.matches(password)
}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    RegisterScreen {

    }
}