package com.example.branchtechnicalchallenge.db.listsdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.branchtechnicalchallenge.data.Lists
import com.example.branchtechnicalchallenge.db.listsdb.ListsDAO

@Database(entities = [Lists::class], version = 1, exportSchema = true)
abstract class ListsDatabase : RoomDatabase() {
    abstract fun listsDAO(): ListsDAO?
}
