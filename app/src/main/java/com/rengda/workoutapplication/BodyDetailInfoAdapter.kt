package com.rengda.workoutapplication

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class BodyDetailInfoAdapter(val list: List<BodyDetailInfo>):RecyclerView.Adapter<BodyDetailInfoAdapter.ViewHolder>(){
    var itemClickListener:(view:View,postion:Int)->Unit ={_,_->}
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val date: TextView =view.findViewById(R.id.date)
        val title: TextView =view.findViewById(R.id.title)
        val delete: ImageButton =view.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_body_detail,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
     return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val item=list[position]
        holder.title.text=item.bodyData+item.unit
        holder.date.text=item.date

        //删除掉记录
        holder.delete.setOnClickListener{
            itemClickListener(it,position)
        }
    }

    fun setOnItemClickListener(action:(view:View,postion:Int)->Unit){
        itemClickListener=action
    }



}