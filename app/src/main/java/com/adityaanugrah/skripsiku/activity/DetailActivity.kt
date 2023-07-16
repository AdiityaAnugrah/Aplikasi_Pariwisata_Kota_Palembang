package com.adityaanugrah.skripsiku.activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.adityaanugrah.skripsiku.model.MainModel
import com.adityaanugrah.skripsiku.retrofit.SharedPref
import com.adityaanugrah.skripsiku.ui.LoginScreen
import com.adityaanugrah.skripsiku.ui.theme.SkripsikuTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.firebase.auth.FirebaseAuth

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SkripsikuTheme {
                val navController = rememberNavController()
                // Ganti dengan implementasi Anda untuk memeriksa status login pengguna

                NavHost(navController, startDestination = "detail") {
                    composable("detail") {
                        DetailScreen(
                            detailState = DetailState(
                                namaWisata = intent.getStringExtra("intent_nnamaWisata"),
                                deskripsi = intent.getStringExtra("intent_ndeskripsi"),
                                alamatWisata = intent.getStringExtra("intent_nalamat"),
                                foto = intent.getStringExtra("intent_ngambar"),
                                koordinat = intent.getStringExtra("koordinat"),
                                fasilitas = intent.getStringExtra("intent_nfasilitas"),
                            ),
                            result = intent.getParcelableExtra<MainModel.Result>("result") as MainModel.Result,
                            isLoggedIn = true,
                            navigateToLogin = { navController.navigate("login") }
                        )
                    }
                }
            }
        }
    }

    private fun checkLoginStatus(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser != null
    }

    // CLASS DATA UNTUK MEMANGGIL DATA YANG DI INTENT UNTUK DI TAMPILKAN DI LAYOUT
    data class DetailState(
        var namaWisata: String?,
        var deskripsi: String?,
        var alamatWisata: String?,
        var foto: String?,
        var koordinat: String?,
        val fasilitas: String?,
    )


    @Composable
    fun DetailScreen(
        detailState: DetailState,
        result: MainModel.Result,
        isLoggedIn: Boolean,
        navigateToLogin: () -> Unit
    ) {
        val scrollState = rememberScrollState()

        val koordinat = detailState.koordinat?.split(",")
        val lat = koordinat?.get(0)
        val long = koordinat?.get(1)

        val singapore = LatLng(lat?.toDouble() ?: 0.0, long?.toDouble() ?: 0.0)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(singapore, 15f)
        }

        val context = LocalContext.current
        val pref = SharedPref(context)

        var hasSave by remember {
            mutableStateOf(false)
        }
        val resultString = Gson().toJson(result)
        if (pref.favorit.contains(resultString)) {
            hasSave = true
        }

        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(text = "Detail Wisata")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        (context as AppCompatActivity).finish()
                    }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = androidx.compose.ui.graphics.Color.White,
                elevation = 10.dp,
                actions = {
                    IconButton(onClick = {
                        if (hasSave) {
                            val data = pref.favorit
                            data.remove(resultString)
                            pref.favorit = data
                        } else {
                            if (isLoggedIn) {
                                val data = pref.favorit
                                data.add(resultString)
                                pref.favorit = data
                            } else {
                                navigateToLogin()
                            }
                        }

                        hasSave = !hasSave
                    }) {
                        Icon(
                            Icons.Filled.Favorite,
                            "backIcon",
                            tint = if (hasSave) Color.Red else Color.White
                        )
                    }
                }
            )
        }) {
            Column(
                Modifier
                    .padding(it)
                    .verticalScroll(scrollState)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    model = detailState.foto ?: "",
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = detailState.namaWisata ?: "", fontWeight = FontWeight.Bold)
                    Text(
                        text = detailState.alamatWisata ?: "",
                        style = MaterialTheme.typography.caption
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Fasilitas",
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = detailState.fasilitas ?: "",
                        style = MaterialTheme.typography.caption
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Divider(Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Deskripsi",
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = detailState.deskripsi ?: "",
                        style = MaterialTheme.typography.caption,
                        textAlign = TextAlign.Justify
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Divider(Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    GoogleMap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        cameraPositionState = cameraPositionState
                    ) {
                        Marker(
                            state = MarkerState(position = singapore),
                            title = "Lokasi",
                            snippet = "Ketuk untuk detail"
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun DefaultPreview() {
        DetailActivity()
    }
}