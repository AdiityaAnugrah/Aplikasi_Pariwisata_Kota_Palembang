package com.adityaanugrah.skripsiku.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.recyclerview.widget.LinearLayoutManager
import com.adityaanugrah.skripsiku.R
import com.adityaanugrah.skripsiku.adapter.MainAdapter
import com.adityaanugrah.skripsiku.model.MainModel
import com.adityaanugrah.skripsiku.retrofit.ApiService
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.android.synthetic.main.activity_main.rvdataList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    private val TAG: String = "MainActivity" // Untuk melihat di LOGCAT
    lateinit var mainAdapter: MainAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        setupRecyclerView()
        getDataFromApi()
    }

    private fun setupRecyclerView(){
        mainAdapter = MainAdapter(arrayListOf(),object : MainAdapter.OnAdapterListener {
            override fun OnClick(result: MainModel.Result) {
                startActivity(
                    Intent(
                        this@MainActivity,
                        DetailActivity::class.java
                    ).putExtra("intent_nnamaWisata", result.namaWisata)
                        .putExtra("koordinat", result.koordinat)
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
        val shimmerLayout = findViewById<ShimmerFrameLayout>(R.id.shimmerLayout)
        shimmerLayout.startShimmer() // Start shimmer animation

        rvdataList.visibility = View.GONE // Hide the actual RecyclerView initially

        rvdataList.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = mainAdapter
        }

        Handler().postDelayed({ // Delay stopping the shimmer animation for demonstration purposes
            shimmerLayout.stopShimmer()
            shimmerLayout.visibility = View.GONE // Hide the shimmer layout
            rvdataList.visibility = View.VISIBLE // Show the actual RecyclerView
        }, 3000) // Stop shimmer after 3 seconds

        rvdataList.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = mainAdapter
        }
    }


    private fun getDataFromApi(){
        ApiService.endpoint.getWisata()
            .enqueue(object : Callback<MainModel>{
                override fun onResponse(
                    call: Call<MainModel>,
                    response: Response<MainModel>
                ) {
                    if (response.isSuccessful){
                        showWisata(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<MainModel>, t: Throwable) {
                    printLog(t.toString())
                }

            })

    }
    //--Untuk melihat data supaya lebih rapi--
    private fun printLog(message: String){
        Log.d(TAG,message)
    }
    private fun showWisata(wisata : MainModel){
        val results = wisata.result
        mainAdapter.setData(results)
        /*for (wisata in results){
            printLog("NamaWisata: ${wisata.namaWisata}")

        }*/
    }
    //--End--
}




