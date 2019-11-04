package com.example.moal_worker

import android.view.View
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_day.view.*
import com.example.moal_worker.DayScheduleModel
import kotlinx.android.synthetic.main.item_time_interval.view.*

//하루 일정과 시간interval 모두 viewHolder 함수를 만들어주는곳
class DayListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(dayModel : DayScheduleModel){
        itemView.gridName.text=dayModel.Name
        itemView.gridfill.text=dayModel.fill
    }

}