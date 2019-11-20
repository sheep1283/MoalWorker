package com.example.moal_worker


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_working_schedule.*
import kotlinx.android.synthetic.main.day_calendar.day_sche_calendar
import kotlinx.android.synthetic.main.day_calendar.time_sche_calendar
import kotlinx.android.synthetic.main.item_day.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    var timeList = arrayListOf<JobTimeForReading>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        // val include_cal_view : View = v.findViewById(R.id.cal_view_1)
        val b1: ImageButton = v.run { findViewById(R.id.button_workingschedule) }
        b1.setOnClickListener {
            val intent = Intent(activity, WorkingScheduleActivity::class.java)
            startActivity(intent)
        }
        return v
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)
        var rootRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()
        val dirFire: DatabaseReference = rootRef.child("노랑통닭 홍대점")
        val listOfDay = ArrayList<DayScheduleModel>(generateDummyData())


        initView(v)


        dirFire.child("WorkingPart").addValueEventListener(object : ValueEventListener {

            val dayListAdapter = DayListAdapter()
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (snapShotDays: DataSnapshot in p0.children) { //요일
                    for (snapShotWorkingParts: DataSnapshot in snapShotDays.children) { //서빙
                        for (snapShotTime: DataSnapshot in snapShotWorkingParts.children) { //오미마
                            val day = snapShotDays.key
                            val position = snapShotWorkingParts.key
                            val part = snapShotTime.key
                            val jobTimeInfo: JobTimeInfo? =
                                snapShotTime.getValue(JobTimeInfo::class.java)

                            if (jobTimeInfo == null || day == null || position == null || part == null) {

                            } else {

                                val jobTimeForReading = JobTimeForReading(
                                    jobTimeInfo.startHour,
                                    jobTimeInfo.startMin,
                                    jobTimeInfo.endHour,
                                    jobTimeInfo.endMin,
                                    jobTimeInfo.requirePeopleNum,
                                    position,
                                    part,
                                    day
                                )
                                timeList.add(jobTimeForReading)
                                //jobTimes.add(jobTimeForReading)
                            }
                        }

                    }

                }
               /* //time_list.run {
                    time_list.layoutManager = LinearLayoutManager(context)
                    time_list.adapter = TimeCardAdapter(timeList, listOfDay)
                    day_sche_calendar.apply {
                    day_sche_calendar.adapter = dayListAdapter
                    dayListAdapter.setDayList(listOfDay)
              //  }
                    //이 코드 필요!
                }*/


            }
        })






       /* var jobTimes = timeList
       // var timeIntList: ArrayList<Int> = arrayListOf()
        var i = 0 //day 인덱스 변수
        for (jobTimeForReading in jobTimes) {
            //jobtimeforreading 객체들이 들어있는 jobtimes에서 하나씩 읽기
            val dayListAdapter = DayListAdapter()
            day_sche_calendar.adapter = dayListAdapter
            //dayListAdapter.setDayList(generateDummyData())
            val listOfDay = ArrayList<DayScheduleModel>(generateDummyData())
           // var dayModel: DayScheduleModel

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
            val startMin = (jobTimeForReading.startMin) * (1 / 60)
            val endHour = jobTimeForReading.endHour
            val endMin = (jobTimeForReading.endMin) * (1 / 60)
            val timeInt: Float= 0.5F
            var start: Float= (startHour + startMin).toFloat()
            val end: Int = endHour + endMin
            val viewnum: Int = 2 * (end - start).toInt()
            var t: Int = 0 //listofDay 인덱스 변수
            while (start < end) {

                /*  val plusPlan : ViewGroup.LayoutParams = item_day_constraint.getLayoutParams()
                  plusPlan.width = ViewGroup.LayoutParams.MATCH_PARENT
                  plusPlan.height =viewnum*27
                  item_day_constraint.setLayoutParams(plusPlan)*/

                t = dayInt + (7 * 2 * start).toInt()
                //dayModel = DayScheduleModel()
                listOfDay[t] = DayScheduleModel(null, null, Color.rgb(129, 209, 191))
                start = (start + timeInt)

            }
            dayListAdapter.setDayList(listOfDay)
        }
        timeList.clear()*/

        }



    private fun initView(v: View) {


        time_sche_calendar.layoutManager = GridLayoutManager(activity, 1)
        time_sche_calendar.addItemDecoration(GridItemDecoration(0, 2))
        time_sche_calendar.addItemDecoration(
            DividerItemDecoration(
                activity,
                LinearLayoutManager.HORIZONTAL
            )
        )
        time_sche_calendar.addItemDecoration(
                DividerItemDecoration(
                    activity,
                    LinearLayoutManager.VERTICAL
                )
            )


        day_sche_calendar.layoutManager = GridLayoutManager(activity, 7)
        //RecyclerView가 고정된 사이즈로 7개 항목을 한 줄에 나타나게 한다.

        //This will for default android divider
        day_sche_calendar.addItemDecoration(GridItemDecoration(0, 2))
        day_sche_calendar.addItemDecoration(
            DividerItemDecoration(
                activity,
                LinearLayoutManager.HORIZONTAL
            )
        )
        day_sche_calendar.addItemDecoration(
            DividerItemDecoration(
                activity,
                LinearLayoutManager.VERTICAL
            )
        )


        val timeListAdapter = TimeIntervalAdapter()
        time_sche_calendar.adapter = timeListAdapter
        timeListAdapter.setTimeList(timeSettingData())

        val dayListAdapter = DayListAdapter()
        day_sche_calendar.adapter = dayListAdapter
        dayListAdapter.setDayList(generateDummyData())

        val daycal: RecyclerView = v.findViewById(R.id.day_sche_calendar)
        val timecal: RecyclerView = v.findViewById(R.id.time_sche_calendar)
        val scrollListener1: RecyclerView.OnScrollListener
        lateinit var scrollListener2: RecyclerView.OnScrollListener
        scrollListener1 = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                daycal.removeOnScrollListener(scrollListener2)
                daycal.scrollBy(dx, dy)
                daycal.addOnScrollListener(scrollListener2)
            }
        }
        scrollListener2 = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                timecal.removeOnScrollListener(scrollListener1)
                timecal.scrollBy(dx, dy)
                timecal.addOnScrollListener(scrollListener1)

            }
        }
        timecal.addOnScrollListener(scrollListener1)
        daycal.addOnScrollListener(scrollListener2)


    }

    private fun timeSettingData(): ArrayList<TimeIntervalModel> {
        var i: Int = 0
        val listOfTime = ArrayList<TimeIntervalModel>()
        var timeModel: TimeIntervalModel

        while (i < 24) {
            timeModel = TimeIntervalModel(i, "시")
            listOfTime.add(timeModel)
            i++
        }//0시~ 24시
        /* i = 0
        while (i < 12) {
            timeModel = TimeIntervalModel(i, "pm")
            listOfTime.add(timeModel)
            i++
        }*/
        return listOfTime
    }

    private fun generateDummyData(): ArrayList<DayScheduleModel> {
        val listOfDay = ArrayList<DayScheduleModel>()
        var i: Int = 0

//        val listOfTime =ArrayList<TimeIntervalModel>()
//        var timeModel : TimeIntervalModel
        var dayModel: DayScheduleModel

        while (i < 7 * 48) {
            dayModel = DayScheduleModel()
            listOfDay.add(dayModel)
            i++
        }


        return listOfDay
    }

/*
    fun judgeTime(jobTimes:ArrayList<JobTimeForReading>, listOfDay : ArrayList<DayScheduleModel>) {


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



        }*/
}