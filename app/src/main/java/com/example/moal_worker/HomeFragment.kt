package com.example.moal_worker


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.day_calendar.*


/**
 * A simple [Fragment] subclass.
 */

class HomeFragment : Fragment()
{
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
        } //home fragment의 +버튼을 누르면 workingSchedualeActivity를 불러온다.
        return v
    }

    val listOfDay = ArrayList<DayScheduleModel>(generateDummyData())
    var storeList = arrayListOf<JobInfoForReading>()
    var timeList = arrayListOf<JobTimeForReading>()
    val user = FirebaseAuth.getInstance().currentUser


    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        super.onViewCreated(v, savedInstanceState)

        val dayListAdapter = DayListAdapter()
        day_sche_calendar.adapter = dayListAdapter

//        var rootRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()
//        val dirFire: DatabaseReference = rootRef.child("stores").child("노랑통닭 홍대점")

        var rootRef: DatabaseReference = FirebaseDatabase.getInstance().getReference()
        val dirFire: DatabaseReference = rootRef
        val user = FirebaseAuth.getInstance().currentUser

        initView(v)  //빈 calendar xml 틀

        val name = ""
        var selectedstore = ""

        val postListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                timeList.clear()
                for(snapShotStores : DataSnapshot in p0.child("users").child("workers").child(user!!.uid).child("RegisteredStore").children) { //로그인을 해야 이 액티비티로 이동이 가능하므로 user는 null아님
                    for (snapShotDays: DataSnapshot in p0.child("stores").child(snapShotStores.key.toString()).child("WorkingPart").children) { //요일 // 위의 intent에서  null처리 했기때문에 selectedstore는 non-null
                        for (snapShotWorkingParts: DataSnapshot in snapShotDays.children) { //서빙
                            for (snapShotTime: DataSnapshot in snapShotWorkingParts.children) { //오미마
                                val day = snapShotDays.key
                                val position = snapShotWorkingParts.key
                                val part = snapShotTime.key
                                val jobTimeInfo: JobTimeInfo? =
                                    snapShotTime.getValue(JobTimeInfo::class.java)

                                if (jobTimeInfo == null || day == null || position == null || part == null) {  //점주 앱에서 네개의 값이 null이 되면 받지 않게 처리됨

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
                }

                var i: Int = 0
                //이미 request된 스케줄 읽어오기
                for(snapShotStores : DataSnapshot in p0.child("users").child("workers").child(user.uid).child("RegisteredStore").children){
                    selectedstore = snapShotStores.key.toString()
                    for (snapShotDays: DataSnapshot in p0.child("stores").child(selectedstore).child("WorkingPart").children) { //요일 // 위의 intent에서  null처리 했기때문에 selectedstore는 non-null
                        for (snapShotWorkingParts: DataSnapshot in snapShotDays.children) { //서빙
                            for (snapShotTime: DataSnapshot in snapShotWorkingParts.children) {
                                if (snapShotTime.child("RequestList").child(user!!.displayName.toString()).getValue() == "Request") { //로그인을 해야 이 액티비티로 이동이 가능하므로 user는 null아님
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
                                            dayListAdapter.showInCalendar(listOfDay, jobTimeForReading ,day, i)
                                            //jodTimeForReading의 내용들을 calendar에 나타내게 하는 코드
                                        }
                                        if(day_sche_calendar != null){ //세번째 fragment에서 등록 후 이부분 널처리 요함
                                            day_sche_calendar.adapter = dayListAdapter
                                            dayListAdapter.setDayList(listOfDay)}
                                            /*day_sche_calendar.apply {
                                                day_sche_calendar.adapter = dayListAdapter
                                                dayListAdapter.setDayList(listOfDay)
                                            }*/
                                    }
                                    if (i == 9) {
                                        i = 0
                                    } else {
                                        i++
                                    }    //한 스케줄 적용이 끝나면 color 체인지, color배열 9개 색 다 쓰면
                                    //0번째 인덱스로 다시 복귀
                                }
                            }
                        }

                    }
                }

            }
        }
        dirFire.addValueEventListener(postListener)
    }


    private fun initView(v: View) { //fragment에서 onViewCreated 밖에 함수를 선언할때
        // view를 사용할 일이 있어서 인자로 넘겨줘야한다.

        //time RecyclerView
        time_sche_calendar.layoutManager = GridLayoutManager(activity, 1)
        //RecyclerView가 고정된 사이즈로 1개 항목을 한 줄에 나타나게 한다.

        //day RecyclerView
        day_sche_calendar.layoutManager = GridLayoutManager(activity, 7)
        //RecyclerView가 고정된 사이즈로 7개 항목을 한 줄에 나타나게 한다.



        val timeListAdapter = TimeIntervalAdapter()
        time_sche_calendar.adapter = timeListAdapter
        timeListAdapter.setTimeList(timeSettingData())

        val dayListAdapter = DayListAdapter()
        day_sche_calendar.adapter = dayListAdapter
        dayListAdapter.setDayList(generateDummyData())
        //내용물 없는 빈 칸 xml 띄우기

        // 2개의 RecyclerView의 Scroll을 통일하는 코드
        val daycal: RecyclerView = v.findViewById(R.id.day_sche_calendar) //v(view)인자 사용부분
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
        var i: Int = 0
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
        var i: Int = 0

        var dayModel: DayScheduleModel

        while (i < 7 * 48) {
            dayModel = DayScheduleModel()//모두 null
            listOfDay.add(dayModel)
            i++
        }
        return listOfDay
    } // 빈 내용을  DayScheduleModel class에 하나씩 넣고 이 각 class들을 listofDay 배열에 넣는다
}