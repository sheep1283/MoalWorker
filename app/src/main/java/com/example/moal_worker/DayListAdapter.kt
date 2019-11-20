package com.example.moal_worker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.day_calendar.*
import kotlinx.android.synthetic.main.item_day.*

class DayListAdapter :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listOfDayList = arrayListOf<DayScheduleModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DayListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_day, parent, false))
    }

    override fun getItemCount(): Int =listOfDayList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val dayViewHolder = viewHolder as DayListViewHolder
        dayViewHolder.bindView(listOfDayList[position])
        dayViewHolder.itemView.setBackgroundColor(listOfDayList[position].color)
       // notifyDataSetChanged()
    }
    fun setDayList(listOfDayList: ArrayList<DayScheduleModel>){
        this.listOfDayList = listOfDayList
        notifyDataSetChanged()
    }//??????????????????????????????????????

   /* fun judgeTime(jobTimes:ArrayList<JobTimeForReading>, listOfDay : ArrayList<DayScheduleModel>) {


        var jobTimeForReading: JobTimeForReading
        var timeIntList: ArrayList<Int> = arrayListOf()
        var i =0 //day 인덱스 변수
        for (jobTimeForReading in jobTimes) {
            val day = jobTimeForReading.jobDay
            val dayList = day.split("  ")
            var dayInt = 0
            for (day in dayList) { // time 화)10:00∼13:00
                when (day[i].toString()) {
                    "월" -> dayInt = 1
                    "화" -> dayInt = 2
                    "수" -> dayInt = 3
                    "목" -> dayInt = 4
                    "금" -> dayInt = 5
                    "토" -> dayInt = 6
                    "일" -> dayInt = 0
                }
                i++
            }



            val startHour = jobTimeForReading.startHour
            val startMin= (jobTimeForReading.startMin)*(1/60)
            val endHour=jobTimeForReading.endHour
            val endMin = (jobTimeForReading.endMin)*(1/60)
            val timeInt :Double=0.5
            var start :Double  = (startHour + startMin).toDouble()
            val end :Int = endHour+ endMin
            val viewnum:Int = 2*(end - start).toInt()
            var t :Int=0 //listofDay 인덱스 변수
            while(start < end){

              /*  val plusPlan : ViewGroup.LayoutParams = item_day_constraint.getLayoutParams()
                plusPlan.width = ViewGroup.LayoutParams.MATCH_PARENT
                plusPlan.height =viewnum*27
                item_day_constraint.setLayoutParams(plusPlan)*/

                t= dayInt+ (7*2*start).toInt()

                start = (start+ timeInt)

            }



        }
    }*/


}