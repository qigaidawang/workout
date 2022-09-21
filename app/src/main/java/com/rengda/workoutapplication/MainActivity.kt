package com.rengda.workoutapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap


class MainActivity : AppCompatActivity() {
    lateinit var context:Context
    lateinit var db: SQLiteDatabase
    lateinit var adapter: Adapter
    val workOutClassList= arrayListOf<WorkOutClass>()
     val list= arrayListOf<WorkOut>()
    var sumCount=0
    var sumTime=0
    var searchMap= HashMap<String, String>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.mipmap.list)
        }
        context=this
         db= Util.getWritableDatabase(this);

        //显示
        adapter= Adapter(list)
        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(this)

        //明细里面的删除按钮
        adapter.setOnItemClickListener{
            _,position->
            AlertDialog.Builder(context)
                .setTitle("操作确认")
                .setMessage("是否确定删除该数据?")
                .setNegativeButton("确定"){
                    _,_->
                    val count= db.delete("WorkOut","id=?", arrayOf(list[position].id.toString()))
                    doQuery(searchMap)
                }
                .setPositiveButton("取消"){
                    it,_->
                    it.dismiss()
                }.create().show()
        }


        //抽屉布局里面的nav被选中
        nav_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.info-> {
                    val intent=  Intent(context,BodyInfoActivity::class.java)
                    startActivity(intent)
                }
            }
             true
        }


        //课程下拉框数据源
        addList()
        //查询数据
        doQuery(searchMap)
        //点击添加的话 出现一个弹框
        add.setOnClickListener{
            creatAlertDialog()
        }

        search.setOnClickListener{
          val intent=  Intent(this,SearchActivity::class.java)
          startActivityForResult(intent,1)
        }

    }



    fun creatAlertDialog(){
        val view: View = View.inflate(this,R.layout.dialog_view, null)
        val alertDialog=   AlertDialog.Builder(this).setView(view).create()
        val button= view.findViewById<Button>(R.id.confirm)
        val line1= view.findViewById<LinearLayout>(R.id.line1)
        val titleEdit=view.findViewById<EditText>(R.id.titleEdit)
        val timeEdit=view.findViewById<EditText>(R.id.timeEdit)
        val colorieEdit=view.findViewById<EditText>(R.id.colorieEdit)


        val spinner=view.findViewById<Spinner>(R.id.spinner)
        val spinnerAdapter= SpinnerAdapter<WorkOutClass>(context,workOutClassList,"title",WorkOutClass::class.java)
        spinner.adapter=spinnerAdapter
        //选中事件
        spinner.onItemSelectedListener=object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val spinnerItem=workOutClassList[position] as WorkOutClass
                if (spinnerItem.title=="自定义"){
                    line1.visibility=View.VISIBLE
                }
            }
        }
        val dateEdit=view.findViewById<EditText>(R.id.date)


        var defualtTime=""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current=  LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
             defualtTime = current.format(formatter)
        } else {
            val current=Calendar.getInstance()
           defualtTime= SimpleDateFormat("yyyy-MM-dd").format(current.time)
        }

        dateEdit.setText(defualtTime)



        button.setOnClickListener{//插入一条数据
            val values=ContentValues().apply {
                put("user","陈雯")
                put("date",dateEdit.text.toString())
                val spinnerItem=spinner.selectedItem as WorkOutClass
                if (spinnerItem.title=="自定义"){
                    put("title",titleEdit.text.toString())
                    put("time", if (timeEdit.text.toString()=="") "0" else timeEdit.text.toString() )
                    put("calorie", if (colorieEdit.text.toString()=="") "0" else colorieEdit.text.toString() )
                }else{
                    put("title", spinnerItem.title)
                    put("time", spinnerItem.time)
                    put("calorie",spinnerItem.calorie)
                }


            }
            db.insert("WorkOut",null,values)
            alertDialog.dismiss()
            doQuery(searchMap)
        }

        alertDialog.show()
    }



    @SuppressLint("Range")
    fun doQuery(searchMap:HashMap<String,String>?){
        list.clear()
        sumCount=0
        sumTime=0
        var selection:String?=""
        var selectionArgs= arrayListOf<String>()
        //如果有查询条件
        if (searchMap!=null&&searchMap.size>0){
            //条件拼接
            var i=0
            for (item in searchMap){
                if (item.value!=""){
                    selection=selection+item.key+" and "
                    selectionArgs.add(item.value)
                }
            }
        }
        if (selection==""||selection==null){
            selection=null
        }else{
            selection=selection.substring(0,selection.length-5)
        }
        Log.d("lalla", "doQuery: "+selection+"   "+selectionArgs.toString())
        val cursor=db.query("WorkOut",null,selection,selectionArgs.toTypedArray(),null,null,"date")
           while (cursor.moveToNext()){
               val user=cursor.getString(cursor.getColumnIndex("user"))
               val title=cursor.getString(cursor.getColumnIndex("title"))
               val date=cursor.getString(cursor.getColumnIndex("date"))
               val time=cursor.getInt(cursor.getColumnIndex("time"))
               val colorie=cursor.getInt(cursor.getColumnIndex("calorie"))
               val id=cursor.getInt(cursor.getColumnIndex("id"))
               val workOutItem=WorkOut(id,title,time,colorie,date,user)
               sumCount++;
               sumTime=sumTime+time
               list.add(workOutItem)
           }
            sumCountText.setText("总次数:"+sumCount.toString()+"次")
            sumTimeText.setText("总时长:"+sumTime.toString()+"分钟")
            adapter.notifyDataSetChanged()
        }


    fun addList(){
        workOutClassList.add(WorkOutClass("刘耕宏跟练精简版",60,0))
        workOutClassList.add(WorkOutClass("刘耕宏跟练完整版",90,0))
        workOutClassList.add(WorkOutClass("Focus 25",25,0))
        workOutClassList.add(WorkOutClass("钟丽缇扩胸",7,0))
        workOutClassList.add(WorkOutClass("自定义",0,0))
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1-> if (resultCode== Activity.RESULT_OK){//返回成功
                searchMap=if ( data?.getSerializableExtra("searchMap")!=null) data?.getSerializableExtra("searchMap") as HashMap<String, String> else LinkedHashMap()
                doQuery(searchMap)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }
}