package com.adityaanugrah.skripsiku.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adityaanugrah.skripsiku.activity.DetailActivity
import com.adityaanugrah.skripsiku.adapter.MainAdapter
import com.adityaanugrah.skripsiku.model.MainModel
import com.adityaanugrah.skripsiku.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun PencarianScreen(context: Context = LocalContext.current) {
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

    var resultData by remember {
        mutableStateOf(arrayListOf<MainModel.Result>())
    }

    LaunchedEffect(true) {
        ApiService.endpoint.getWisata().enqueue(object : Callback<MainModel> {
            override fun onResponse(
                call: Call<MainModel>, response: Response<MainModel>
            ) {
                if (response.isSuccessful) {
                    val results = response.body()!!.result
                    resultData = results
                    mainAdapter.setData(resultData)
                }
            }

            override fun onFailure(call: Call<MainModel>, t: Throwable) {

            }

        })
    }

    var searchValue by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            OutlinedTextField(
                value = searchValue,
                onValueChange = { value ->
                    searchValue = value

                    mainAdapter.setData(resultData.filter {
                        it.namaWisata.lowercase().contains(searchValue.lowercase())
                    })
                },
                label = {
                    Text(text = "Ketik nama wisata disini...")
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RectangleShape)
                    .background(Color.White)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
            )
        }
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
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
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    PencarianScreen()
}