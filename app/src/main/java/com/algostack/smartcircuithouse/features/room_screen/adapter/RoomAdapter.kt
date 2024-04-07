package com.algostack.smartcircuithouse.features.room_screen.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.services.model.RoomData

class RoomAdapter(private val onBookNowClickListener: OnBookNowClickListener) :
    ListAdapter<RoomData, RoomAdapter.RoomViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<RoomData>() {
            override fun areItemsTheSame(oldItem: RoomData, newItem: RoomData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RoomData, newItem: RoomData): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view, onBookNowClickListener)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class RoomViewHolder(
        itemView: View,
        private val onBookNowClickListener: OnBookNowClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        private val roomNoTextView: TextView = itemView.findViewById(R.id.textViewRoomNo)
        private val bedTypeTextView: TextView = itemView.findViewById(R.id.textViewBedType)
        private val floorNoTextView: TextView = itemView.findViewById(R.id.textViewFloorNo)
        private val bookNowButton: Button = itemView.findViewById(R.id.roomBookNow)

        fun bind(roomData: RoomData) {
            floorNoTextView.text = /*"Floor No:  " +*/ roomData.floorNo.toString()
            roomNoTextView.text = /*"Room No: " +*/ roomData.roomNo
            bedTypeTextView.text = /*"Bed Type: " +*/ roomData.bedType

            if (roomData.isBooked) {
                bookNowButton.text = itemView.context.getString(R.string.booked)
                bookNowButton.setBackgroundColor(itemView.context.getColor(R.color.red))
            } else {
                bookNowButton.text = itemView.context.getString(R.string.book_now)
                bookNowButton.setBackgroundColor(itemView.context.getColor(R.color.primary))
            }

            bookNowButton.setOnClickListener {
                if (roomData.isBooked) {
                    showCancelBookingConfirmation(roomData)
                } else {
                    onBookNowClickListener.onBookNowClick(roomData)
                }
            }
        }

        private fun showCancelBookingConfirmation(roomData: RoomData) {
            AlertDialog.Builder(itemView.context)
                .setMessage("Are you sure you want to cancel the booking?")
                .setPositiveButton("Yes") { _, _ ->
                    onBookNowClickListener.onCancelBookingClick(roomData)
                }
                .setNegativeButton("No", null)
                .show()
        }
    }


    interface OnBookNowClickListener {
        fun onBookNowClick(roomData: RoomData)
        fun onCancelBookingClick(roomData: RoomData)
    }

    fun updateRoomStatus(position: Int) {
        val roomData = currentList[position]
        roomData.isBooked = true
        notifyItemChanged(position)
    }
}
