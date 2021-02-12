package com.example.branchtechnicalchallenge.listFragment

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.branchtechnicalchallenge.MainActivity.Companion.db
import com.example.branchtechnicalchallenge.R
import com.example.branchtechnicalchallenge.data.Lists
import com.example.branchtechnicalchallenge.databinding.FragmentListsBinding
import com.example.branchtechnicalchallenge.db.Database
import com.example.branchtechnicalchallenge.listFragment.adapter.ListsFragmentAdapter
import com.example.branchtechnicalchallenge.listFragment.listsViewModelFactory.ListsViewModelFactory
import com.example.branchtechnicalchallenge.listFragment.viewModel.ListsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */


class ListsFragment : Fragment() {
    lateinit var ListsViewModelFactory: ListsViewModelFactory
    lateinit var viewModel: ListsViewModel
    private lateinit var binding: FragmentListsBinding
    lateinit var database: Database

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lists, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(
                        true
                ) {
                    override fun handleOnBackPressed() {}
                })

        //create the viewmodel that will hold the data

        this.database = db
        ListsViewModelFactory = ListsViewModelFactory(Application(), database, binding)
        viewModel = ViewModelProvider(
                this,
                ListsViewModelFactory
        ).get(ListsViewModel::class.java)


        viewModel.lists.value = (database.listsDAO()?.lists as MutableList<Lists>)


        binding.listRecycler.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val adapter = context?.let { ListsFragmentAdapter(viewModel.lists.value!!, it, viewModel, binding) }
        binding.listRecycler.adapter = adapter


        binding.fab.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(requireContext())

            // dialog title
            builder.setTitle("List Name")

            // dialog message view
            val constraintLayout = context?.let { it1 -> getEditTextLayout(it1) }
            builder.setView(constraintLayout)

            val textInputLayout = constraintLayout?.findViewWithTag<TextInputLayout>("textInputLayoutTag")
            val textInputEditText = constraintLayout?.findViewWithTag<TextInputEditText>("textInputEditTextTag")

            // alert dialog positive button
            builder.setPositiveButton("Submit"){ _, _ -> //set what should happen when negative button is clicked
                if (textInputEditText != null) {
                    viewModel.createList(textInputEditText.text.toString())
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
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

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
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                .isEnabled = false
                    }else{
                        if (textInputLayout != null) {
                            textInputLayout.error = ""
                        }
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
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
    textInputLayout.hint = "Type list name here"
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

