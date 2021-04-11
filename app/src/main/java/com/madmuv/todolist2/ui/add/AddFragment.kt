package com.madmuv.todolist2.ui.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.madmuv.todolist2.R
import com.madmuv.todolist2.databinding.FragmentAddBinding
import com.madmuv.todolist2.db.TaskEntry
import com.madmuv.todolist2.viewmodel.TaskViewModel

class AddFragment : Fragment() {

    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val _binding = FragmentAddBinding.inflate(layoutInflater)

        val itemAdapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.priorities)
        )

        _binding.apply {
            spinner.adapter = itemAdapter
            btnAdd.setOnClickListener {
                if (TextUtils.isEmpty(editTask.text)) {
                    Toast.makeText(requireContext(), "Please, Fill your task", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val title_str = editTask.text.toString()
                val prioritiy = spinner.selectedItemPosition

                val taskEntry = TaskEntry(
                    0,
                    title_str,
                    prioritiy,
                    System.currentTimeMillis()
                )

                viewModel.insert(taskEntry)
                Toast.makeText(requireContext(), "Added", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addFragment_to_taskFragment)
            }
        }

        return _binding.root
    }

}