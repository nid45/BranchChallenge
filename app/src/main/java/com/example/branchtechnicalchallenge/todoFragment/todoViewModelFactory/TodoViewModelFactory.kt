package com.example.branchtechnicalchallenge.todoFragment.todoViewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.branchtechnicalchallenge.databinding.FragmentTodoBinding
import com.example.branchtechnicalchallenge.db.Database
import com.example.branchtechnicalchallenge.todoFragment.viewModel.TodoViewModel

class TodoViewModelFactory(
        private val application: Application, var todoDatabase: Database, var binding: FragmentTodoBinding) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            return TodoViewModel(application, todoDatabase, binding) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
