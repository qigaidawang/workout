package com.rengda.workoutapplication

data class WorkOut(val id:Int,val title: String,val time:Int,val calorie:Int,val date:String,val user:String){
}

data class WorkOutClass( val title:String, val time:Int, val calorie:Int){
}

data class BodyDetailInfo(val id:Int,val bodyData:String,val date:String,val unit:String)