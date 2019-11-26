package com.example.moal_worker


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
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

        return v
    }


}
