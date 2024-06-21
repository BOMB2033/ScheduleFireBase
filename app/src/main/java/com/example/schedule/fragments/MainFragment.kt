package com.example.schedule.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.schedule.R
import com.example.schedule.databinding.FragmentLoginBinding
import com.example.schedule.databinding.FragmentMainBinding
import com.example.schedule.fragments.menu.AdminFragment
import com.example.schedule.repository.DataViewModel
import com.example.schedule.repository.Day
import com.example.schedule.repository.DaysAdapter
import com.example.schedule.repository.Lesson
import com.example.schedule.repository.LessonsAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val viewModel:DataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        waitingLoad()

        with(binding){
            buttonBack.setOnClickListener {
                isDay = true
                waitingLoad()
            }
            buttonAddLesson.setOnClickListener {
                it.findNavController().navigate(R.id.action_mainFragment_to_adminFragment, AdminFragment.setUidBook("", selectDay.name, true))
            }
        }

    }
    private var selectDay:Day = Day()
    private var isDay:Boolean = true
    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun waitingLoad(){
        viewLifecycleOwner.lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            while (viewModel.getUser().uid == "")
                delay(1000)
            val adapterDay = DaysAdapter(requireContext(),viewModel.getUser(),object :DaysAdapter.Listener{
                override fun editDay(day: Day) {

                }

                override fun selectDay(day: Day) {
                    selectDay = day
                    isDay = false
                    waitingLoad()
                }
            })
            val adapterLesson = LessonsAdapter(requireContext(),viewModel.getUser(),object :LessonsAdapter.Listener{
                override fun editLesson(lesson: Lesson) {
                    binding.root.findNavController().navigate(R.id.action_mainFragment_to_adminFragment, AdminFragment.setUidBook(lesson.uid, selectDay.name, false))
                }
            })
            if (viewModel.getUser().isAdmin && !isDay)
                binding.buttonAddLesson.visibility = View.VISIBLE
            else
                binding.buttonAddLesson.visibility = View.GONE

            if (isDay){
                binding.buttonBack.visibility = View.GONE
                binding.recyclerView.adapter = adapterDay
                viewModel.data.observe(viewLifecycleOwner){
                    adapterDay.submitList(it.week)
                }
            }
            else {
                binding.buttonBack.visibility = View.VISIBLE

                binding.recyclerView.adapter = adapterLesson
                viewModel.data.observe(viewLifecycleOwner){
                    adapterLesson.submitList(it.week.first { day -> day.name == selectDay.name }.lesson)
                }
            }


            binding.progressBar.visibility = View.GONE
        }
    }
}