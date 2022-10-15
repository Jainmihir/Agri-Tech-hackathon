package com.example.chatapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter( private val userList : ArrayList<user>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem =  userList[position]
        holder.name.text = currentitem.Name
        holder.state.text = currentitem.State
        holder.gender.text = currentitem.Gender
        holder.phone.text = currentitem.MobileNumber
    }

    override fun getItemCount(): Int {
        return userList.size
    }
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.sh11)
        val state : TextView = itemView.findViewById(R.id.sh22)
        val phone : TextView = itemView.findViewById(R.id.sh33)
        val gender : TextView = itemView.findViewById(R.id.sh44)
    }
}