package com.adityaanugrah.skripsiku.ui

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adityaanugrah.skripsiku.R
import com.adityaanugrah.skripsiku.activity.DetailActivity
import com.adityaanugrah.skripsiku.adapter.MainAdapter
import com.adityaanugrah.skripsiku.model.MainModel
import com.adityaanugrah.skripsiku.retrofit.SharedPref
import com.google.gson.Gson

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun FavoritScreen(goToLogin: () -> Unit) {
    val context = LocalContext.current
    val pref = SharedPref(context)

    val mainAdapter = MainAdapter(arrayListOf(), object : MainAdapter.OnAdapterListener {
        override fun OnClick(result: MainModel.Result) {
            context.startActivity(
                Intent(context, DetailActivity::class.java).putExtra("koordinat", result.koordinat)
                    .putExtra("result", result)
                    .putExtra("intent_nnamaWisata", result.namaWisata)
                    .putExtra("intent_ndeskripsi", result.deskripsi)
                    .putExtra("intent_nalamat", result.alamat)
                    .putExtra("intent_nfasilitas", result.fasilitas)
                    .putExtra("intent_nkategori", result.kategori)
                    .putExtra("intent_njamBuka", result.jamBuka)
                    .putExtra("intent_njamTutup", result.jamTutup)
                    .putExtra("intent_ngambar", result.gambar)
            )
        }

    })

    OnLifecycleEvent { _, event ->
        // do stuff on event
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                Log.e("t", "titi")
                val data = pref.favorit.toList()
                    .map {
                        Gson().fromJson(it, MainModel.Result::class.java)
                    }
                Log.e("ta", data.toString())
                mainAdapter.setData(data)
            }

            else -> {}
        }
    }

    Scaffold(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (pref.isLogin) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), // Occupy the max size in the Compose UI tree
                    factory = { context ->
                        RecyclerView(context).apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = mainAdapter
                        }
                    },
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.pp),
                    contentDescription = stringResource(id = R.string.Katakatalogin),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Text(text = "Silahkan login terlebih dahulu", style = MaterialTheme.typography.body1)
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
    FavoritScreen {

    }
}