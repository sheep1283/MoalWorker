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


    val requestclicked  = 1
    var selectedstore = ""
    var dirFire: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    val database = FirebaseDatabase.getInstance().reference
    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_working_schedule)
        val colors = ArrayList<Int>()
        val dayListAdapter = DayListAdapter()
        day_sche_calendar.adapter = dayListAdapter

        colors.add(Color.rgb(250, 190, 190))//c1
        colors.add(Color.rgb(248, 237, 170))//c2
        colors.add(Color.rgb(162, 194, 106))//c3
        colors.add(Color.rgb(166, 235, 142))//c4
        colors.add(Color.rgb(125, 211, 240))//하늜색
        colors.add(Color.rgb(173, 211, 255))//c5
        colors.add(Color.rgb(106, 196, 185))//c9
        colors.add(Color.rgb(215, 190, 252))//c7
        colors.add(Color.rgb(211, 181, 228))//c8
        colors.add(Color.rgb(172, 165, 165))//c9


        initView()//빈 calendar xml
        val listOfDay = ArrayList<DayScheduleModel>(generateDummyData())
        var timeList = arrayListOf<JobTimeForReading>()
        var storeList = arrayListOf<JobInfoForReading>() //DB에서 가게 목록 읽어서 StoreCardView로 보냄
        var timeListToSend = arrayListOf<JobTimeForReading>()

        val postListener = object : ValueEventListener {


            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (intent.hasExtra("clickedstore")) { //사용자가 누른 값이 있으면
                    selectedstore = intent.getStringExtra("clickedstore")
                } else { //시작시 자동으로 읽어올 가게
                    for (snapShotStores: DataSnapshot in p0.child("users").child("workers").child(
                        user!!.uid
                    ).child("RegisteredStore").children) { //user개인정보에 다른 프래그먼트에서 이미 등록한 RegisteredStore
                        if (snapShotStores.key == null) { //DB에 등록된 가게가 없으면 아무것도 읽어오지 않음.

                        } else {
                            selectedstore = snapShotStores.key.toString() //DB에서 아무 가게나 일단 읽어오
                        }
                    }
                }
                var i: Int = 0
                fun writeDatabase() { //내가 신청한 요일, 파트 등의 정보를 읽어와 캘린더에 띄워주는 함수. datasnapshot을 이용하기 때문에 onCreate 바깥이 아닌 onDataChange 내부에 선언했음
                    for (snapShotDays: DataSnapshot in p0.child("stores").child(selectedstore).child(
                        "WorkingPart"
                    ).children) { //요일 // intent에서  null처리 했기때문에 selectedstore는 non-null
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

                                        } else {
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
                                            dayListAdapter.showInCalendar(listOfDay, jobTimeForReading ,day , colors, i)

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

                for (snapShotStore: DataSnapshot in p0.child("users").child("workers")
                    .child(user!!.uid).child("RegisteredStore").children) { //user개인정보에 다른 프래그먼트에서 이미 등록한 RegisteredStore
                    val storename = snapShotStore.key
                    if (storename == null) {

                    } else {
                        val jobInfoForReading = JobInfoForReading(storename)
                        storeList.add(jobInfoForReading)
                    }
                }
                store_list.apply { //cardView로 띄워지게 해당 adapter에 읽은 값 전달
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = StoreCardAdapter(storeList)
                }

                timeList.clear()
                timeListToSend.clear()//직전 실행 값이 남아있을 수 있으므로 초기화

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
                            if(snapShotTime.child("RequestList").child(user!!.displayName.toString()).getValue() != "Request"){
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
                                    timeListToSend.add(jobTimeForReading)
                                }
                            }
                        }
                    }
                }
                writeDatabase()//이미 request된 스케줄 읽어오기. 일단 노랑통닭 홍대점만 읽지만, 등록된 스토어 전부 읽어야 함


                request_button.setOnClickListener {
                    for (snapShotDays: DataSnapshot in p0.child("stores").child(selectedstore).child("WorkingPart").children) { //요일 // 위의 intent에서  null처리 했기때문에 selectedstore는 non-null
                        for (snapShotWorkingParts: DataSnapshot in snapShotDays.children) { //서빙
                            for (snapShotTime: DataSnapshot in snapShotWorkingParts.children) {
                                if (snapShotTime.child("RequestList").child(user!!.displayName.toString()).getValue() == "Checked") {//로그인을 해야 이 액티비티로 이동이 가능하므로 user는 null아님
                                    database //체크된 스케줄 버튼 클릭시 request
                                        .child("stores")
                                        .child(selectedstore)
                                        .child("WorkingPart")
                                        .child(snapShotDays.key.toString())
                                        .child(snapShotWorkingParts.key.toString())
                                        .child(snapShotTime.key.toString())
                                        .child("RequestList")
                                        .child(user!!.displayName.toString())
                                        .setValue("Request")


                                }

                            }
                        }
                    }
                    writeDatabase()
                }


                day_sche_calendar.apply {

                    day_sche_calendar.adapter = dayListAdapter
                    dayListAdapter.setDayList(listOfDay)
                }
                time_list.apply {
                    layoutManager = LinearLayoutManager(context) //cardView로 띄워지게 해당 adapter에 읽은 값 전달
                    adapter = TimeCardAdapter(timeListToSend)
                    // jobTimes =timecardAdapter.copy()

                }


            }
        }
        dirFire.addValueEventListener(postListener)


    }



    private fun initView() {


// view를 사용할 일이 있을때 인자로 넘겨줘야한다.

        //time RecyclerView
        time_sche_calendar.layoutManager = GridLayoutManager(this, 1)
        //RecyclerView가 고정된 사이즈로 1개 항목을 한 줄에 나타나게 한다.
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
            )
            구분선 넣는 코드
            */


        //day RecyclerView
        day_sche_calendar.layoutManager = GridLayoutManager(this, 7)
        //RecyclerView가 고정된 사이즈로 7개 항목을 한 줄에 나타나게 한다.

        day_sche_calendar.addItemDecoration(GridItemDecoration(0, 2))//여백 0,
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
        )
