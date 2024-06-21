package com.example.schedule.repository

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.databinding.ItemLessonBinding


class LessonDiffCallback : DiffUtil.ItemCallback<Lesson>(){
    override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
        return oldItem.uid==newItem.uid
    }

    override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
        return  oldItem == newItem
    }

}
class LessonsViewHolder(private val binding: ItemLessonBinding)
    :RecyclerView.ViewHolder(binding.root) {
    fun bind(context: Context, lesson: Lesson, listener: LessonsAdapter.Listener, user: User) {
        binding.apply {
            nameLesson.text = lesson.name
            timeLesson.text = lesson.time
            if (user.isAdmin){
                buttonEdit.visibility = View.VISIBLE
            }else{
                buttonEdit.visibility = View.GONE
            }
            buttonEdit.setOnClickListener {
                listener.editLesson(lesson)
            }
        }
    }
}

class LessonsAdapter(private val context:Context,private val user:User,
    private val listener: Listener,
):ListAdapter<Lesson, LessonsViewHolder>(LessonDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonsViewHolder {
        val binding = ItemLessonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LessonsViewHolder(binding)
    }
    override fun onBindViewHolder(holder: LessonsViewHolder, position:Int){
        val post = getItem(position)
        holder.bind(context,post, listener, user)
    }

    interface Listener{
        fun editLesson(lesson: Lesson)
    }
}
