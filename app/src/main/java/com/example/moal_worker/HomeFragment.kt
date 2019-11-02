package com.example.moal_worker


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        val b1 : Button = v.run { findViewById(R.id.button_workingschedule) }
        b1.setOnClickListener {
            val intent = Intent (activity, WorkingScheduleActivity::class.java)
            startActivity(intent)
        }
        return v

    }


}
