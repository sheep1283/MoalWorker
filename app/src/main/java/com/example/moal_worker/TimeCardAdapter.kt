package com.example.moal_worker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.day_calendar.*
import kotlinx.android.synthetic.main.part_time_cardview.view.*

class TimeCardAdapter(val timeList:ArrayList<JobTimeForReading>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeCardAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return timeList.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ViewHolder
        val data: JobTimeForReading = timeList[position]
        holder.bind(data)



    }


    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.part_time_cardview, parent, false)) {
        val database = FirebaseDatabase.getInstance().reference
        val checkbox = itemView.cardView_checkBox




        fun bind(data: JobTimeForReading) {

            itemView.cardView_startTime.text =
                data.startHour.toString() + " : " + data.startMin.toString()
            itemView.cardView_endTime.text =
                data.endHour.toString() + " : " + data.endMin.toString()
            itemView.cardView_people.text = data.requirePeopleNum.toString() + " 명"
            itemView.cardView_position.text = data.positionName
            itemView.cardView_partName.text = data.partName
            itemView.cardView_day.text = data.jobDay + "요일"

            itemView.cardView_checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    if (data.storeName != null) { //임시 null허용 처리
                        database.child(data.storeName).child("WorkingPart")
                            .child(data.jobDay)
                            .child(data.positionName)
                            .child(data.partName)
                            .child("RequestList")
                            .child("Jini")
                            .setValue("Checked")

                    }
                    itemView.cardView_checkBox.isChecked = true
                }
                else {

                    if (data.storeName != null) { //임시 null허용 처리
                        database.child(data.storeName).child("WorkingPart")
                            .child(data.jobDay)
                            .child(data.positionName)
                            .child(data.partName)
                            .child("RequestList")
                            .child("Jini")
                            .removeValue()

                    }


                }

            }
        }
    }

}