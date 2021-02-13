package com.example.branchtechnicalchallenge.todoFragment.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.branchtechnicalchallenge.data.Lists
import com.example.branchtechnicalchallenge.data.ToDo
import com.example.branchtechnicalchallenge.databinding.FragmentTodoBinding
import com.example.branchtechnicalchallenge.db.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext



class TodoViewModel(application: Application, var todoDatabase: Database, var binding: FragmentTodoBinding) : AndroidViewModel(application) {

    val allToDoItems = MutableLiveData<MutableList<ToDo>>()

    init {
        allToDoItems.value = mutableListOf()
    }

    /**
     * Initialize data into to do items from the room database
     *
     * @param  database  room database where we need to pull data from
     * @return  none
     */
    fun initData(database: Database, list: Lists){
        lateinit var temp: MutableList<ToDo>
        runBlocking {
            withContext(Dispatchers.IO) {
                temp = database.todoDAO()?.getTodoForList(list.uid) as MutableList<ToDo>
            }
        }
        allToDoItems.value = temp
    }

    /**
     * delete a to do item from database and viewmodel
     *
     * @param  to do which item must be deleted from viemodel and database
     * @return  none
     */
    fun deleteTodo(todo: ToDo) {
        allToDoItems.value?.remove(todo)
        runBlocking {
            withContext(Dispatchers.IO) {
                todoDatabase.todoDAO()?.deleteToDo(todo)
            }
        }
        binding.todoRecycler.adapter?.notifyDataSetChanged()
    }

    /**
     * update a to do object in viewmodel and database
     *
     * @param  to do  update a to do object in viewmodel and database
     * @param  position  position in list that must be updated
     * @return  none
     */
    fun updateTodo(todo: ToDo, position:Int) {
        runBlocking {
            withContext(Dispatchers.IO) {
                todoDatabase.todoDAO()?.updateToDo(todo)
            }
        }

        allToDoItems.value?.set(position, todo)
        binding.todoRecycler.adapter?.notifyDataSetChanged()
    }

    /**
     * create a to do object in viewmodel and database
     *
     * @param  to do  create a to do object in viewmodel and database
     * @return  none
     */
    fun createTodo(todo: ToDo?) {
        var uid: Long
        runBlocking {
            withContext(Dispatchers.IO) {
                uid = todoDatabase.todoDAO()?.addTodo(todo)!!
            }
        }
        if (todo != null) {
            todo.uid = uid
            allToDoItems.value?.add(todo)
        }
        binding.todoRecycler.adapter?.notifyDataSetChanged()
    }

}