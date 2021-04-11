package com.madmuv.todolist2.ui.task

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
        return _binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.task_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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