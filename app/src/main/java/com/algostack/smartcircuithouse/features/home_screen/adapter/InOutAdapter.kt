package com.algostack.smartcircuithouse.features.home_screen.adapter

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.features.home_screen.model.InOutViewModel
import com.algostack.smartcircuithouse.features.home_screen.model.Item
import java.util.*

class InOutAdapter(private val viewModel: InOutViewModel) :
    ListAdapter<Item, InOutAdapter.ItemViewHolder>(ItemDiffCallback()) {

    private var expandedPosition = RecyclerView.NO_POSITION
    private val handler = Handler(Looper.getMainLooper())

    init {
        startExitDateCheck()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_info_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position == expandedPosition)

        holder.itemView.startAnimation(
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fall_down)
        )
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val buildingNameTextView: TextView =
            itemView.findViewById(R.id.buildingNameTextView)
        private val roomNumberTextView: TextView = itemView.findViewById(R.id.roomNumberTextView)
        private val floorNumberTextView: TextView = itemView.findViewById(R.id.floorNumberTextView)
        private val bedTypeTextView: TextView = itemView.findViewById(R.id.bedTypeTextView)
        private val roomBookNowButton: ImageView = itemView.findViewById(R.id.roomBookNow)

        private val expandableLayout: LinearLayout = itemView.findViewById(R.id.expandableLayout)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val detailsTextView: TextView = itemView.findViewById(R.id.detailsTextView)
        private val entryDateTextView: TextView = itemView.findViewById(R.id.entryDateTextView)
        private val exitDateTextView: TextView = itemView.findViewById(R.id.exitDateTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (position == expandedPosition) {
                        expandedPosition = RecyclerView.NO_POSITION
                        notifyItemChanged(position)
                    } else {
                        val previouslyExpandedPosition = expandedPosition
                        expandedPosition = position
                        notifyItemChanged(previouslyExpandedPosition)
                        notifyItemChanged(expandedPosition)
                    }
                }
            }

            roomBookNowButton.setOnClickListener {
                showCancelBookingConfirmation()
            }

        }

        private fun showCancelBookingConfirmation() {
            AlertDialog.Builder(itemView.context)
                .setMessage("Are you sure you want to cancel the booking?")
                .setPositiveButton("Yes") { _, _ ->
                    val item = getItem(adapterPosition)
                    item?.let {
                        viewModel.cancelBooking(it.id)
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }

        fun bind(item: Item, isExpanded: Boolean) {
            buildingNameTextView.text = "Building: " + item.roomBuildingName
            roomNumberTextView.text = "Floor No: " + item.roomNumber
            floorNumberTextView.text = "Room No: " + item.floorNumber
            bedTypeTextView.text = "Bed Type: " + item.bedType

            nameTextView.text = "C. Name: " + item.name
            detailsTextView.text = "C. Details: " + item.details
            entryDateTextView.text = "Check-in: " + item.entryDate
            exitDateTextView.text = "Check-out: " + item.exitDate

            expandableLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE
        }
    }

    private class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    private fun startExitDateCheck() {
        handler.postDelayed(exitDateCheckRunnable, EXIT_DATE_CHECK_INTERVAL)
    }

    private fun checkExitDatesAndCancelBooking(position: Int) {
        val currentDate = Calendar.getInstance().timeInMillis.toString()

        val item = getItem(position)
        if (item.exitDate != null && item.exitDate!! < currentDate) {
            item.let {
                viewModel.cancelBooking(it.id)
            }
        }
    }

    private val exitDateCheckRunnable = object : Runnable {
        override fun run() {
            currentList.forEachIndexed { index, _ ->
                checkExitDatesAndCancelBooking(index)
            }
            handler.postDelayed(this, EXIT_DATE_CHECK_INTERVAL)
        }
    }

    companion object {
        private const val EXIT_DATE_CHECK_INTERVAL = 24 * 60 * 60 * 1000L
    }
}

