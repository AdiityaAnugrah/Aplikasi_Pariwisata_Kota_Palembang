package com.adityaanugrah.skripsiku.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
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
fun BerandaScreen(context: Context = LocalContext.current) {
    val mainAdapter =
        MainAdapter(arrayListOf(), object : MainAdapter.OnAdapterListener {
            override fun OnClick(result: MainModel.Result) {
                context.startActivity(
                    Intent(context, DetailActivity::class.java)
                        .putExtra("result", result)
                        .putExtra("koordinat", result.koordinat)
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

    LaunchedEffect(true) {
        ApiService.endpoint.getWisata()
            .enqueue(object : Callback<MainModel> {
                override fun onResponse(
                    call: Call<MainModel>,
                    response: Response<MainModel>
                ) {
                    if (response.isSuccessful) {
                        val results = response.body()!!.result
                        mainAdapter.setData(results)
                    }
                }

                override fun onFailure(call: Call<MainModel>, t: Throwable) {

                }

            })
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
        factory = { context ->
            RecyclerView(context).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mainAdapter
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    BerandaScreen()
}