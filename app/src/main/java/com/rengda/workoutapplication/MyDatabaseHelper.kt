package com.rengda.workoutapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(context: Context, name:String,version:Int)
    :SQLiteOpenHelper(context,name,null,version) {

    private val creatWorkOut="create table WorkOut("+
            "id integer primary key autoincrement,"+ //唯一标识
            "user text,"+//用户
            "title text,"+//运动名称
            "time integer,"+//课程时长
            "calorie integer,"+//卡路里
            "date text)"//运动的日期



    private val creatWeight="create table Weight("+
            "id integer primary key autoincrement,"+ //唯一标识
            "user text,"+//用户
            "Weight real,"+//体重
            "date text)"//那一天的数据

    private val creatBFR="create table BFR("+
            "id integer primary key autoincrement,"+ //唯一标识
            "user text,"+//用户
            "BFR real,"+//体重
            "date text)"//那一天的数据

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(creatWorkOut)
        db?.execSQL(creatWeight)
        db?.execSQL(creatBFR)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion==2){
            db?.execSQL(creatWeight)
            db?.execSQL(creatBFR)
        }

    }


}