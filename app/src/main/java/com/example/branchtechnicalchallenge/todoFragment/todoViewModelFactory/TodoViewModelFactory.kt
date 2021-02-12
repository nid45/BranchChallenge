package com.example.branchtechnicalchallenge.todoFragment.todoViewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.branchtechnicalchallenge.databinding.FragmentTodoBinding
import com.example.branchtechnicalchallenge.db.tododb.TodoDatabase
import com.example.branchtechnicalchallenge.listFragment.viewModel.ListsViewModel
import com.example.branchtechnicalchallenge.listFragment.viewModel.TodoViewModel

class TodoViewModelFactory(
        private val application: Application, var todoDatabase: TodoDatabase, var binding: FragmentTodoBinding) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            return TodoViewModel(application, todoDatabase, binding) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
