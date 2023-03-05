package com.example.audiorecorderfinalchoice

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TimeCreated {
    fun getTimeCreated(duration: Long): String{
        val now = Date()
        val modified = Date(duration)
        val formatter = SimpleDateFormat("dd.MM.yyyy в hh:MM")
        val formatted =formatter.format(modified)
        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(now.time - duration)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - duration)
        return if(seconds<60){
            "Только что"
        }else if(minutes == 1.toLong()){
            "Минуту назад"
        }else if(minutes in 2..59){
            "$minutes минуты назад"
        } else{
            "$formatted "
        }
    }

}