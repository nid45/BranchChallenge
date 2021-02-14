package com.example.branchtechnicalchallenge.todoFragment

import android.app.Application
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
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


        //title of page is the name of the list
        binding.todoTitle.text = list.title

        viewModel.initData(database, list)

        if(viewModel.allToDoItems.value?.size != 0){
            binding.emptyState.visibility = View.INVISIBLE
        }

        val adapter = context?.let { activity?.let { it1 -> TodoAdapter(viewModel.allToDoItems.value!!, it, viewModel, it1, binding) } }

        binding.todoRecycler.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.todoRecycler.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.todoRecycler.adapter as TodoAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                if(viewModel.allToDoItems.value?.size == 0){
                    binding.emptyState.visibility = View.VISIBLE
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.todoRecycler)


        binding.todoFab.setOnClickListener {
            createNewToDo()
        }

        binding.emptyState.setOnClickListener {
            createNewToDo()
        }
    }
    private fun createNewToDo(){
        //  var viewdialog = layoutInflater.inflate(R.layout.edit_todo_dialog, null);
        var complete = false
        val builder = context?.let { it1 -> MaterialAlertDialogBuilder(it1, R.style.ThemeOverlay_App_MaterialAlertDialog) }
        val inflater: LayoutInflater =  requireActivity().layoutInflater

        val viewdialog: View = inflater.inflate(R.layout.edit_todo_dialog, null)
        viewdialog.findViewById<TextView>(R.id.edit_dialog_title).hint = "Title"

        builder!!.setView(viewdialog)

        builder.setNeutralButton("Cancel") { _, _ -> }

        context?.let {
            builder.setPositiveButton("Save") { _, _ ->
                if(viewdialog.findViewById<EditText>(R.id.edit_dialog_title).text.toString() == ""){
                    viewdialog.findViewById<EditText>(R.id.edit_dialog_title).error = "List Name is required."
                }else {
                    val desc = viewdialog.findViewById<EditText>(R.id.description).text.toString()
                    val title = viewdialog.findViewById<EditText>(R.id.edit_dialog_title).text.toString()
                    val todo = ToDo(title, desc, System.currentTimeMillis(), list.uid, complete)
                    viewModel.createTodo(todo)
                    if(viewModel.allToDoItems.value?.size != 0){
                        binding.emptyState.visibility = View.GONE
                    }
                    view?.findViewById<RecyclerView>(R.id.todo_recycler)?.adapter?.notifyDataSetChanged()
                }
            }

        }

        val dialog = builder.create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

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
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

                }else{
                    viewdialog.findViewById<EditText>(R.id.edit_dialog_title).error = null
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true

                }
            }
        })
    }
}