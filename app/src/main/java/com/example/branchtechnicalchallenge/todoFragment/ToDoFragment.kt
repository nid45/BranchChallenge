package com.example.branchtechnicalchallenge.todoFragment

import android.app.Application
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.branchtechnicalchallenge.MainActivity.Companion.db
import com.example.branchtechnicalchallenge.R
import com.example.branchtechnicalchallenge.data.Lists
import com.example.branchtechnicalchallenge.data.ToDo
import com.example.branchtechnicalchallenge.databinding.FragmentTodoBinding
import com.example.branchtechnicalchallenge.db.Database
import com.example.branchtechnicalchallenge.helpers.SwipeToDeleteCallback
import com.example.branchtechnicalchallenge.todoFragment.adapter.TodoAdapter
import com.example.branchtechnicalchallenge.todoFragment.adapter.TodoAdapter.Companion.selectedList
import com.example.branchtechnicalchallenge.todoFragment.viewModel.TodoViewModel
import com.example.branchtechnicalchallenge.todoFragment.todoViewModelFactory.TodoViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder


/**
 * A simple [Fragment] subclass for the page that will display the to do items in a list
 */
class ToDoFragment : Fragment() {
    lateinit var todoViewModelFactory: TodoViewModelFactory
    lateinit var viewModel: TodoViewModel
    private lateinit var binding: FragmentTodoBinding
    lateinit var database: Database
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
        this.database = db
        todoViewModelFactory = TodoViewModelFactory(Application(), database, binding)
        viewModel = ViewModelProvider(
                this,
                todoViewModelFactory
        ).get(TodoViewModel::class.java)

        binding.todoTitle.text = list.title

        viewModel.initData(database, list)

        var adapter = context?.let { activity?.let { it1 -> TodoAdapter(viewModel.allToDoItems.value!!, it, viewModel, it1, binding) } }

        binding.todoRecycler.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.todoRecycler.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.todoRecycler.adapter as TodoAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.todoRecycler)




        binding.delete.setOnClickListener {
            for(todo in selectedList ){
                viewModel.deleteTodo(todo.key)
            }
        }


        binding.todoFab.setOnClickListener {
          //  var viewdialog = layoutInflater.inflate(R.layout.edit_todo_dialog, null);
            var complete = false
            val builder = context?.let { it1 -> MaterialAlertDialogBuilder(it1, R.style.ThemeOverlay_App_MaterialAlertDialog) }
            val inflater: LayoutInflater =  requireActivity().layoutInflater

            val viewdialog: View = inflater.inflate(R.layout.edit_todo_dialog, null)
            viewdialog.findViewById<TextView>(R.id.edit_dialog_title).hint = "Title"

            builder!!.setView(viewdialog)


            var dialog = builder.create()
            dialog.show()

            viewdialog.findViewById<TextView>(R.id.cancel).setOnClickListener {
                dialog.dismiss()
            }
            viewdialog.findViewById<RadioButton>(R.id.incomplete).isChecked = true
            context?.let { it1 ->
                viewdialog.findViewById<TextView>(R.id.save).setOnClickListener {
                    if(viewdialog.findViewById<EditText>(R.id.edit_dialog_title).text.toString() == ""){
                        viewdialog.findViewById<EditText>(R.id.edit_dialog_title).error = "List Name is required."
                    }else {
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
            viewdialog.findViewById<EditText>(R.id.edit_dialog_title)?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int,
                                               p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int,
                                           p2: Int, p3: Int) {
                    if (p0.isNullOrBlank()){
                        if (viewdialog.findViewById<EditText>(R.id.edit_dialog_title).text.equals("")) {
                            viewdialog.findViewById<EditText>(R.id.edit_dialog_title).error = "List Name is required."
                        }
                        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                                .isEnabled = false
                    }else{
                        if (viewdialog.findViewById<EditText>(R.id.edit_dialog_title) != null) {
                            viewdialog.findViewById<EditText>(R.id.edit_dialog_title).error = ""
                        }
                        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                                .isEnabled = true
                    }
                }
            })

        }


    }
}