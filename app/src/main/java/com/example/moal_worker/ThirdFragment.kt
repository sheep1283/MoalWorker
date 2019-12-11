package com.example.moal_worker


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_third.*
/**
 * A simple [Fragment] subclass.
 */
class ThirdFragment : Fragment() {

    var dirFire: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    val database = FirebaseDatabase.getInstance().reference
    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v : View = inflater.inflate(R.layout.fragment_third, container, false)
        return v
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var storename = "" //DB에서 찾을 가게 이름
        var registerStore = "" //검색하고자하는 가게 이름
        dirFire.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                want_to_join.setOnClickListener {
                    if(join_store.text.toString() == null){ //공백상태로 요청하기 누를 경우
                        Toast.makeText(activity, "검색할 가게 이름을 입력해주세요!", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        registerStore = join_store.text.toString()
                    }
                    for (snapShotStore: DataSnapshot in p0.child("stores").children) {
                        val snapShottedName = snapShotStore.key
                        if (snapShottedName == null) { //DB에 등록된 가게가 아예 없을 경우. 유저 사용시 이런 경우는 없을 것.
                            Toast.makeText(activity, "아직 가게 정보가 없어요!", Toast.LENGTH_SHORT).show()
                        } else {
                            if (snapShottedName == registerStore) { //DB에 해당 가게가 없을 경우
                                storename = registerStore
                                database.child("stores") //해당 가게에 요청 보내기
                                    .child(storename)
                                    .child("StoreInfo")
                                    .child("WantToJoin")
                                    .child(user!!.displayName.toString())
                                    .setValue(user!!.uid)
                                database
                                    .child("users") //내 유저 정보 중 등록된 가게 리스트에 추가하기
                                    .child("workers")
                                    .child(user!!.uid)
                                    .child("RegisteredStore")
                                    .child(storename)
                                    .setValue("Join")
                                Toast.makeText(activity, "등록완료", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(activity, "그런 가게 없어요!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
}
