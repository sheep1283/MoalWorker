package com.example.moal_worker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.day_calendar.*
import kotlinx.android.synthetic.main.part_time_cardview.view.*

class TimeCardAdapter(val timeListToSend:ArrayList<JobTimeForReading>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeCardAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return timeListToSend.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ViewHolder
        val data: JobTimeForReading = timeListToSend[position]
        holder.bind(data)

    }

    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.part_time_cardview, parent, false)) {
        val database = FirebaseDatabase.getInstance().reference
        val user = FirebaseAuth.getInstance().currentUser
        val dirFire: DatabaseReference = FirebaseDatabase.getInstance().getReference()

        fun bind(data: JobTimeForReading) {
            itemView.cardView_startTime.text =  String.format("%02d",data.startHour)+" : "+String.format("%02d",data.startMin)
            itemView.cardView_endTime.text = String.format("%02d",data.endHour)+" : "+String.format("%02d",data.endMin)
            itemView.cardView_people.text = data.requirePeopleNum.toString() + " 명"
            itemView.cardView_position.text = data.positionName
            itemView.cardView_partName.text = data.partName
            itemView.cardView_day.text = data.jobDay + "요일"

            itemView.cardView_checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (itemView.cardView_checkBox.isChecked == true){
                    if (data.storeName != null) { //임시 null허용 처리
                        database.child("stores").child(data.storeName).child("WorkingPart")
                            .child(data.jobDay)
                            .child(data.positionName)
                            .child(data.partName)
                            .child("RequestList")
                            .child(user!!.displayName.toString()) //로그인을 해야 이 액티비티로 이동이 가능하므로 user는 null아님
                            .setValue("Checked")
                        return@setOnCheckedChangeListener

                    }
                    itemView.cardView_checkBox.isChecked = true
                }
                else {
                    if (data.storeName != null) { //임시 null허용 처리
                        database.child("stores").child(data.storeName).child("WorkingPart").child(data.jobDay)
                            .child(data.positionName)
                            .child(data.partName)
                            .child("RequestList")
                            .child(user!!.displayName.toString()) //로그인을 해야 이 액티비티로 이동이 가능하므로 user는 null아님
                            .removeValue()
                        itemView.cardView_checkBox
                        return@setOnCheckedChangeListener
                    }
                }
            }
        }
    }
}