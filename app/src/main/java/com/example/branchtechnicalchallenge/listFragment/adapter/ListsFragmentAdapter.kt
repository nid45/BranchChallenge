package com.example.branchtechnicalchallenge.listFragment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.branchtechnicalchallenge.R
import com.example.branchtechnicalchallenge.data.Lists
import com.example.branchtechnicalchallenge.databinding.FragmentListsBinding
import com.example.branchtechnicalchallenge.listFragment.viewModel.ListsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*


class ListsFragmentAdapter(var lists: MutableList<Lists>, var contextin: Context, var viewModel: ListsViewModel, var binding: FragmentListsBinding): RecyclerView.Adapter<ListsFragmentAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lists_item_view, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(lists[position], contextin, viewModel, position, binding)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    class ListViewHolder(var view: View): RecyclerView.ViewHolder(view.rootView) {

        @SuppressLint("SimpleDateFormat")
        fun bind(list: Lists, context: Context, viewModel: ListsViewModel, position: Int, binding: FragmentListsBinding) {
            itemView.findViewById<TextView>(R.id.title).text = list.title
            val simpleDateFormat = SimpleDateFormat("MM/dd/yyyy")
            val date = Date(list.timecreated)
            val time: String = simpleDateFormat.format(date)
            itemView.findViewById<TextView>(R.id.time).text = "Created " + time


            itemView.setOnLongClickListener{
                AlertDialog.Builder(context)
                        .setTitle("Are you sure you want to delete this list?")
                        .setPositiveButton("Yes"){ _, _ ->
                            viewModel.deleteLists(list, position)
                            binding.listRecycler.adapter?.notifyDataSetChanged()
                        }
                        .create().show()
                return@setOnLongClickListener true
            }

            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable("list", list)
                view.findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
            }

            itemView.findViewById<TextView>(R.id.edit).setOnClickListener {

                val builder = MaterialAlertDialogBuilder(context)

                // dialog title
                builder.setTitle("Edit List Name")

                // dialog message view
                val constraintLayout = context.let { it1 -> getEditTextLayout(it1) }
                builder.setView(constraintLayout)

                val textInputLayout = constraintLayout.findViewWithTag<TextInputLayout>("textInputLayoutTag")
                val textInputEditText = constraintLayout.findViewWithTag<TextInputEditText>("textInputEditTextTag")

                // alert dialog positive button
                builder.setPositiveButton("Submit"){ _, _ -> //set what should happen when negative button is clicked
                    if (textInputEditText != null) {
                        viewModel.updateList(textInputEditText.text.toString(), position)
                        binding.listRecycler.adapter?.notifyDataSetChanged()
                    }
                }

                // alert dialog other buttons
                builder.setNeutralButton("Cancel",null)

                // set dialog non cancelable
                builder.setCancelable(false)

                // finally, create the alert dialog and show it
                val dialog = builder.create()

                dialog.show()

                // initially disable the positive button
                dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).isEnabled = false

                // edit text text change listener
                textInputEditText?.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int,
                                                   p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int,
                                               p2: Int, p3: Int) {
                        if (p0.isNullOrBlank()){
                            if (textInputLayout != null) {
                                textInputLayout.error = "List Name is required."
                            }
                            dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                                    .isEnabled = false
                        }else{
                            if (textInputLayout != null) {
                                textInputLayout.error = ""
                            }
                            dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                                    .isEnabled = true
                        }
                    }
                })
            }
        }

        // get edit text layout
        private fun getEditTextLayout(context: Context): ConstraintLayout {
            val constraintLayout = ConstraintLayout(context)
            val layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            constraintLayout.layoutParams = layoutParams
            constraintLayout.id = View.generateViewId()

            val textInputLayout = TextInputLayout(context)
            textInputLayout.boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
            layoutParams.setMargins(
                    32,
                    8,
                    32,
                    8
            )
            textInputLayout.layoutParams = layoutParams
            textInputLayout.hint = "Type new list name here"
            textInputLayout.id = View.generateViewId()
            textInputLayout.tag = "textInputLayoutTag"


            val textInputEditText = TextInputEditText(context)
            textInputEditText.id = View.generateViewId()
            textInputEditText.tag = "textInputEditTextTag"

            textInputLayout.addView(textInputEditText)

            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)

            constraintLayout.addView(textInputLayout)
            return constraintLayout
        }

    }



}



