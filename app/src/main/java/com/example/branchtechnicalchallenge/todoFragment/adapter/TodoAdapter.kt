package com.example.branchtechnicalchallenge.todoFragment.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.branchtechnicalchallenge.R
import com.example.branchtechnicalchallenge.data.ToDo
import com.example.branchtechnicalchallenge.databinding.FragmentTodoBinding
import com.example.branchtechnicalchallenge.todoFragment.viewModel.TodoViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class TodoAdapter(var todos: MutableList<ToDo>,
                  var contextin: Context,
                  var viewModel: TodoViewModel,
                  var activity: Activity,
                  var binding: FragmentTodoBinding ): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    companion object {
        var selectedList: HashMap<ToDo, Int> = HashMap()
    }

    fun removeAt(position: Int) {
        this.viewModel.deleteTodo(todos.get(position))
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item_card, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todos[position], contextin, viewModel, position, activity, binding)
    }

    override fun getItemCount(): Int {
        return todos.size
    }


    class TodoViewHolder(var view: View): RecyclerView.ViewHolder(view.rootView) {



        @SuppressLint("SetTextI18n")
        fun bind(todo: ToDo, context: Context, viewModel: TodoViewModel, position: Int, activity: Activity, binding: FragmentTodoBinding) {
            view.findViewById<TextView>(R.id.title).text = todo.title
            itemView.findViewById<CheckBox>(R.id.isCompleted).isChecked = false
            if(todo.completed){
                itemView.findViewById<TextView>(R.id.status).text = "Completed"
            } else{
                itemView.findViewById<TextView>(R.id.status).text = "Not Completed"

            }


           itemView.findViewById<CheckBox>(R.id.isCompleted).setOnCheckedChangeListener { buttonView, isChecked ->
               selectedList.put(todo, position)
           }

            itemView.setOnClickListener {
                val builder = MaterialAlertDialogBuilder(context,  R.style.ThemeOverlay_App_MaterialAlertDialog)
                val inflater: LayoutInflater =  activity.layoutInflater

                val viewdialog: View = inflater.inflate(R.layout.edit_todo_dialog, null)
                builder.setView(viewdialog)
                var dialog = builder.create()
                dialog.show()
// TODO: 2/12/21 edittext turns blue when selected
                viewdialog.findViewById<TextView>(R.id.edit_dialog_title).text = todo.title
                viewdialog.findViewById<EditText>(R.id.description).text = SpannableStringBuilder(todo.description)
                if (todo.completed) {
                    viewdialog.findViewById<RadioButton>(R.id.completed).isChecked = true
                } else {
                    viewdialog.findViewById<RadioButton>(R.id.incomplete).isChecked = true
                }

                viewdialog.findViewById<TextView>(R.id.save).setOnClickListener {
                    if (viewdialog.findViewById<EditText>(R.id.edit_dialog_title).text.toString() == "") {
                        viewdialog.findViewById<EditText>(R.id.edit_dialog_title).error = "List Name is required."
                    } else {
                        todo.completed = viewdialog.findViewById<RadioButton>(R.id.completed).isChecked == true
                        todo.title = viewdialog.findViewById<TextView>(R.id.edit_dialog_title).text.toString()
                        todo.description = viewdialog.findViewById<EditText>(R.id.description).text.toString()
                        viewModel.updateTodo(todo, position)
                        binding.todoRecycler.adapter?.notifyDataSetChanged()
                        dialog.dismiss()
                    }
                }

                viewdialog.findViewById<TextView>(R.id.cancel).setOnClickListener {
                    dialog.dismiss()
                }


                var titleEdit = viewdialog.findViewById<EditText>(R.id.edit_dialog_title)
                titleEdit.findViewById<EditText>(R.id.edit_dialog_title)?.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int,
                                                   p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int,
                                               p2: Int, p3: Int) {
                        if (p0.isNullOrBlank()){
                            if (titleEdit!= null) {
                                titleEdit.error = "List Name is required."
                            }
                            viewdialog.findViewById<TextView>(R.id.save).isClickable = false
                        }else{
                            if (titleEdit != null) {
                                titleEdit.error = ""
                            }
                            viewdialog.findViewById<TextView>(R.id.save).isClickable = true

                        }
                    }
                })
            }



            itemView.setOnLongClickListener {
                if (!todo.completed) {
                    MaterialAlertDialogBuilder(context,  R.style.ThemeOverlay_App_MaterialAlertDialog)
                            .setTitle("Mark item as completed")
                            .setPositiveButton("Yes") { _, _ ->
                                todo.completed = true
                                viewModel.updateTodo(todo, position)
                                binding.todoRecycler.adapter?.notifyDataSetChanged()

                            }.setNegativeButton("No") { _, _ ->
                            }
                            .create().show()
                }else{
                    AlertDialog.Builder(context)
                            .setTitle("Mark item as incomplete")
                            .setPositiveButton("Yes") { _, _ ->
                                todo.completed = false
                                viewModel.updateTodo(todo, position)
                                binding.todoRecycler.adapter?.notifyDataSetChanged()
                            }.setNegativeButton("No") { _, _ ->
                            }
                            .create().show()
                }
                return@setOnLongClickListener true
            }
        }
    }
}