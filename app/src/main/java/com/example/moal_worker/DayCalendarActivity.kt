package com.example.moal_worker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.day_calendar.*

class DayCalendarActivity :Fragment() {
    //일단 안씀

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.day_calendar)

        initView()




    }*/
    /*override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        initView()
        return inflater.inflate(R.layout.day_calendar, container, false)
    }*/
    private fun initView() {
        val myLayoutManager = GridLayoutManager(getActivity(), 8)
        //getactivity ?????????????
        day_sche_calendar.layoutManager = myLayoutManager

        //This will for default android divider
        day_sche_calendar.addItemDecoration(GridItemDecoration(10, 2))

        val dayListAdapter = DayListAdapter()
        day_sche_calendar.adapter = dayListAdapter

        dayListAdapter.setDayList(generateDummyData())
    }
    private fun generateDummyData(): ArrayList<DayScheduleModel> {
        val listOfMovie = ArrayList<DayScheduleModel>()

        var movieModel = DayScheduleModel("aa", "Avengers")
        listOfMovie.add(movieModel)

        movieModel = DayScheduleModel("b", "Avengers: Age of Ultron")
        listOfMovie.add(movieModel)

        movieModel = DayScheduleModel("c", "Iron Man 3")
        listOfMovie.add(movieModel)
        movieModel = DayScheduleModel("d", "Avengers: Infinity War" )
        listOfMovie.add(movieModel)

        movieModel = DayScheduleModel("e", "Thor: Ragnarok")
        listOfMovie.add(movieModel)

        movieModel = DayScheduleModel("f", "Black Panther")
        listOfMovie.add(movieModel)

        movieModel = DayScheduleModel("g", "Logan")
        listOfMovie.add(movieModel)

        movieModel = DayScheduleModel("aasd", "Avengers: Age of Ultron")
        listOfMovie.add(movieModel)

        movieModel = DayScheduleModel("asw21edf", "Iron Man 3")
        listOfMovie.add(movieModel)
        movieModel = DayScheduleModel("awr5esdf", "Avengers: Infinity War" )
        listOfMovie.add(movieModel)

        movieModel = DayScheduleModel("asrwedf", "Thor: Ragnarok")
        listOfMovie.add(movieModel)

        movieModel = DayScheduleModel("fae", "Black Panther")
        listOfMovie.add(movieModel)

        movieModel = DayScheduleModel("gwer", "Logan")
        listOfMovie.add(movieModel)

        return listOfMovie
    }

}