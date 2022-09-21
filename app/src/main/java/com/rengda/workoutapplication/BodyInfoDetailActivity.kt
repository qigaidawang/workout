package com.rengda.workoutapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_body_info_detail.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class BodyInfoDetailActivity : AppCompatActivity() {
    private lateinit var type:String
    private lateinit var db: SQLiteDatabase
    lateinit var adapter: BodyDetailInfoAdapter
    val list= arrayListOf<BodyDetailInfo>()
    private lateinit var context:Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_info_detail)
        type=intent.getStringExtra("type")?:""
        if(type=="Weight"){
            toolbar.setTitle("体重")
        }else if (type=="BFR"){
            toolbar.setTitle("体脂率")
        }


        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }



        db= Util.getWritableDatabase(this)
        context=this;

        adapter= BodyDetailInfoAdapter(list)
        recyclerView.adapter=adapter
        recyclerView.layoutManager= LinearLayoutManager(this)






        adapter.setOnItemClickListener{
                _,position->
            AlertDialog.Builder(context)
                .setTitle("操作确认")
                .setMessage("是否确定删除该数据?")
                .setNegativeButton("确定"){
                        _,_->
                    val count= db.delete(type,"id=?", arrayOf(list[position].id.toString()))
                    doQuery()
                }
                .setPositiveButton("取消"){
                        it,_->
                    it.dismiss()
                }.create().show()
        }

        add.setOnClickListener {
            creatAlertDialog()
        }

        //进来查询显示
        doQuery()

    }


    @SuppressLint("Range")
    fun doQuery(){
        list.clear()
        val cursor=db.query(type,null,null,null,null,null,"date")
        while (cursor.moveToNext()){
            val bodyData=cursor.getString(cursor.getColumnIndex(type))
            val date=cursor.getString(cursor.getColumnIndex("date"))
            val id=cursor.getInt(cursor.getColumnIndex("id"))
            var unit:String=""
            if(type=="Weight"){
                unit="KG"
            }else if (type=="BFR"){
                unit="%"
            }

            val BodyDetailInfo=BodyDetailInfo(id,bodyData,date,unit)
            list.add(BodyDetailInfo)
            adapter.notifyDataSetChanged()

        }
    }

    fun creatAlertDialog(){
        val view: View = View.inflate(this,R.layout.dialog_body_view, null)
        val alertDialog=   AlertDialog.Builder(this).setView(view).create()
        val button= view.findViewById<Button>(R.id.confirm)
        val title=view.findViewById<TextView>(R.id.title)
        val contextEdit=view.findViewById<EditText>(R.id.contextEdit)
        val dateEdit=view.findViewById<EditText>(R.id.date)
        if (type=="BFR") {
            title.setText("体脂率(%):")
        }else if (type=="Weight") {
            title.setText("体重(KG):")
        }

        var defualtTime=""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current=  LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            defualtTime = current.format(formatter)
        } else {
            val current= Calendar.getInstance()
            defualtTime= SimpleDateFormat("yyyy-MM-dd").format(current.time)
        }

        dateEdit.setText(defualtTime)



        button.setOnClickListener{//插入一条数据
            val values= ContentValues().apply {
                put("user","陈雯")
                put("date",dateEdit.text.toString())
                put(type,contextEdit.text.toString())
            }


            db.insert(type,null,values)
            alertDialog.dismiss()
            doQuery()
        }

        alertDialog.show()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->finish()
        }
        return true
    }
}