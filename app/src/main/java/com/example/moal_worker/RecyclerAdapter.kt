package com.example.moal_worker

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter() : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    var data = listOf<String>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}