package com.example.branchtechnicalchallenge.db.listsdb

import androidx.room.*
import com.example.branchtechnicalchallenge.data.Lists

@Dao
interface ListsDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addList(list: Lists?)

    @Update
    fun updateLists(list: Lists?)

    @Delete
    fun deleteLists(list: Lists?)

    @Query("SELECT * FROM lists_table WHERE uid = :id")
    fun getList(id: Long): Lists?

    @get:Query("SELECT * FROM lists_table")
    val lists: List<Lists>?

}