package com.example.moal_worker

import android.graphics.Color
import android.view.View
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_day.view.*
import com.example.moal_worker.DayScheduleModel
import kotlinx.android.synthetic.main.item_time_interval.view.*
import kotlinx.android.synthetic.main.part_time_cardview.view.*


class DayListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(dayModel : DayScheduleModel) {
        //if(dayModel.Name != null && dayModel.fill ==null) {
        // }

        if (dayModel.fill != null ) {//내용(part이름, 오픈 미들 마감)이 들어올때
            itemView.gridName.text = dayModel.fill
            //xml의 원하는 공간에 해당 내용을 넣는다
            itemView.gridName.setTextColor(Color.rgb(117, 117, 117))
            itemView.gridName.setTextSize(13F)
        }
        else{
            itemView.gridName.text = dayModel.Name
            itemView.gridName.setTextColor(Color.BLACK)
            itemView.gridName.setTextSize(13F)
        }//제목(position이름, 서빙 주방)이 들어올때
        //itemView.gridName.text
        //itemView.gridfill.text=dayModel.fill
        itemView.setBackgroundColor(dayModel.color)

    }

}