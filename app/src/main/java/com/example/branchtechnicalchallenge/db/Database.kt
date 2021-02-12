package com.example.branchtechnicalchallenge.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.branchtechnicalchallenge.data.Lists
import com.example.branchtechnicalchallenge.data.ToDo
import com.example.branchtechnicalchallenge.db.listsdb.ListsDAO
import com.example.branchtechnicalchallenge.db.tododb.ToDoDAO

@Database(entities = [Lists::class, ToDo::class], version = 1, exportSchema = true)
abstract class Database : RoomDatabase() {
    abstract fun listsDAO(): ListsDAO?
    abstract fun todoDAO(): ToDoDAO?
}
