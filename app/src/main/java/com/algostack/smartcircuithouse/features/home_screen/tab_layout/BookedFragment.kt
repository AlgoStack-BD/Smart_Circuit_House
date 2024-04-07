package com.algostack.smartcircuithouse.features.home_screen.tab_layout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.features.home_screen.adapter.BookedRoomAdapter
import com.algostack.smartcircuithouse.services.model.RoomData

class BookedFragment : Fragment() {
    private lateinit var bookedRoomAdapter: BookedRoomAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_booked, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.bookedRecyclerView)
        bookedRoomAdapter = BookedRoomAdapter()
        recyclerView.adapter = bookedRoomAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return view
    }

    fun updateRooms(bookedRooms: List<RoomData>) {
        bookedRoomAdapter.submitList(bookedRooms)
    }
}
