package com.example.branchtechnicalchallenge.todoFragment.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.branchtechnicalchallenge.data.ToDo
import com.example.branchtechnicalchallenge.databinding.FragmentTodoBinding
import com.example.branchtechnicalchallenge.db.tododb.TodoDatabase


class TodoViewModel(application: Application, var todoDatabase: TodoDatabase, var binding: FragmentTodoBinding) : AndroidViewModel(application) {

    val allToDoItems = MutableLiveData<MutableList<ToDo>>()

    init {
        allToDoItems.value = mutableListOf()
    }


    fun deleteTodo(todo: ToDo, position: Int) {
        Log.i("here", position.toString())
        allToDoItems.value?.removeAt(position)
        todoDatabase.ToDoDAO()?.deleteToDo(todo)
        binding.todoRecycler.adapter?.notifyDataSetChanged()
    }

    fun updateTodo(todo: ToDo, position:Int) {
        todoDatabase.ToDoDAO()?.updateToDo(todo)
        allToDoItems.value?.set(position, todo)
        binding.todoRecycler.adapter?.notifyDataSetChanged()
    }

    fun createTodo(todo: ToDo?) {
        todoDatabase.ToDoDAO()?.addTodo(todo)
        if (todo != null) {
            allToDoItems.value?.add(todo)
        }
        binding.todoRecycler.adapter?.notifyDataSetChanged()
    }


}