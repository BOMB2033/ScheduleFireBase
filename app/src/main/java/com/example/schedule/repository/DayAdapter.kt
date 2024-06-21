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
import com.example.schedule.databinding.ItemDayBinding


class DayDiffCallback : DiffUtil.ItemCallback<Day>(){
    override fun areItemsTheSame(oldItem: Day, newItem: Day): Boolean {
        return oldItem.name==newItem.name
    }

    override fun areContentsTheSame(oldItem: Day, newItem: Day): Boolean {
        return  oldItem == newItem
    }

}
class DaysViewHolder(private val binding: ItemDayBinding)
    :RecyclerView.ViewHolder(binding.root) {
    fun bind(context: Context, day: Day, listener: DaysAdapter.Listener, user: User) {
        binding.apply {
            nameDay.text = day.name
            root.setOnClickListener {
                listener.selectDay(day)
            }
        }
    }
}

class DaysAdapter(private val context:Context,private val user:User,
    private val listener: Listener,
):ListAdapter<Day, DaysViewHolder>(DayDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        val binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DaysViewHolder(binding)
    }
    override fun onBindViewHolder(holder: DaysViewHolder, position:Int){
        val post = getItem(position)
        holder.bind(context,post, listener, user)
    }

    interface Listener{
        fun editDay(day: Day)
        fun selectDay(day: Day)
    }
}
