package com.example.moal_worker

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_working_schedule.*

class RegistrationPersonal : Fragment() {
//개인일정 추가를 위해 만들다가 중지함 나중에 하려고
    val database = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.activity_working_schedule, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var timeList = arrayListOf<JobTimeInfo>() // 여러가지 파트타임  오픈, 미들 마감 등등을 넣어주기위한 timeList
        var dayList = arrayListOf<String>() // 월, 화, 수, 목, 금, 토, 일 중 어떤날을 선택했는가

        //파트타임 추가 버튼 눌렀을 때
        button_personalSchedule.setOnClickListener {

            val builder = AlertDialog.Builder(context)
            val dialogView = layoutInflater.inflate(R.layout.dialog_set_personal, null, false)
            val dialogText = dialogView.findViewById<EditText>(R.id.dialog_part_name)
            val dialogStartHourPicker: NumberPicker =
                dialogView.findViewById(R.id.dialog_start_hour_picker)
            val dialogStartMinPicker: NumberPicker =
                dialogView.findViewById(R.id.dialog_start_min_picker)
            val dialogEndHourPicker: NumberPicker =
                dialogView.findViewById(R.id.dialog_end_hour_picker)
            val dialogEndMinPicker: NumberPicker =
                dialogView.findViewById(R.id.dialog_end_min_picker)
            val dialogRequiredNumber = dialogView.findViewById<EditText>(R.id.people)

            dialogStartHourPicker.minValue = 0
            dialogStartHourPicker.maxValue = 24
            dialogEndHourPicker.minValue = 0
            dialogEndHourPicker.maxValue = 24
            //0-24시로 만들어 줄거니까 최소 최대값 설정

            dialogStartMinPicker.minValue = 0
            dialogStartMinPicker.maxValue = 59
            dialogEndMinPicker.minValue = 0
            dialogEndMinPicker.maxValue = 59
            //0-59분으로 만들어 줄거니까 최소 최대값 설정

            dialogStartHourPicker.wrapSelectorWheel = true //무한루프 가능
            dialogStartHourPicker.descendantFocusability =
                NumberPicker.FOCUS_BLOCK_DESCENDANTS //키보드로 수정하기 금지
            dialogEndHourPicker.wrapSelectorWheel = true //무한루프 가능
            dialogEndHourPicker.descendantFocusability =
                NumberPicker.FOCUS_BLOCK_DESCENDANTS //키보드로 수정하기 금지
            dialogStartMinPicker.wrapSelectorWheel = true //무한루프 가능
            dialogStartMinPicker.descendantFocusability =
                NumberPicker.FOCUS_BLOCK_DESCENDANTS //키보드로 수정하기 금지
            dialogEndMinPicker.wrapSelectorWheel = true //무한루프 가능
            dialogEndMinPicker.descendantFocusability =
                NumberPicker.FOCUS_BLOCK_DESCENDANTS //키보드로 수정하기 금지

            dialogStartHourPicker.setFormatter { i -> String.format("%02d", i) }
            dialogStartMinPicker.setFormatter { i -> String.format("%02d", i) }
            dialogEndHourPicker.setFormatter { i -> String.format("%02d", i) }
            dialogEndMinPicker.setFormatter { i -> String.format("%02d", i) }
            //각각의 다이얼 시간을 00, 01, ... , 24 이런식으로 보이게 만들어 줌

            var pickedEndHour = dialogEndHourPicker.value
            var pickedEndMin = dialogEndMinPicker.value
            var pickedStartHour = dialogStartHourPicker.value
            var pickedStartMin = dialogStartMinPicker.value


            dialogEndHourPicker.setOnValueChangedListener { numberPicker, i, i2 ->
                pickedEndHour = dialogEndHourPicker.value
            }
            dialogEndMinPicker.setOnValueChangedListener { numberPicker, i, i2 ->
                pickedEndMin = dialogEndMinPicker.value
            }
            dialogStartHourPicker.setOnValueChangedListener { numberPicker, i, i2 ->
                pickedStartHour = dialogStartHourPicker.value
            }
            dialogStartMinPicker.setOnValueChangedListener { numberPicker, i, i2 ->
                pickedStartMin = dialogStartMinPicker.value
            }//스크롤이 바뀔때마다 업데이트 해줄 수 있도록 리스너 등록


            //찍은 시간을 저장하는 변수들 안바뀌니까 val

            builder.setView(dialogView)
            val dialog: AlertDialog = builder.create()
            dialog.show()

            /*
            기본 nagative, positive button을 사용하지 않기 위한 노력1단계: dialog.dismiss()를 사용할 것이기 때문에 빌더자체를 보여주는게 아니라
            다이얼로그로 한번 옮겨와서 dismiss 함
            */

            val dialogBtnComplete: TextView = dialogView.findViewById(R.id.dialog_complete)
            val dialogBtnCancel: TextView = dialogView.findViewById(R.id.dialog_cancel)//확인,취소 버튼

            dialogBtnComplete.setOnClickListener {
                var jobTime = JobTimeInfo()
                jobTime.partName = dialogText.getText().toString()
                jobTime.startHour = pickedStartHour
                jobTime.startMin = pickedStartMin
                jobTime.endHour = pickedEndHour
                jobTime.endMin = pickedEndMin
                jobTime.requirePeopleNum = dialogRequiredNumber.getText().toString().toInt()

                timeList.add(jobTime)

                time_list.apply {
                    layoutManager = LinearLayoutManager(activity)
                   // adapter = TimeCardAdapter(timeList)
                }

                Log.d(
                    "good",
                    timeList[0].partName + "'s start,end time is: " + timeList[0].startHour + "," + timeList[0].endHour
                )

                dialog.dismiss()
            }

            dialogBtnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
    }
}
        //파트타임 추가 눌렀을때 클릭 리스너 끝
