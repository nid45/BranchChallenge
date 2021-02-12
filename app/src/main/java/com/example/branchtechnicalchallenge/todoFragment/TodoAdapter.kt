package com.example.branchtechnicalchallenge.todoFragment

import android.app.Activity
import android.content.Context
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.branchtechnicalchallenge.R
import com.example.branchtechnicalchallenge.data.ToDo
import com.example.branchtechnicalchallenge.databinding.FragmentTodoBinding
import com.example.branchtechnicalchallenge.listFragment.viewModel.TodoViewModel


class TodoAdapter(var todos: MutableList<ToDo>,
                  var contextin: Context,
                  var viewModel: TodoViewModel,
                  var activity: Activity,
                  var binding: FragmentTodoBinding ): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

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

        fun bind(todo: ToDo, context: Context, viewModel: TodoViewModel, position: Int, activity: Activity, binding: FragmentTodoBinding) {
            view.findViewById<TextView>(R.id.title).text = todo.title
            if(todo.completed){
                itemView.findViewById<TextView>(R.id.status).text = "Completed"
            } else{
                itemView.findViewById<TextView>(R.id.status).text = "Not Completed"

            }


            itemView.setOnClickListener {
                val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
                val inflater: LayoutInflater =  activity.layoutInflater

                val viewdialog: View = inflater.inflate(R.layout.edit_todo_dialog, null)
                builder.setView(viewdialog)
                var dialog = builder.create()
                dialog.show()

                viewdialog.findViewById<TextView>(R.id.edit_dialog_title).text = todo.title
                viewdialog.findViewById<EditText>(R.id.description).text = SpannableStringBuilder(todo.description)
                if (todo.completed) {
                    viewdialog.findViewById<RadioButton>(R.id.completed).isChecked = true
                } else {
                    viewdialog.findViewById<RadioButton>(R.id.incomplete).isChecked = true
                }

                viewdialog.findViewById<TextView>(R.id.save).setOnClickListener {
                    todo.completed = viewdialog.findViewById<RadioButton>(R.id.completed).isChecked == true
                    todo.title = viewdialog.findViewById<TextView>(R.id.edit_dialog_title).text.toString()
                    todo.description = viewdialog.findViewById<EditText>(R.id.description).text.toString()
                    viewModel.updateTodo(todo, position)
                    binding.todoRecycler.adapter?.notifyDataSetChanged()
                    dialog.dismiss()
                }
            }



            itemView.setOnLongClickListener {
                if (!todo.completed) {
                    val alert = AlertDialog.Builder(context)
                            .setTitle("Mark item as completed")
                            .setPositiveButton("Yes") { _, _ ->
                                todo.completed = true
                                viewModel.updateTodo(todo, position)
                                view.findViewById<RecyclerView>(R.id.todo_recycler).adapter?.notifyDataSetChanged()

                            }.setNegativeButton("No") { _, _ ->
                            }
                            .create().show()
                }else{
                    val alert = AlertDialog.Builder(context)
                            .setTitle("Mark item as incomplete")
                            .setPositiveButton("Yes") { _, _ ->
                                todo.completed = false
                                viewModel.updateTodo(todo, position)
                                view.findViewById<RecyclerView>(R.id.todo_recycler).adapter?.notifyDataSetChanged()
                            }.setNegativeButton("No") { _, _ ->
                            }
                            .create().show()
                }
                return@setOnLongClickListener true

            }
        }
    }
}