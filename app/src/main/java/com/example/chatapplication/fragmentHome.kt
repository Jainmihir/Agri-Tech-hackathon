package com.example.chatapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*


class fragmentHome : Fragment() {

    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<user>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        userRecyclerView = view.findViewById(R.id.recyclerview)
        userRecyclerView.layoutManager = LinearLayoutManager(context)
        dbref = FirebaseDatabase.getInstance().getReference("farmer")
        getuserdata()
        return view

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    private fun getuserdata()
    {

        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Toast.makeText(activity,"Details Fetch Successfull",Toast.LENGTH_SHORT).show()
                    for (userSnapshot in snapshot.children)
                    {
                        val user: user? = userSnapshot.getValue(user::class.java)

                        if (user != null) {
                            userArrayList.add(user)
                        }
                    }
                    val myAdapter=MyAdapter(context!!,userArrayList)
                    Log.i("AdapterValues", userArrayList.toString())
                    userRecyclerView.adapter=myAdapter

            }

            override fun onCancelled(error: DatabaseError) {
               Toast.makeText(context,"Details Fetch Failed",Toast.LENGTH_SHORT).show()
            }

        })
    }

}