package com.bikeshare.vhome.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bikeshare.vhome.data.itemmodel.EventItem
import com.bikeshare.vhome.databinding.ItemEventBinding
import java.text.SimpleDateFormat
import java.util.*

class EventListAdapter(private val listener: OnItemClickListener) :
    ListAdapter<EventItem, EventListAdapter.EventViewHolder>(EVENT_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class EventViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val event = getItem(position)
                        listener.onItemClick(event)
                    }
                }
            }
        }

        fun bind(event: EventItem) {
            binding.apply {
                /*Glide.with(itemView)
                    .load(event.thumbnail.fileURL)
                    .into(eventImage)*/
                eventCameraId.text = event.deviceId
                eventTime.text = SimpleDateFormat("HH:mm:ss").format(Date(event.createDate))
                if (event.type == 17) eventType.setText("Phát hiện chuyển động")
                else eventType.setText("Phát hiện người")
                Log.d("EventAdapter", "${bindingAdapterPosition}. ${event.deviceId}  ${SimpleDateFormat("HH:mm:ss").format(Date(event.createDate))}")
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(event: EventItem)
    }

    companion object{
        private val EVENT_COMPARATOR = object : DiffUtil.ItemCallback<EventItem>() {
            override fun areItemsTheSame(oldItem: EventItem, newItem: EventItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: EventItem, newItem: EventItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}