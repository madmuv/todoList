package com.madmuv.todolist2.ui.task

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.madmuv.todolist2.R
import com.madmuv.todolist2.databinding.FragmentTaskBinding
import com.madmuv.todolist2.viewmodel.TaskViewModel

class TaskFragment : Fragment() {

    private val viewModel: TaskViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val _binding = FragmentTaskBinding.inflate(layoutInflater)

        _binding.lifecycleOwner = this
        _binding.viewModel = viewModel

        taskAdapter = TaskAdapter(TaskClickListener { taskEntry ->
            findNavController().navigate(TaskFragmentDirections.actionTaskFragmentToUpdateFragment(taskEntry))
        })

        viewModel.getAllTask.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }

        _binding.apply {

            _binding.recyclerView.adapter = taskAdapter

            floatingActionButton.setOnClickListener {
                findNavController().navigate(R.id.action_taskFragment_to_addFragment)
            }
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback( 0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val taskEntry = taskAdapter.currentList[position]
                viewModel.delete(taskEntry)

                Snackbar.make(_binding.root, "Deleted", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.insert(taskEntry)
                    }
                    show()
                }
            }
        }).attachToRecyclerView(_binding.recyclerView)

        setHasOptionsMenu(true)
        hideKeyboard(requireActivity())
        return _binding.root
    }

    private fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusedView = activity.currentFocus
        currentFocusedView.let {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.task_menu, menu)
        val searchItem = menu.findItem(R.id.action_serach)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    runQuery(newText)
                }
                return true
            }
        })
    }

    private fun runQuery(query: String) {
        val searchQuery = "%$query%"
        viewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner, Observer { tasks ->
            taskAdapter.submitList(tasks)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_priority -> viewModel.getAllPriorityTask.observe(viewLifecycleOwner, Observer { tasks ->
                taskAdapter.submitList(tasks)
            })
            R.id.action_delete_all -> deleteAllItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllItem() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete All")
            .setMessage("Are you sure?")
            .setPositiveButton("Yes") { dialog, _ ->
                viewModel.deleteAll()
                dialog.dismiss()
            }.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }

}