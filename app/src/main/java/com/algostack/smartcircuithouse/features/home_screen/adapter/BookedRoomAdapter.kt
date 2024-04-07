package com.algostack.smartcircuithouse.features.home_screen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.services.model.RoomData

class BookedRoomAdapter :
    ListAdapter<RoomData, BookedRoomAdapter.ViewHolder>(RoomDataDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_booked_room, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val room = getItem(position)
        holder.bind(room)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roomNumberTextView: TextView = itemView.findViewById(R.id.roomNumberTextView)
        private val bedTypeTextView: TextView = itemView.findViewById(R.id.bedTypeTextView)

        fun bind(room: RoomData) {
            roomNumberTextView.text = room.roomNo
            bedTypeTextView.text = room.bedType
        }
    }

    class RoomDataDiffCallback : DiffUtil.ItemCallback<RoomData>() {
        override fun areItemsTheSame(oldItem: RoomData, newItem: RoomData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RoomData, newItem: RoomData): Boolean {
            return oldItem == newItem
        }
    }
}
