package com.example.moal_worker


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_second.*

/**
 * A simple [Fragment] subclass.
 */
class SecondFragment : Fragment() {

    val database = FirebaseDatabase.getInstance().reference
    var myRef : DatabaseReference = FirebaseDatabase.getInstance().getReference()
    val dirFire : DatabaseReference = myRef

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var v= inflater.inflate(R.layout.fragment_second, container, false)
        val b1 : Button = v.run { findViewById(R.id.button4) }
        b1.setOnClickListener {
            database.child("한국항공대학교").child("WorkingPart").setValue("WorkingPart")


        }
        return v
    }


}
