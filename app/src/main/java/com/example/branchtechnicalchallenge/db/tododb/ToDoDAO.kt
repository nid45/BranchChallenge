package com.example.branchtechnicalchallenge.db.tododb

import androidx.room.*
import com.example.branchtechnicalchallenge.data.Lists
import com.example.branchtechnicalchallenge.data.ToDo

@Dao
interface ToDoDAO {
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTodo(todo: ToDo?): Long
    
    @Update
    fun updateToDo(todo: ToDo?)
    
    @Delete
    fun deleteToDo(todo: ToDo?)
    
    @Query("SELECT * FROM todos_table WHERE uid = :id")
    fun getTodo(id: Long): ToDo?

    @Query("SELECT * FROM todos_table WHERE list = :id")
    fun getTodoForList(id: Long): List<ToDo?>
    
//    @get:Query("SELECT * FROM todos_table")
//    val ToDo: MutableList<ToDo>?


}