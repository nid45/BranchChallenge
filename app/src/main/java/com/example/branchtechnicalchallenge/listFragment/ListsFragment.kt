package com.example.branchtechnicalchallenge.listFragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.branchtechnicalchallenge.MainActivity.Companion.db
import com.example.branchtechnicalchallenge.R
import com.example.branchtechnicalchallenge.databinding.FragmentListsBinding
import com.example.branchtechnicalchallenge.db.Database
import com.example.branchtechnicalchallenge.helpers.SwipeToDeleteCallback
import com.example.branchtechnicalchallenge.listFragment.adapter.ListsFragmentAdapter
import com.example.branchtechnicalchallenge.listFragment.listsViewModelFactory.ListsViewModelFactory
import com.example.branchtechnicalchallenge.listFragment.viewModel.ListsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


/**
 * A fragment to display the lists that a user has created
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

        //initialize data in viewmodel from the room database
        viewModel.initData(database)

        if (viewModel.lists.value?.size != 0) {
            binding.emptyState.visibility = View.INVISIBLE
        }

        //setting layout manager for recyclerview adapter
        binding.listRecycler.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val adapter = context?.let { ListsFragmentAdapter(viewModel.lists.value!!, it, viewModel, binding, requireActivity()) }
        binding.listRecycler.adapter = adapter


        //swipehandler allows for swipe to delete action on list cards
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.listRecycler.adapter as ListsFragmentAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                if (viewModel.lists.value?.size == 0) {
                    binding.emptyState.visibility = View.VISIBLE
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.listRecycler)

        //floating action button is clicked and a new list dialog is shown
        binding.fab.setOnClickListener {
            openNewListDialog()
        }

        binding.empty.setOnClickListener {
            openNewListDialog()
        }


    }

    fun openNewListDialog(){
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog)

        builder.setTitle("List Name")

        val constraintLayout = context?.let { it1 -> getEditTextLayout(it1) }
        builder.setView(constraintLayout)

        val textInputLayout = constraintLayout?.findViewWithTag<TextInputLayout>("textInputLayoutTag")
        val textInputEditText = constraintLayout?.findViewWithTag<EditText>("textInputEditTextTag")

        // alert dialog positive button
        builder.setPositiveButton("Submit"){ _, _ -> //set what should happen when negative button is clicked
            if (textInputEditText != null) {
                viewModel.createList(textInputEditText.text.toString())
                binding.emptyState.visibility = View.GONE
            }
        }


        builder.setNeutralButton("Cancel") { _, _ -> }

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

//companion object for dialog layout, is in companion object for use in adapter
    companion object {
        // get edit text dialog
        @SuppressLint("UseCompatLoadingForColorStateLists")
        fun getEditTextLayout(context: Context): ConstraintLayout {
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
            textInputLayout.id = View.generateViewId()
            textInputLayout.tag = "textInputLayoutTag"


            val textInputEditText = TextInputEditText(context)
            textInputEditText.id = View.generateViewId()
            textInputEditText.tag = "textInputEditTextTag"
            textInputEditText.setTextColor(Color.WHITE)
            textInputEditText.backgroundTintList = ContextCompat.getColorStateList(context, R.color.white)


            textInputLayout.addView(textInputEditText)

            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)

            constraintLayout.addView(textInputLayout)
            return constraintLayout
        }
    }

}
