package com.example.branchtechnicalchallenge.todoFragment.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.branchtechnicalchallenge.data.ToDo
import com.example.branchtechnicalchallenge.databinding.FragmentTodoBinding
import com.example.branchtechnicalchallenge.db.Database


class TodoViewModel(application: Application, var todoDatabase: Database, var binding: FragmentTodoBinding) : AndroidViewModel(application) {

    val allToDoItems = MutableLiveData<MutableList<ToDo>>()

    init {
        allToDoItems.value = mutableListOf()
    }


    fun deleteTodo(todo: ToDo) {
        allToDoItems.value?.remove(todo)
        todoDatabase.todoDAO()?.deleteToDo(todo)
        binding.todoRecycler.adapter?.notifyDataSetChanged()
    }

    fun updateTodo(todo: ToDo, position:Int) {
        todoDatabase.todoDAO()?.updateToDo(todo)
        allToDoItems.value?.set(position, todo)
        binding.todoRecycler.adapter?.notifyDataSetChanged()
    }

    fun createTodo(todo: ToDo?) {
        var uid = todoDatabase.todoDAO()?.addTodo(todo)
        if (todo != null) {
            todo.uid = uid!!
            allToDoItems.value?.add(todo)
        }
        binding.todoRecycler.adapter?.notifyDataSetChanged()
    }


}