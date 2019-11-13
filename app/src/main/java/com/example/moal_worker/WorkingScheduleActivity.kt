package com.example.moal_worker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_working_schedule.*
import kotlinx.android.synthetic.main.day_calendar.day_sche_calendar
import kotlinx.android.synthetic.main.day_calendar.time_sche_calendar


class WorkingScheduleActivity : AppCompatActivity() {

    var rootRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    val dirFire: DatabaseReference = rootRef.child("노랑통닭 홍대점")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_working_schedule)


        var timeList =arrayListOf<JobTimeForReading>()

        dirFire.child("WorkingPart").addValueEventListener(object: ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                timeList.clear()
                for (snapShotDays: DataSnapshot in p0.children){ //요일
                    for(snapShotWorkingParts : DataSnapshot in snapShotDays.children){ //서빙
                        for(snapShotTime: DataSnapshot in snapShotWorkingParts.children){ //오미마
                            val day = snapShotDays.key
                            val position = snapShotWorkingParts.key
                            val part = snapShotTime.key
                            val jobTimeInfo: JobTimeInfo? = snapShotTime.getValue(JobTimeInfo::class.java)

                            if (jobTimeInfo == null || day == null || position == null || part == null){

                            }else{
                                val jobTimeForReading = JobTimeForReading(jobTimeInfo.startHour,jobTimeInfo.startMin,jobTimeInfo.endHour,jobTimeInfo.endMin,
                                    jobTimeInfo.requirePeopleNum,position,part,day)
                                timeList.add(jobTimeForReading)
                            }
                        }
                    }
                }
                time_list.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = TimeCardAdapter(timeList)
                }
            }
        })

        initView()

    }




    private fun initView() {


        time_sche_calendar.layoutManager = GridLayoutManager(this, 1)
        time_sche_calendar.addItemDecoration(GridItemDecoration(10, 2))
        time_sche_calendar.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.HORIZONTAL
            )
        )
        /*time_sche_calendar.addItemDecoration(
                DividerItemDecoration(
                    this,
                    LinearLayoutManager.VERTICAL
                )
            )*/


        day_sche_calendar.layoutManager = GridLayoutManager(this, 7)

        //This will for default android divider
        day_sche_calendar.addItemDecoration(GridItemDecoration(10, 2))
        day_sche_calendar.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.HORIZONTAL
            )
        )
        day_sche_calendar.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )


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

        while (i < 12) {
            timeModel = TimeIntervalModel(i, "am")
            listOfTime.add(timeModel)
            i++
        }
        i = 0
        while (i < 12) {
            timeModel = TimeIntervalModel(i, "pm")
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

        while (i < 7 * 24) {
            dayModel = DayScheduleModel()
            listOfDay.add(dayModel)
            i++
        }

        return listOfDay
    }

}
