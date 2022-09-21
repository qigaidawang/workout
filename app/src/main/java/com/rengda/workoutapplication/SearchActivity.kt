package com.rengda.workoutapplication

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_search.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.LinkedHashMap


class SearchActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.hide()
        //获取当前时间
        var defualtTime=  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             val current=  LocalDateTime.now()
             val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
             current.format(formatter)
        }else {
            val current=Calendar.getInstance()
            SimpleDateFormat("yyyy-MM-dd").format(current.time)
        }




        //默认本周
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current=  LocalDateTime.now()
            val fromDay= current.minusDays((current.dayOfWeek.value-1).toLong())
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val fromDayTime=fromDay.format(formatter)
            dateFrom.setText(fromDayTime)
            dateTo.setText(defualtTime)
        } else {
            val current=Calendar.getInstance()
            var dayOfWeek:Int
            if(Calendar.DAY_OF_WEEK-1>0)  {//周一为一
                dayOfWeek= Calendar.DAY_OF_WEEK-1
            } else
                dayOfWeek=7
            val  subtractDay=dayOfWeek-1
            current.add(Calendar.DAY_OF_MONTH,-subtractDay)
            val fromDayTime= SimpleDateFormat("yyyy-MM-dd").format(current.time)
            dateFrom.setText(fromDayTime)
            dateTo.setText(defualtTime)
        }





        year.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dateTo.setText(defualtTime)
                val current=  LocalDateTime.now()
                val formatterFrom = DateTimeFormatter.ofPattern("yyyy")
                val year = current.format(formatterFrom)
                dateFrom.setText(year+"-01-01")
                dateTo.setText(defualtTime)
            }else{
                val current=Calendar.getInstance()
                val year= SimpleDateFormat("yyyy").format(current.time)
                dateFrom.setText(year+"-01-01")
                dateTo.setText(defualtTime)
            }
        }

        month.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current=  LocalDateTime.now()
                val formatterFrom = DateTimeFormatter.ofPattern("yyyy-MM")
                val month = current.format(formatterFrom)
                dateFrom.setText(month+"-01")
                dateTo.setText(defualtTime)
            }else{
                val current=Calendar.getInstance()
                val month= SimpleDateFormat("yyyy-MM").format(current.time)
                dateFrom.setText(month+"-01")
                dateTo.setText(defualtTime)
            }



        }

        week.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current=  LocalDateTime.now()
                val fromDay= current.minusDays((current.dayOfWeek.value-1).toLong())
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val fromDayTime=fromDay.format(formatter)
                dateFrom.setText(fromDayTime)
                dateTo.setText(defualtTime)
            } else {
                val current=Calendar.getInstance()
                var dayOfWeek:Int
                if(Calendar.DAY_OF_WEEK-1>0)  {//周一为一
                    dayOfWeek= Calendar.DAY_OF_WEEK-1
                } else
                    dayOfWeek=7
                val  subtractDay=dayOfWeek-1
                current.add(Calendar.DAY_OF_MONTH,-subtractDay)
                val fromDayTime= SimpleDateFormat("yyyy-MM-dd").format(current.time)
                dateFrom.setText(fromDayTime)
                dateTo.setText(defualtTime)
            }
        }



        confirm.setOnClickListener {
            val intent=Intent()
            val map= LinkedHashMap<String,String>()
            if (titleEdit.text.toString()!=""){
                map["title like ?"]="%"+titleEdit.text.toString()+"%"
            }
            if (titleNotIncludeEdit.text.toString()!=""){
                map["title not like ?"]="%"+titleNotIncludeEdit.text.toString()+"%"
            }

            if (dateFrom.text.toString()!=""){
                map["date>=?"]=dateFrom.text.toString()
            }
            if (dateTo.text.toString()!=""){
                map["date<=?"]=dateTo.text.toString()
            }

            intent.putExtra("searchMap",map)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }

    }
}