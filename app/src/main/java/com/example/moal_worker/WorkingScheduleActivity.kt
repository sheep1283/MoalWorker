package com.example.moal_worker

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_working_schedule.*
import kotlinx.android.synthetic.main.day_calendar.day_sche_calendar
import kotlinx.android.synthetic.main.day_calendar.time_sche_calendar
import kotlin.collections.ArrayList


class WorkingScheduleActivity : AppCompatActivity() {

    var selectedstore = ""
    val dirFire: DatabaseReference = FirebaseDatabase.getInstance().getReference() //DB 읽어오기
    val database = FirebaseDatabase.getInstance().reference
    val user = FirebaseAuth.getInstance().currentUser //DB 현재 유저 정보 읽어오기(로그인 한 계정)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_working_schedule)
        val colors = ArrayList<Int>()

        colors.add(Color.rgb(250, 190, 190))//c1
        colors.add(Color.rgb(248, 237, 170))//c2
        colors.add(Color.rgb(162, 194, 106))//c3
        colors.add(Color.rgb(166, 235, 142))//c4
        colors.add(Color.rgb(125, 211, 240))//하늜색
        //colors.add(Color.rgb(99, 135, 245))
        colors.add(Color.rgb(173, 211, 255))//c5
        colors.add(Color.rgb(106, 196, 185))//c9
        colors.add(Color.rgb(215, 190, 252))//c7
        colors.add(Color.rgb(211, 181, 228))//c8
        colors.add(Color.rgb(172, 165, 165))//c9


        initView()

        val listOfDay = ArrayList<DayScheduleModel>(generateDummyData())
        var storeList = arrayListOf<JobInfoForReading>() //DB에서 가게 목록 읽어서 StoreCardView로 보냄
        var timeList = arrayListOf<JobTimeForReading>()

