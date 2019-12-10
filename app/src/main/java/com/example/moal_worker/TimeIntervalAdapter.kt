package com.example.moal_worker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TimeIntervalAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //시간(0시~23시)을 담는 day_calendar의 time_sche_cal xml을 연결해주는 어댑터
    private var listOfTime = arrayListOf<TimeIntervalModel>() //시간 내용들을 담고있는 arraylist

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TimeListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_time_interval, parent, false))
    }

    override fun getItemCount(): Int =listOfTime.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val timeViewHolder = viewHolder as TimeListViewHolder
        timeViewHolder.bindTimeView(listOfTime[position])
    }
    fun setTimeList(listOfTime: ArrayList<TimeIntervalModel>){
        this.listOfTime = listOfTime
        notifyDataSetChanged()
    }


}