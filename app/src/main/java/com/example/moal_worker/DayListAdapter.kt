package com.example.moal_worker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class DayListAdapter :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listOfDayList = arrayListOf<DayScheduleModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DayListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_day, parent, false))
    }

    override fun getItemCount(): Int =listOfDayList.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val dayViewHolder = viewHolder as DayListViewHolder
        dayViewHolder.bindView(listOfDayList[position])

        var color :Int = listOfDayList[position].color
        dayViewHolder.itemView.setBackgroundColor(color)
        //해당 itemView로 들어온 color를 이용해서 calendar 칸의 배경색을 바꿔준다.
    }
    fun setDayList(listOfDayList: ArrayList<DayScheduleModel>){
        this.listOfDayList = listOfDayList
        notifyDataSetChanged()
        //인자로 들어온 array를 adapter에 새롭게 갱신
    }

    fun showInCalendar(listOfDay:ArrayList<DayScheduleModel>,
    jobTimeForReading : JobTimeForReading,day :String, colors: ArrayList<Int>, i: Int){

        var dayInt = 0
        when (day) {
            "월" -> dayInt = 0
            "화" -> dayInt = 1
            "수" -> dayInt = 2
            "목" -> dayInt = 3
            "금" -> dayInt = 4
            "토" -> dayInt = 5
            "일" -> dayInt = 6
        }
        //snapshot으로 들어온 day 문자열애 따라서 숫자로 변환, 0~6 까지

        val positionName: String = jobTimeForReading.positionName //ex) 서빙, 주방
        val partName: String = jobTimeForReading.partName// ex) 오픈, 마감
        val startHour = jobTimeForReading.startHour //시작 시간
        val startMin =
            (((jobTimeForReading.startMin) / 6) * 0.1).toFloat()
        //시작 분, 이를 소수점화 시킨다.ex) 30분 -> 0.5
        val endHour = jobTimeForReading.endHour //끝 시간
        val endMin =
            (((jobTimeForReading.endMin) / 6) * 0.1).toFloat() //끝 분

        val timeInt: Float = 0.5F //시작부터 끝까지 칸을 채우기 위해 0.5씩 더하는 도구

        var start: Float = (startHour + startMin)//총 시작시간=시+분
        val end: Float = endHour + endMin//총 끝시간
        val st: Float = start//while문의 영향 안받고 시작+끝/2 를 위한 변수
        var t: Int = 0 //listofDay 인덱스 변수
        while (start < end) {

            t = dayInt + (7 * 2 * start).toInt()
            //요일 + (총 시작시간*1시간당 2칸으로 쪼개짐 *같은 요일의 시간이여야하니까 7칸씩 곱함)
            //dayModel = DayScheduleModel()
            val se = st + end
            val ts = start * 2
            if (startMin != endMin) {
                //시작 /끝 분->30분/ 0분 = 홀수개의 칸5r4
                if (ts == (se - 1.5F)) {
                    listOfDay[t] = DayScheduleModel(
                        positionName,
                        null,
                        colors[i]
                    ) //포지션 이름만 표기
                } else if (ts == se - 0.5F) {
                    listOfDay[t] =
                        DayScheduleModel(null, partName, colors[i])
                    //파트 이름만 표기
                } else {
                    listOfDay[t] =
                        DayScheduleModel(null, null, colors[i])
                    //아무것도 x, 색만 채움
                }
            }
            if (startMin == endMin) {
                //0분/ 0분 -> 짝수개의 칸
                if ((start * 2) == (st + end - 1)) {
                    listOfDay[t] = DayScheduleModel(
                        positionName,
                        null,
                        colors[i]
                    )
                } else if ((start * 2) == st + end) {
                    listOfDay[t] =
                        DayScheduleModel(null, partName, colors[i])
                } else {
                    listOfDay[t] =
                        DayScheduleModel(null, null, colors[i])
                }

            }

            start = (start + timeInt)

        }

    }





}