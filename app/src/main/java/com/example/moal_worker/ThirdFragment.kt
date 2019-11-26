package com.example.moal_worker


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_third.*

/**
 * A simple [Fragment] subclass.
 */
class ThirdFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_third, container, false)
        var nameForSearch = ""
        btn_store_search.setOnClickListener{
            nameForSearch = store_name.text.toString()

        }

        btn_registration.setOnClickListener {
            if (nameForSearch != null){

            }
            else{

            }
        }

        return v
    }

}
