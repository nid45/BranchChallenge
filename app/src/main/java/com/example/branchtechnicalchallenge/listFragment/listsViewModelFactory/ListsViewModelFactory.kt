package com.example.branchtechnicalchallenge.listFragment.listsViewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.branchtechnicalchallenge.db.Database
import com.example.branchtechnicalchallenge.databinding.FragmentListsBinding
import com.example.branchtechnicalchallenge.listFragment.viewModel.ListsViewModel

class ListsViewModelFactory(
        private val application: Application, val todoDatabase: Database, val binding: FragmentListsBinding) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListsViewModel::class.java)) {
            return ListsViewModel(application, todoDatabase, binding) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
