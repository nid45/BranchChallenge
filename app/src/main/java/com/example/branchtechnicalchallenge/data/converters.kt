package com.example.branchtechnicalchallenge.data

import androidx.room.TypeConverter
import com.google.gson.Gson

class MyTypeConverters {

    @TypeConverter
    fun todoToString(app: MutableList<ToDo>): String = Gson().toJson(app)

    @TypeConverter
    fun stringToTodo(string: String): MutableList<ToDo> = Gson().fromJson<MutableList<ToDo>>(string, ToDo::class.java)

}