*/

        val timeListAdapter = TimeIntervalAdapter()
        time_sche_calendar.adapter = timeListAdapter
        timeListAdapter.setTimeList(timeSettingData())

        val dayListAdapter = DayListAdapter()
        day_sche_calendar.adapter = dayListAdapter
        dayListAdapter.setDayList(generateDummyData())
        //내용물 없는 빈 칸 xml 띄우기

        // 2개의 RecyclerView의 Scroll을 통일하는 코드
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
        } //daycal에 스크롤 제거 후, scrollListner2를 적용
        scrollListener2 = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                timecal.removeOnScrollListener(scrollListener1)
                timecal.scrollBy(dx, dy)
                timecal.addOnScrollListener(scrollListener1)

            }
        }//timecal에 스크롤 제거 후, scrollListner2를 적용
        timecal.addOnScrollListener(scrollListener1)
        daycal.addOnScrollListener(scrollListener2)


    }

    private fun timeSettingData(): ArrayList<TimeIntervalModel> {
        //
        var i = 0
        val listOfTime = ArrayList<TimeIntervalModel>()
        var timeModel: TimeIntervalModel

        while (i < 24) {
            timeModel = TimeIntervalModel(i)
            listOfTime.add(timeModel)
            i++
        }//0시~ 23시 내용을 TimeIntervaModel class에 하나씩 넣고 이 각 class들을 listofTime배열에 넣는다

        return listOfTime
    }

    private fun generateDummyData(): ArrayList<DayScheduleModel> {
        val listOfDay = ArrayList<DayScheduleModel>()
        var i = 0

        var dayModel: DayScheduleModel

        while (i < 7 * 48) {
            dayModel = DayScheduleModel()//모두 null
            listOfDay.add(dayModel)
            i++
        }


        return listOfDay
    } // 빈 내용을  DayScheduleModel class에 하나씩 넣고 이 각 class들을 listofDay 배열에 넣는다


}

//extension or member ?