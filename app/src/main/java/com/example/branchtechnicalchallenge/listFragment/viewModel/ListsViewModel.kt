package com.example.branchtechnicalchallenge.listFragment.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.branchtechnicalchallenge.data.Lists
import com.example.branchtechnicalchallenge.databinding.FragmentListsBinding
import com.example.branchtechnicalchallenge.db.Database


class ListsViewModel(application: Application, var todoDatabase: Database, var binding: FragmentListsBinding) :
    AndroidViewModel(application) {
    var lists = MutableLiveData<MutableList<Lists>>()

    init {
        lists.value = mutableListOf()
    }


    fun deleteLists(list: Lists, position: Int) {
        lists.value?.removeAt(position)
        todoDatabase.listsDAO()?.deleteLists(list)
        binding.listRecycler.adapter?.notifyDataSetChanged()
    }

    fun updateList(title: String, position:Int) {
        val list: Lists? = lists.value?.get(position)
        if (list != null) {
            list.title = title
        }
        todoDatabase.listsDAO()?.updateLists(list)
        if (list != null) {
            lists.value?.set(position, list)
        }
        binding.listRecycler.adapter?.notifyDataSetChanged()
    }

    fun createList(title: String) {
        val list = Lists(title, System.currentTimeMillis())
        val uid = todoDatabase.listsDAO()?.addList(list)
        list.uid = uid!!
        lists.value?.add(list)
        binding.listRecycler.adapter?.notifyDataSetChanged()
    }


}


