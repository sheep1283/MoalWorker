package com.example.moal_worker

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.store_list_cardview.view.*
import kotlin.coroutines.coroutineContext


class StoreCardAdapter(val storeList:ArrayList<JobInfoForReading>): RecyclerView.Adapter<StoreCardAdapter.ViewHolder>(){





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreCardAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return storeList.size
    }

    override fun onBindViewHolder(holder: StoreCardAdapter.ViewHolder, position: Int) {
        val data: JobInfoForReading = storeList[position]
        holder.bind(data)
    }




    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.store_list_cardview, parent, false)) {

        fun bind(data: JobInfoForReading) {

            itemView.cardview_storename.text = data.storeName //액티비티에서 받아온거 하나씩 뷰로 띄워줌

            itemView.StoreLayout.setOnClickListener { //해당 가게 클릭되면
                val intent : Intent = Intent(itemView.context, WorkingScheduleActivity::class.java)
                intent.putExtra("clickedstore", itemView.cardview_storename.text.toString()) //clickedstore를 통해 클릭한 가게 이름 전달
                (itemView.context as WorkingScheduleActivity).finish()
                itemView.context.startActivity(intent)
            }


        }

    }

}