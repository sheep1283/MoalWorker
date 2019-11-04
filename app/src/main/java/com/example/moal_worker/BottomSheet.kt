package com.example.moal_worker


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_bottom_sheet.view.*


class BottomSheet : BottomSheetDialogFragment() {

    val database = FirebaseDatabase.getInstance().reference
    var myRef : DatabaseReference = FirebaseDatabase.getInstance().getReference()

    private var mBottomSheetListener: BottomSheetListener?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)

        v.button1.setOnClickListener {
            database.child("한국항공대학교").child("StoreInfo").child("Monday").child("open").setValue("user1 :"+ "hope")
        }
        v.button2.setOnClickListener {
            database.child("한국항공대학교").child("StoreInfo").child("Monday").child("middle").setValue("user1 :"+ "Hope")
        }
        v.button3.setOnClickListener {
            database.child("한국항공대학교").child("StoreInfo").child("Monday").child("dinner").setValue("user1 :"+ "Hope")

        }

        return v
    }

    interface BottomSheetListener{
        fun onOptionClick(text: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            mBottomSheetListener = context as BottomSheetListener?
        }
        catch (e: ClassCastException){
            throw ClassCastException(context!!.toString())
        }

    }


}