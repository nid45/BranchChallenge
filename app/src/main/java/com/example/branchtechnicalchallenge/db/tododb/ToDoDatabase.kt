package com.example.branchtechnicalchallenge.db.tododb


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.branchtechnicalchallenge.data.Lists
import com.example.branchtechnicalchallenge.data.ToDo
import com.example.branchtechnicalchallenge.db.listsdb.ListsDAO

@Database(entities = [ToDo::class], version = 1, exportSchema = true)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun ToDoDAO(): ToDoDAO?
}