        val postListener = object : ValueEventListener {

            var i: Int = 0

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (intent.hasExtra("clickedstore")) { //사용자가 누른 값이 있으면
                    selectedstore = intent.getStringExtra("clickedstore")
                } else { //시작시 자동으로 읽어올 가게
                    for (snapShotStores: DataSnapshot in p0.child("users").child("workers").child(user!!.uid).child("RegisteredStore").children) { //user개인정보에 다른 프래그먼트에서 이미 등록한 RegisteredStore
                        if (snapShotStores.key == null){ //DB에 등록된 가게가 없으면 아무것도 읽어오지 않음.

                        }
                        else{
                            selectedstore = snapShotStores.key.toString() //DB에서 아무 가게나 일단 읽어오
                        }

                    }
                }
                fun writeDatabase(){ //내가 신청한 요일, 파트 등의 정보를 읽어와 캘린더에 띄워주는 함수. datasnapshot을 이용하기 때문에 onCreate 바깥이 아닌 onDataChange 내부에 선언했음
                    for (snapShotDays: DataSnapshot in p0.child("stores").child(selectedstore).child("WorkingPart").children) { //요일 // intent에서  null처리 했기때문에 selectedstore는 non-null
                        for (snapShotWorkingParts: DataSnapshot in snapShotDays.children) { //서빙, 주방 등등
                            for (snapShotTime: DataSnapshot in snapShotWorkingParts.children) { //오픈, 미들, 마감 등등
                                if (snapShotTime.child("RequestList").child(user!!.displayName.toString()).getValue() == "Request") { //로그인을 해야 이 액티비티로 이동이 가능하므로 user는 null아님
                                    for (jobTimeForReading in timeList) { //timeList에 저장 된 값들 읽기. 값의 형식은 JobTimeForReading
                                        val jobTimeInfo: JobTimeInfo? =
                                            snapShotTime.getValue(JobTimeInfo::class.java)
                                        val day = snapShotDays.key
                                        val position = snapShotWorkingParts.key
                                        val part = snapShotTime.key

                                        if (jobTimeInfo == null || day == null || position == null || part == null) { //점주 앱에서 네개의 값이 null이 되면 받지 않게 처리됨

                                        }
                                        else {
                                            val jobTimeForReading = JobTimeForReading(
                                                jobTimeInfo.startHour,
                                                jobTimeInfo.startMin,
                                                jobTimeInfo.endHour,
                                                jobTimeInfo.endMin,
                                                jobTimeInfo.requirePeopleNum,
                                                selectedstore,
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
                                                var se = st + end
                                                var ts = start * 2
                                                if (startMin != endMin) {
                                                    if (ts == (se - 1.5F)) {
                                                        listOfDay[t] = DayScheduleModel(positionName, null, colors[i])
                                                    } else if (ts == se - 0.5F) {
                                                        listOfDay[t] = DayScheduleModel(null, partName, colors[i])
                                                    } else {
                                                        listOfDay[t] = DayScheduleModel(null, null, colors[i])
                                                    }
                                                }
                                                if (startMin == endMin) {
                                                    if ((start * 2) == (st + end - 1)) {
                                                        listOfDay[t] = DayScheduleModel(positionName, null, colors[i])
                                                    } else if ((start * 2) == st + end) {
                                                        listOfDay[t] = DayScheduleModel(null, partName, colors[i])
                                                    } else {
                                                        listOfDay[t] = DayScheduleModel(null, null, colors[i])
                                                    }

                                                }

                                                start = (start + timeInt)
                                            }

                                            if (i == 9) {
                                                i = 0
                                            } else {
                                                i++
                                            }


                                        }
                                    }
                                }
                            }
                        }
                    }
                }


                storeList.clear() //직전 실행 값이 남아있을 수 있으므로 초기화

                for (snapShotStore: DataSnapshot in p0.child("users").child("workers").child(user!!.uid).child("RegisteredStore").children) { //user개인정보에 다른 프래그먼트에서 이미 등록한 RegisteredStore
                    val storename = snapShotStore.key
                    if (storename == null) {

                    } else {
                        val jobInfoForReading = JobInfoForReading(storename)
                        storeList.add(jobInfoForReading)
                    }
                }
                store_list.apply {//cardView로 띄워지게 해당 adapter에 읽은 값 전달
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = StoreCardAdapter(storeList)
                }

                timeList.clear() //직전 실행 값이 남아있을 수 있으므로 초기화

                val name = selectedstore
                for (snapShotDays: DataSnapshot in p0.child("stores").child(selectedstore).child("WorkingPart").children) { //요일 // 위의 intent에서  null처리 했기때문에 selectedstore는 non-null
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
                writeDatabase() //이미 request된 스케줄 읽어오기.

                request_button.setOnClickListener {
                    for (snapShotDays: DataSnapshot in p0.child("stores").child(selectedstore).child("WorkingPart").children) { //요일 // 위의 intent에서  null처리 했기때문에 selectedstore는 non-null
                        for (snapShotWorkingParts: DataSnapshot in snapShotDays.children) { //서빙
                            for (snapShotTime: DataSnapshot in snapShotWorkingParts.children) {
                                if (snapShotTime.child("RequestList").child(user!!.displayName.toString()).getValue() == "Checked") {//로그인을 해야 이 액티비티로 이동이 가능하므로 user는 null아님
                                    database
                                        .child("stores")
                                        .child(selectedstore)
                                        .child("WorkingPart")
                                        .child(snapShotDays.key.toString())
                                        .child(snapShotWorkingParts.key.toString())
                                        .child(snapShotTime.key.toString())
                                        .child("RequestList")
                                        .child(user!!.displayName.toString())
                                        .setValue("Request")
                                    //체크된 스케줄 버튼 클릭시 request

                                }
                            }
                        }
                    }
                    writeDatabase()
                }
                day_sche_calendar.apply {
                    val dayListAdapter = DayListAdapter()
                    day_sche_calendar.adapter = dayListAdapter
                    dayListAdapter.setDayList(listOfDay)
                }
                time_list.apply { //cardView로 띄워지게 해당 adapter에 읽은 값 전달
                    layoutManager = LinearLayoutManager(context)
                    adapter = TimeCardAdapter(timeList)


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