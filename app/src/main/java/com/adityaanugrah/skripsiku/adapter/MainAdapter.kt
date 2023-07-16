package com.adityaanugrah.skripsiku.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adityaanugrah.skripsiku.R
import com.adityaanugrah.skripsiku.model.MainModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.row_item.view.nalamat
import kotlinx.android.synthetic.main.row_item.view.ngambar
import kotlinx.android.synthetic.main.row_item.view.nnamaWisata

class MainAdapter(
    val results : ArrayList<MainModel.Result>,
    val listener: OnAdapterListener
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.row_item,parent,false)
    )

    override fun getItemCount() = results.size

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.view.nnamaWisata.text = result.namaWisata
        holder.view.nalamat.text = result.alamat

        Log.d("MainAdapter","resultImage:${result.gambar}")
        Glide.with(holder.view)      //--UNTUK MENAMPILAKAN GAMBAR MENGGUNAKAN GLIDE--
            .load(result.gambar)
            .placeholder(R.raw.loading)
            .error(R.raw.loading)
            .centerCrop()
            .into(holder.view.ngambar) ///--END--


        holder.view.setOnClickListener{
            listener.OnClick(result)
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    fun setData(data: List<MainModel.Result>){
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{
        fun OnClick( result: MainModel.Result)
    }

}