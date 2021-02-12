package com.example.branchtechnicalchallenge.todoFragment

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.branchtechnicalchallenge.R
import com.example.branchtechnicalchallenge.data.Lists
import com.example.branchtechnicalchallenge.data.ToDo
import com.example.branchtechnicalchallenge.databinding.FragmentTodoBinding
import com.example.branchtechnicalchallenge.db.tododb.TodoDatabase
import com.example.branchtechnicalchallenge.listFragment.viewModel.TodoViewModel
import com.example.branchtechnicalchallenge.todoFragment.todoViewModelFactory.TodoViewModelFactory


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ToDoFragment : Fragment() {
    lateinit var todoViewModelFactory: TodoViewModelFactory
    lateinit var viewModel: TodoViewModel
    private lateinit var binding: FragmentTodoBinding
    lateinit var todoDatabase: TodoDatabase
    lateinit var list: Lists

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_todo, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState
        )
        val bundle = arguments
        list = (bundle!!.getSerializable("list") as Lists?)!!
        todoDatabase = Room.databaseBuilder(requireActivity().applicationContext , TodoDatabase::
        class.java, "todoDB").allowMainThreadQueries().build()
        todoViewModelFactory = TodoViewModelFactory(Application(), todoDatabase, binding)
        viewModel = ViewModelProvider(
                this,
                todoViewModelFactory
        ).get(TodoViewModel::class.java)

        binding.todoTitle.text = list.title

        viewModel.allToDoItems.value = (todoDatabase.ToDoDAO()?.getTodoForList(list.uid) as MutableList<ToDo>)

        var adapter = context?.let { activity?.let { it1 -> TodoAdapter(viewModel.allToDoItems.value!!, it, viewModel, it1, binding) } }

        binding.todoRecycler.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.todoRecycler.adapter = adapter




        binding.todoFab.setOnClickListener {
          //  var viewdialog = layoutInflater.inflate(R.layout.edit_todo_dialog, null);
            var complete = false
            val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
            val inflater: LayoutInflater =  requireActivity().layoutInflater

            val viewdialog: View = inflater.inflate(R.layout.edit_todo_dialog, null)
            builder.setView(viewdialog)
            var dialog = builder.create()
            dialog.show()
            viewdialog.findViewById<RadioButton>(R.id.incomplete).isChecked = true
            val alert = context?.let { it1 ->
                var save = viewdialog.findViewById<TextView>(R.id.save).setOnClickListener {
                    viewdialog.findViewById<RadioGroup>(R.id.radio_group).setOnCheckedChangeListener { group, checkedId -> // checkedId is the RadioButton selected
                        when (checkedId) {
                            R.id.completed -> {
                                complete = true
                            }
                            R.id.incomplete -> {
                                complete = false
                            }
                        }
                    }
                    var desc = viewdialog.findViewById<EditText>(R.id.description).text.toString()
                    var title = viewdialog.findViewById<EditText>(R.id.edit_dialog_title).text.toString()
                    var todo = ToDo(title, desc, System.currentTimeMillis(), list.uid, complete)
                    viewModel.createTodo(todo)
                    view.findViewById<RecyclerView>(R.id.todo_recycler).adapter?.notifyDataSetChanged()
                    dialog.dismiss()
                }

            }

        }


    }
}