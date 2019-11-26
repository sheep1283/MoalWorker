package com.example.moal_worker

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_working_schedule.*
import kotlinx.android.synthetic.main.day_calendar.day_sche_calendar
import kotlinx.android.synthetic.main.day_calendar.time_sche_calendar
import kotlin.collections.ArrayList


class WorkingScheduleActivity : AppCompatActivity() {


    val requestclicked  = 1
    var selectedstore = ""
    var rootRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    val dirFire: DatabaseReference = rootRef
    val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_working_schedule)


        initView()
        val listOfDay = ArrayList<DayScheduleModel>(generateDummyData())
        var timeList = arrayListOf<JobTimeForReading>()
        var jobTimes = arrayListOf<JobTimeForReading>()
        val timecardAdapter = TimeCardAdapter(timeList)
        var storeList = arrayListOf<JobInfoForReading>()

        val postListener = object : ValueEventListener {

            val dayListAdapter = DayListAdapter()


            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (intent.hasExtra("clickedstore")) {
                    selectedstore = intent.getStringExtra("clickedstore")
                } else {
                    selectedstore = "노랑통닭 홍대점"
                }

                storeList.clear()

                for (snapShotStore: DataSnapshot in p0.children) {
                    val storename = snapShotStore.key
                    if (storename == null) {

                    } else {
                        val jobInfoForReading = JobInfoForReading(storename)
                        storeList.add(jobInfoForReading)
                    }
                }
                store_list.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = StoreCardAdapter(storeList)
                }
                timeList.clear()
                val name = selectedstore
                for (snapShotDays: DataSnapshot in p0.child(selectedstore).child("WorkingPart").children) { //요일 // 위의 intent에서  null처리 했기때문에 selectedstore는 non-null
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
                                    name,
                                    position,
                                    part,
                                    day

                                )
                                timeList.add(jobTimeForReading)
                            }
                        }
                    }
                }
                var i: Int = 0
                //이미 request된 스케줄 읽어오기. 일단 노랑통닭 홍대점만 읽지만, 등록된 스토어 전부 읽어야 함
                for (snapShotDays: DataSnapshot in p0.child(selectedstore).child("WorkingPart").children) { //요일 // 위의 intent에서  null처리 했기때문에 selectedstore는 non-null
                    for (snapShotWorkingParts: DataSnapshot in snapShotDays.children) { //서빙
                        for (snapShotTime: DataSnapshot in snapShotWorkingParts.children) {
                            if (snapShotTime.child("RequestList").child("Jini").getValue() == "Request") {
                                for (jobTimeForReading in timeList) {
                                    val jobTimeInfo: JobTimeInfo? =
                                        snapShotTime.getValue(JobTimeInfo::class.java)
                                    val day = snapShotDays.key
                                    val position = snapShotWorkingParts.key
                                    val part = snapShotTime.key

                                    if (jobTimeInfo == null || day == null || position == null || part == null) {

                                    }
                                    else {
                                        val jobTimeForReading = JobTimeForReading(
                                            jobTimeInfo.startHour,
                                            jobTimeInfo.startMin,
                                            jobTimeInfo.endHour,
                                            jobTimeInfo.endMin,
                                            jobTimeInfo.requirePeopleNum,
                                            name,
                                            position,
                                            part,
                                            day
                                        )
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
                                        val positionName: String = jobTimeForReading.positionName
                                        val partName: String = jobTimeForReading.partName
                                        val startHour = jobTimeForReading.startHour
                                        val startMin =
                                            (((jobTimeForReading.startMin) / 6) * 0.1).toFloat()
                                        val endHour = jobTimeForReading.endHour
                                        val endMin = (((jobTimeForReading.endMin) / 6) * 0.1).toFloat()
                                        val timeInt: Float = 0.5F
                                        var start: Float = (startHour + startMin).toFloat()
                                        var st: Float = start
                                        val end: Float = endHour + endMin
                                        val viewnum: Int = 2 * (end - start).toInt()
                                        var t: Int = 0 //listofDay 인덱스 변수
                                        while (start < end) {
                                            t = dayInt + (7 * 2 * start).toInt()
                                            //dayModel = DayScheduleModel()
                                            listOfDay[t] = DayScheduleModel(positionName, partName, Color.rgb(240, 0, 0))
                                            start = (start + timeInt)
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                request_button.setOnClickListener {
                    for (snapShotDays: DataSnapshot in p0.child(selectedstore).child("WorkingPart").children) { //요일 // 위의 intent에서  null처리 했기때문에 selectedstore는 non-null
                        for (snapShotWorkingParts: DataSnapshot in snapShotDays.children) { //서빙
                            for (snapShotTime: DataSnapshot in snapShotWorkingParts.children) {
                                if (snapShotTime.child("RequestList").child("Jini").getValue() == "Checked") {
                                    database
                                        .child(selectedstore)
                                        .child("WorkingPart")
                                        .child(snapShotDays.key.toString())
                                        .child(snapShotWorkingParts.key.toString())
                                        .child(snapShotTime.key.toString())
                                        .child("RequestList")
                                        .child("Jini")
                                        .setValue("Request")
                                    //체크된 스케줄 버튼 클릭시 request

                                }
                                if (snapShotTime.child("RequestList").child("Jini").getValue() == "Request") {
                                    for (jobTimeForReading in timeList) {
                                        val jobTimeInfo: JobTimeInfo? =
                                            snapShotTime.getValue(JobTimeInfo::class.java)
                                        val day = snapShotDays.key
                                        val position = snapShotWorkingParts.key
                                        val part = snapShotTime.key

                                        if (jobTimeInfo == null || day == null || position == null || part == null) {

                                        } else {
                                            val jobTimeForReading = JobTimeForReading(
                                                jobTimeInfo.startHour,
                                                jobTimeInfo.startMin,
                                                jobTimeInfo.endHour,
                                                jobTimeInfo.endMin,
                                                jobTimeInfo.requirePeopleNum,
                                                name,
                                                position,
                                                part,
                                                day
                                            )
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
                                            val positionName: String =
                                                jobTimeForReading.positionName
                                            val partName: String = jobTimeForReading.partName
                                            val startHour = jobTimeForReading.startHour
                                            val startMin =
                                                (((jobTimeForReading.startMin) / 6) * 0.1).toFloat()
                                            val endHour = jobTimeForReading.endHour
                                            val endMin =
                                                (((jobTimeForReading.endMin) / 6) * 0.1).toFloat()
                                            val timeInt: Float = 0.5F
                                            var start: Float = (startHour + startMin).toFloat()
                                            var st: Float = start
                                            val end: Float = endHour + endMin
                                            val viewnum: Int = 2 * (end - start).toInt()
                                            var t: Int = 0 //listofDay 인덱스 변수
                                            while (start < end) {
                                                t = dayInt + (7 * 2 * start).toInt()
                                                //dayModel = DayScheduleModel()
                                                listOfDay[t] = DayScheduleModel(positionName, partName, Color.rgb(240, 0, 0))
                                                start = (start + timeInt)
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                day_sche_calendar.apply {
                    val dayListAdapter = DayListAdapter()
                    day_sche_calendar.adapter = dayListAdapter
                    dayListAdapter.setDayList(listOfDay)
                }
                time_list.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = TimeCardAdapter(timeList)
                    // jobTimes =timecardAdapter.copy()

                }


            }
        }
        dirFire.addValueEventListener(postListener)


    }



    fun initView() {


        time_sche_calendar.layoutManager = GridLayoutManager(this, 1)
        time_sche_calendar.addItemDecoration(GridItemDecoration(0, 2))
        /*time_sche_calendar.addItemDecoration(
            DividerItemDecoration(
                activity,
                LinearLayoutManager.HORIZONTAL
            )
        )*/
        /*time_sche_calendar.addItemDecoration(
                DividerItemDecoration(
                    activity,
                    LinearLayoutManager.VERTICAL
                )
            )*/

        day_sche_calendar.layoutManager = GridLayoutManager(this, 7)
        day_sche_calendar.addItemDecoration(GridItemDecoration(0, 2))
        /*day_sche_calendar.addItemDecoration(
            DividerItemDecoration(
            activity,
            LinearLayoutManager.HORIZONTAL
        ))*/

        /*day_sche_calendar.addItemDecoration(
            DividerItemDecoration(
                activity,
                LinearLayoutManager.VERTICAL
            )
        )*/



        val timeListAdapter = TimeIntervalAdapter()
        time_sche_calendar.adapter = timeListAdapter
        timeListAdapter.setTimeList(timeSettingData())

        val dayListAdapter = DayListAdapter()
        day_sche_calendar.adapter = dayListAdapter
        dayListAdapter.setDayList(generateDummyData())

        val daycal: RecyclerView = findViewById(R.id.day_sche_calendar)
        val timecal: RecyclerView = findViewById(R.id.time_sche_calendar)
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
            timeModel = TimeIntervalModel(i)
            listOfTime.add(timeModel)
            i++
        }
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

}

//extension or member ?