package com.example.branchtechnicalchallenge.listFragment.viewModel

import android.app.Application
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.branchtechnicalchallenge.data.Lists
import com.example.branchtechnicalchallenge.databinding.FragmentListsBinding
import com.example.branchtechnicalchallenge.db.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class ListsViewModel(application: Application, var todoDatabase: Database, var binding: FragmentListsBinding) :
    AndroidViewModel(application) {
    var lists = MutableLiveData<MutableList<Lists>>()

    init {
        lists.value = mutableListOf()
    }

    fun initData(database: Database){
        lateinit var temp: MutableList<Lists>
        runBlocking {
             withContext(Dispatchers.IO) {
                 temp = database.listsDAO()?.lists as MutableList<Lists>
             }
        }
        lists.value = temp
    }

    fun deleteLists(list: Lists, position: Int) {
        lists.value?.removeAt(position)
        runBlocking {
            withContext(Dispatchers.IO) {
                todoDatabase.listsDAO()?.deleteLists(list)
            }
        }
        binding.listRecycler.adapter?.notifyDataSetChanged()
    }

    fun updateList(title: String, position:Int) {
        val list: Lists? = lists.value?.get(position)
        if (list != null) {
            list.title = title
        }
        runBlocking {
            withContext(Dispatchers.IO) {
                todoDatabase.listsDAO()?.updateLists(list)
            }
        }
        if (list != null) {
            lists.value?.set(position, list)
        }
        binding.listRecycler.adapter?.notifyDataSetChanged()
    }

    fun createList(title: String) {
        var uid: Long
        val list = Lists(title, System.currentTimeMillis())
        runBlocking {
            withContext(Dispatchers.IO) {
                uid = todoDatabase.listsDAO()?.addList(list)!!
            }
        }
        list.uid = uid
        lists.value?.add(list)
        binding.listRecycler.adapter?.notifyDataSetChanged()
    }


}


