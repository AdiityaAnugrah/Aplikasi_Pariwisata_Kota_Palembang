package com.adityaanugrah.skripsiku.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adityaanugrah.skripsiku.R
import com.adityaanugrah.skripsiku.retrofit.SharedPref
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ProfileScreen(goToLogin: () -> Unit = {}) {
    val context = LocalContext.current
    var pref by remember {
        mutableStateOf(SharedPref(context).isLogin)
    }
    var email by remember {
        mutableStateOf("")
    }
    val auth = FirebaseAuth.getInstance()

    // Fungsi untuk mengambil data email pengguna dari Firebase
    fun fetchUserEmail() {
        val user = auth.currentUser
        if (user != null) {
            val userEmail = user.email
            if (userEmail != null) {
                email = userEmail
            }
        }
    }

    fetchUserEmail()


    Scaffold(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (pref) {
                Image(
                    painter = painterResource(id = R.drawable.jj),
                    contentDescription = stringResource(id = R.string.Katakatalogin),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Text(text = "Login berhasil")
                Text(text = "$email")
                Button(onClick = {
                    pref = false
                    SharedPref(context).isLogin = false
                }) {
                    Text(text = "Log out")
                }
            } else {
                Image(
                    painter = painterResource(id = R.drawable.pp),
                    contentDescription = stringResource(id = R.string.Katakatalogin),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Text(text = " Silahkan login terlebih dahulu", style = MaterialTheme.typography.body1)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    goToLogin()
                }) {
                    Text(text = "Login")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    ProfileScreen()
}