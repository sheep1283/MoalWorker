package com.example.moal_worker




import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.day_calendar.*


/**
 * A simple [Fragment] subclass.
 */

class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_home, container,false)
        // val include_cal_view : View = v.findViewById(R.id.cal_view_1)
        val b1: ImageButton = v.run { findViewById(R.id.button_workingschedule) }
        b1.setOnClickListener {
            val intent = Intent(activity, WorkingScheduleActivity::class.java)
            startActivity(intent)
        }
        return v
    }
    //  val requestclicked  = 1
    // var selectedstore = ""
    // var rootRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    // val dirFire: DatabaseReference = rootRef
    val listOfDay = ArrayList<DayScheduleModel>(generateDummyData())
    var storeList = arrayListOf<JobInfoForReading>()
    // var data =  arrayListOf<JobInfoForReading>()
    var timeList = arrayListOf<JobTimeForReading>()


    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)
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






        var rootRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()
        val dirFire: DatabaseReference = rootRef.child("노랑통닭 홍대점")


        initView(v)
        val postListener= object: ValueEventListener {

            val dayListAdapter = DayListAdapter()


            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                timeList.clear()
                for (snapShotDays: DataSnapshot in p0.children){ //요일
                    for(snapShotWorkingParts : DataSnapshot in snapShotDays.children){ //서빙
                        for(snapShotTime: DataSnapshot in snapShotWorkingParts.children){
                            if (snapShotTime.child("RequestList").child("Jini").getValue() == "Request"){
                                val day = snapShotDays.key
                                val position = snapShotWorkingParts.key
                                val part = snapShotTime.key
                                val jobTimeInfo: JobTimeInfo? = snapShotTime.getValue(JobTimeInfo::class.java)

                                if (jobTimeInfo == null || day == null || position == null || part == null){

                                }else{


                                    val jobTimeForReading = JobTimeForReading(jobTimeInfo.startHour,jobTimeInfo.startMin,jobTimeInfo.endHour,jobTimeInfo.endMin,
                                        jobTimeInfo.requirePeopleNum,null, position,part,day)
                                    timeList.add(jobTimeForReading)
                                }
                            }

                        }
                    }
                }
                /* time_list.apply {
                     layoutManager = LinearLayoutManager(context)
                     adapter = TimeCardAdapter(timeList, listOfDay)
                     day_sche_calendar.apply{
                         day_sche_calendar.adapter = dayListAdapter
                         dayListAdapter.setDayList(listOfDay)
                     }//이 코드 필요!

                     // jobTimes =timecardAdapter.copy()

                 }*/
                var i : Int =0

                for (jobTimeForReading in timeList) {
                    //jobtimeforreading 객체들이 들어있는 jobtimes에서 하나씩 읽기

                    //dayListAdapter.setDayList(generateDummyData())

                    // var dayModel: DayScheduleModel

                    val day = jobTimeForReading.jobDay
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
                    val startMin = (((jobTimeForReading.startMin)/6)*0.1).toFloat()
                    val endHour = jobTimeForReading.endHour
                    val endMin = (((jobTimeForReading.endMin)/6)*0.1).toFloat()
                    val timeInt: Float = 0.5F
                    var start: Float = (startHour + startMin).toFloat()
                    var st : Float = start
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
                day_sche_calendar.apply{
                    val dayListAdapter = DayListAdapter()
                    day_sche_calendar.adapter = dayListAdapter
                    dayListAdapter.setDayList(listOfDay)
                }



            }
        }

        dirFire.child("WorkingPart").addValueEventListener(postListener)

        var jobTimes = timeList
        var timeIntList: ArrayList<Int> = arrayListOf()
        var i = 0 //day 인덱스 변수


    }



    private fun initView(v: View) {

        time_sche_calendar.layoutManager = GridLayoutManager(activity, 1)
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


        day_sche_calendar.layoutManager = GridLayoutManager(activity, 7)
        //RecyclerView가 고정된 사이즈로 7개 항목을 한 줄에 나타나게 한다.

        //This will for default android divider
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
        )
*/

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
            timeModel = TimeIntervalModel(i)
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

}