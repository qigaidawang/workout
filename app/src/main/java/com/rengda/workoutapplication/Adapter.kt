package com.rengda.workoutapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(val list: List<WorkOut>):RecyclerView.Adapter<Adapter.ViewHolder>(){
    var itemClickListener:(view:View,postion:Int)->Unit ={_,_->}
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val no:TextView=view.findViewById(R.id.No);
        val date: TextView =view.findViewById(R.id.date)
        val title: TextView =view.findViewById(R.id.title)
        val time: TextView =view.findViewById(R.id.time)
        val delete: ImageButton =view.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
     return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val item=list[position]
       holder.no.text=(position+1).toString()
        holder.title.text=item.title
        holder.time.text=(item.time.toString()+"分钟")
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