package com.example.moal_worker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.day_calendar.*
import kotlinx.android.synthetic.main.part_time_cardview.view.*

class TimeCardAdapter(
    val timeList:ArrayList<JobTimeForReading>, val listOfDay:ArrayList<DayScheduleModel>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeCardAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return timeList.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ViewHolder
        val data: JobTimeForReading = timeList[position]
        holder.bind(data)




        val day = data.jobDay
        var dayInt = 0
        when (day) {
            "월" -> dayInt = 1
            "화" -> dayInt = 2
            "수" -> dayInt = 3
            "목" -> dayInt = 4
            "금" -> dayInt = 5
            "토" -> dayInt = 6
            "일" -> dayInt = 0
        }
        //i++
        //dayList.add(dayInt)

        val positionName: String = data.positionName
        val partName: String = data.partName
        val startHour = data.startHour
        val startMin :Float = (((data.startMin) / 6)*0.1).toFloat()
        val endHour = data.endHour
        val endMin: Float = (((data.endMin)/ 6)*0.1).toFloat()
        val timeInt: Float = 0.5F
        var start: Float = (startHour + startMin).toFloat()
        val end: Float = endHour + endMin
        val viewnum: Int = 2 * (end - start).toInt()
        var t: Int = 0 //listofDay 인덱스 변수
        while (start < end) {



            t = dayInt + (7 * 2 * start).toInt()
            //dayModel = DayScheduleModel()
            listOfDay[t] = DayScheduleModel(positionName, partName, Color.rgb(240, 0, 0))
            start = (start + timeInt)
        }



        holder.toAdapter(listOfDay)



    }


    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.part_time_cardview, parent, false)) {

        fun bind(data: JobTimeForReading) {
            itemView.cardView_startTime.text =
                data.startHour.toString() + " : " + data.startMin.toString()
            itemView.cardView_endTime.text =
                data.endHour.toString() + " : " + data.endMin.toString()
            itemView.cardView_people.text = data.requirePeopleNum.toString() + " 명"
            itemView.cardView_position.text = data.positionName
            itemView.cardView_partName.text = data.partName
            itemView.cardView_day.text = data.jobDay + "요일"
        }
        val v = inflater.inflate(R.layout.day_calendar, parent, false)
        // val include_cal_view : View = v.findViewById(R.id.cal_view_1)
        val day: RecyclerView = v.findViewById(R.id.day_sche_calendar)

        fun toAdapter(listOfDay: ArrayList<DayScheduleModel>) {
            val dayListAdapter = DayListAdapter()
            day.adapter = dayListAdapter
            dayListAdapter.setDayList(listOfDay)
        }


        }



}