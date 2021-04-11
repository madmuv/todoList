package com.madmuv.todolist2.ui.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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

        taskAdapter = TaskAdapter()

        viewModel.getAllTask.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }

        _binding.apply {

            _binding.recyclerView.adapter = taskAdapter

            floatingActionButton.setOnClickListener {
                findNavController().navigate(R.id.action_taskFragment_to_addFragment)
            }
        }
        return _binding.root
    }

}