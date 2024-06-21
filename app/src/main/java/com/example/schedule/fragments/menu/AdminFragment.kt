package com.example.schedule.fragments.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.schedule.databinding.FragmentAdminBinding
import com.example.schedule.repository.DataViewModel
import com.example.schedule.repository.Day
import com.example.schedule.repository.Lesson


private const val ARG_UID_LESSON = "uidLesson"
private const val ARG_UID_DAY = "nameDay"
private const val ARG_IS_NEW = "isNew"

class AdminFragment : Fragment() {
    private lateinit var binding: FragmentAdminBinding
    private val viewModel: DataViewModel by activityViewModels()

    private var uidLesson: String? = null
    private var uidDay: String? = null
    private var isNew: Boolean? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uidLesson = it.getString(ARG_UID_LESSON)
            uidDay = it.getString(ARG_UID_DAY)
            isNew= it.getBoolean(ARG_IS_NEW)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            var lesson = Lesson()
            viewModel.data.value!!.week.forEach {itDay ->
                if (itDay.name == uidDay)
                    itDay.lesson.forEach{
                        if (it.uid == uidLesson)
                            lesson = it
                    }
            }
            editTextName.setText(lesson.name)
            editTextTime.setText(lesson.time)
            binding.buttonAdd.setOnClickListener {
                val name = editTextName.text.toString().trim()
                val time = editTextTime.text.toString().trim()


                if ((name.isEmpty() || time.isEmpty()) && !(name.isEmpty() && time.isEmpty())) {
                    Toast.makeText(requireContext(), "Заполните все поля или для удаления не заполнять ничего", Toast.LENGTH_SHORT).show()

                    return@setOnClickListener
                }
                viewModel.editLesson(lesson.copy(name = name, time = time), uidDay!!)
                it.findNavController().popBackStack()
            }
        }

    }

    companion object {
        @JvmStatic
        fun setUidBook(uidLesson: String, uidDay:String, isNew:Boolean): Bundle {
            return bundleOf(Pair(ARG_UID_LESSON,uidLesson),Pair(ARG_UID_DAY,uidDay),Pair(ARG_IS_NEW,isNew))
        }
    }
}