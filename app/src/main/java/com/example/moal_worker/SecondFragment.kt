package com.example.moal_worker


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.multidex.MultiDexApplication
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

/**
 * A simple [Fragment] subclass.
 */
class SecondFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var v= inflater.inflate(R.layout.fragment_second, container, false)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        qr_code.setOnClickListener{
            run{
                IntentIntegrator(activity).initiateScan()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        val user = FirebaseAuth.getInstance().currentUser

        if(result != null){

            if(result.contents != null){
                Toast.makeText(context,result.contents,Toast.LENGTH_SHORT)
                txtValue.setText(result.contents)
            } else {
                Toast.makeText(context,"fail...1",Toast.LENGTH_SHORT)
                txtValue.text = "scan failed"
            }
            Toast.makeText(context,"fail...2",Toast.LENGTH_SHORT)
        } else {
            Toast.makeText(context,"fail...3",Toast.LENGTH_SHORT)
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}
