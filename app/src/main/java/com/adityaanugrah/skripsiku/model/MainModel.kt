package com.adityaanugrah.skripsiku.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class MainModel(
    val result: ArrayList<Result>
) {
    @Parcelize
    data class Result(
        val alamat: String,
        val created_at: String,
        val deskripsi: String,
        val fasilitas: String,
        val gambar: String,
        val id: Int,
        val jamBuka: Float,
        val jamTutup: Float,
        val kategori: String,
        val namaWisata: String,
        val updated_at: String,
        val koordinat: String,
    ) : Parcelable

}