package com.example.branchtechnicalchallenge.listFragment.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.branchtechnicalchallenge.data.Lists
import com.example.branchtechnicalchallenge.databinding.FragmentListsBinding
import com.example.branchtechnicalchallenge.db.listsdb.ListsDatabase


class ListsViewModel(application: Application, var todoDatabase: ListsDatabase, var binding: FragmentListsBinding) :
    AndroidViewModel(application) {
    var lists = MutableLiveData<MutableList<Lists>>()

    init {
        lists.value = mutableListOf()
    }


    fun deleteLists(list: Lists, position: Int) {
        Log.i("here", position.toString())
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
        var list = Lists(title, System.currentTimeMillis())
        todoDatabase.listsDAO()?.addList(list)
        lists.value?.add(list)
        binding.listRecycler.adapter?.notifyDataSetChanged()
    }


}


