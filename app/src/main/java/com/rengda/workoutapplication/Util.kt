package com.rengda.workoutapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase


object Util
{
        fun  getWritableDatabase(context: Context): SQLiteDatabase {
            val dbHelper=MyDatabaseHelper(context,"WorkOut.db",3)
             val db=  dbHelper.writableDatabase //创建数据库
            return db
        }

}