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

    fun initData(database: Database, list: Lists){
        lateinit var temp: MutableList<ToDo>
        runBlocking {
            withContext(Dispatchers.IO) {
                temp = database.todoDAO()?.getTodoForList(list.uid) as MutableList<ToDo>
            }
        }
        allToDoItems.value = temp
    }

    fun deleteTodo(todo: ToDo) {
        allToDoItems.value?.remove(todo)
        runBlocking {
            withContext(Dispatchers.IO) {
                todoDatabase.todoDAO()?.deleteToDo(todo)
            }
        }
        binding.todoRecycler.adapter?.notifyDataSetChanged()
    }

    fun updateTodo(todo: ToDo, position:Int) {
        runBlocking {
            withContext(Dispatchers.IO) {
                todoDatabase.todoDAO()?.updateToDo(todo)
            }
        }

        allToDoItems.value?.set(position, todo)
        binding.todoRecycler.adapter?.notifyDataSetChanged()
    }

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