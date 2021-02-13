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
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.branchtechnicalchallenge.R
import com.example.branchtechnicalchallenge.data.ToDo
import com.example.branchtechnicalchallenge.databinding.FragmentTodoBinding
import com.example.branchtechnicalchallenge.todoFragment.viewModel.TodoViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder



//adapter fr recyclerview that displays to do items
class TodoAdapter(var todos: MutableList<ToDo>,
                  var contextin: Context,
                  var viewModel: TodoViewModel,
                  var activity: Activity,
                  var binding: FragmentTodoBinding ): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    //remove item at specific position for swipe delete
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

//to do viewholder class
    class TodoViewHolder(var view: View): RecyclerView.ViewHolder(view.rootView) {

        @SuppressLint("SetTextI18n")
        fun bind(todo: ToDo, context: Context, viewModel: TodoViewModel, position: Int, activity: Activity, binding: FragmentTodoBinding) {
            view.findViewById<TextView>(R.id.title).text = todo.title
            if(todo.completed) {
                itemView.findViewById<CheckBox>(R.id.isCompleted).isChecked = true
            }

            //completed text based on whether to do object is labelled complete
            if(todo.completed){
                itemView.findViewById<TextView>(R.id.status).text = "Completed"
            } else{
                itemView.findViewById<TextView>(R.id.status).text = "Not Completed"

            }


            //change in checkbox denotes whether the to do has been labelled complete
            itemView.findViewById<CheckBox>(R.id.isCompleted).setOnCheckedChangeListener { _, _ ->
                todo.completed = itemView.findViewById<CheckBox>(R.id.isCompleted).isChecked
                binding.todoRecycler.adapter?.notifyDataSetChanged()
            }


            //allow user to edit a to do item when they click on the card
            itemView.setOnClickListener {
                val builder = MaterialAlertDialogBuilder(context,  R.style.ThemeOverlay_App_MaterialAlertDialog)
                val inflater: LayoutInflater =  activity.layoutInflater

                val viewdialog: View = inflater.inflate(R.layout.edit_todo_dialog, null)
                builder.setView(viewdialog)

                builder.setPositiveButton("Save") { _, _ ->
                    if (viewdialog.findViewById<EditText>(R.id.edit_dialog_title).text.toString() == "") {
                        viewdialog.findViewById<EditText>(R.id.edit_dialog_title).error = "List Name is required."
                    } else {
                        todo.title = viewdialog.findViewById<TextView>(R.id.edit_dialog_title).text.toString()
                        todo.description = viewdialog.findViewById<EditText>(R.id.description).text.toString()
                        viewModel.updateTodo(todo, position)
                        binding.todoRecycler.adapter?.notifyDataSetChanged()
                    }
                }

                builder.setNeutralButton("Cancel") { _, _ -> }

                val dialog = builder.create()
                dialog.show()
                viewdialog.findViewById<TextView>(R.id.edit_dialog_title).text = todo.title
                viewdialog.findViewById<EditText>(R.id.description).text = SpannableStringBuilder(todo.description)


                //show error message saying that a title is required and make the submit unclickable until there is text
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