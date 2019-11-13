package com.example.moal_worker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.part_time_cardview.view.*

class TimeCardAdapter(val timeList:ArrayList<JobTimeForReading>): RecyclerView.Adapter<TimeCardAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeCardAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return timeList.size
    }

    override fun onBindViewHolder(holder: TimeCardAdapter.ViewHolder, position: Int) {
        val data: JobTimeForReading = timeList[position]
        holder.bind(data)
    }

    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.part_time_cardview, parent, false)) {
        fun bind(data: JobTimeForReading) {
            itemView.cardView_startTime.text =  data.startHour.toString()+" : "+data.startMin.toString()
            itemView.cardView_endTime.text = data.endHour.toString()+" : "+data.endMin.toString()
            itemView.cardView_people.text = data.requirePeopleNum.toString()+" 명"
            itemView.cardView_position.text = data.positionName
            itemView.cardView_partName.text = data.partName
            itemView.cardView_day.text = data.jobDay+"요일"
        }
}
}