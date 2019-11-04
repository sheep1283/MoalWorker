package com.example.moal_worker

import android.app.PendingIntent.getActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.calender_activity.*
import kotlinx.android.synthetic.main.day_calendar.*
import androidx.recyclerview.widget.RecyclerView
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.LayoutInflater
import android.view.View


class CalenderActivity : AppCompatActivity(), BottomSheet.BottomSheetListener {
    override fun onOptionClick(text: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.day_calendar)
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
        val scrollListener1:RecyclerView.OnScrollListener
        lateinit var scrollListener2:RecyclerView.OnScrollListener
        scrollListener1 = object:RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                daycal.removeOnScrollListener(scrollListener2)
                daycal.scrollBy(dx, dy)
                daycal.addOnScrollListener(scrollListener2)
            }
        }
        scrollListener2=object :RecyclerView.OnScrollListener(){
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
    private fun timeSettingData(): ArrayList<TimeIntervalModel>{
        var i: Int=0
        val listOfTime =ArrayList<TimeIntervalModel>()
        var timeModel : TimeIntervalModel

        while(i<12){
            timeModel=TimeIntervalModel(i, "am")
            listOfTime.add(timeModel)
            i++
        }
        i=0
        while(i<12){
            timeModel=TimeIntervalModel(i, "pm")
            listOfTime.add(timeModel)
            i++
        }
        return listOfTime
    }
    private fun generateDummyData(): ArrayList<DayScheduleModel> {
        val listOfDay =ArrayList<DayScheduleModel>()
        var i: Int=0

//        val listOfTime =ArrayList<TimeIntervalModel>()
//        var timeModel : TimeIntervalModel
        var dayModel : DayScheduleModel

        while (i < 7*24) {
            dayModel = DayScheduleModel()
            listOfDay.add(dayModel)
            i++
        }

        return listOfDay
    }
}

