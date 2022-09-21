package com.rengda.workoutapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_body_info.*


class BodyInfoActivity : AppCompatActivity() {
    private lateinit var context:Context
    private lateinit var db: SQLiteDatabase
    private  var typeList= arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_info)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
        db= Util.getWritableDatabase(this)
        context=this;
        typeList.add("Weight")
        typeList.add("BFR")

        //打开页面
        BFRArrow.setOnClickListener {
            var intent= Intent(context,BodyInfoDetailActivity::class.java)
            intent.putExtra("type","BFR")
            startActivity(intent)
        }

        //打开页面
        weightArrow.setOnClickListener {
            var intent= Intent(context,BodyInfoDetailActivity::class.java)
            intent.putExtra("type","Weight")
            startActivity(intent)
        }

        doQuery()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->finish()
        }
        return true
    }


    @SuppressLint("Range")
    fun doQuery(){
        for (item in typeList){
            val cursor=db.query(item,null,null,null,null,null,"date desc")
            var i=0
            var currentBodyData=0.0 //第一个是现在的
            var bodyData=0.0
            while (i<2){ //只拿前面两条
                if (cursor.moveToNext()){
                    bodyData=cursor.getString(cursor.getColumnIndex(item)).toDouble()
                    if (item=="Weight"){
                         if (i==1){
                             if (bodyData<currentBodyData){
                                 WeightChange.setText("升高")
                                 //WeightChange.setBackgroundColor(R.color.Red)
                             }else{
                                 WeightChange.setText("降低")
                                 //WeightChange.setBackgroundColor(getColor(R.color.black))
                             }
                             lastWeight.setText(bodyData.toString()+"kg")
                             Weight.setText(currentBodyData.toString()+"kg")
                             currentWeight.setText(currentBodyData.toString()+"kg")
                             currentWeightBig.setText(currentBodyData.toString()+"kg")
                         }

                    }else if (item=="BFR"){
                        if (i==1) {
                            if (bodyData < currentBodyData) {
                                BFRChange.setText("升高")
                                //WeightChange.setBackgroundColor(R.color.Red)
                            } else {
                                BFRChange.setText("降低")
                                //WeightChange.setBackgroundColor(getColor(R.color.black))
                            }
                            BFR.setText(currentBodyData.toString()+"%")
                        }
                    }

                    currentBodyData=cursor.getString(cursor.getColumnIndex(item)).toDouble()
                    i++
                }
            }


        }


    }
}