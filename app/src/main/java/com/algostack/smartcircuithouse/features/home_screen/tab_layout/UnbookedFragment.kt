package com.algostack.smartcircuithouse.features.home_screen.tab_layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.algostack.smartcircuithouse.databinding.FragmentUnBookedBinding
import com.algostack.smartcircuithouse.features.home_screen.model.UnbookedViewModel
import com.algostack.smartcircuithouse.features.home_screen.model.UnbookedViewModelFactory
import com.algostack.smartcircuithouse.features.room_screen.adapter.RoomAdapter
import com.algostack.smartcircuithouse.services.db.RoomRepository
import com.algostack.smartcircuithouse.services.model.RoomData

class UnbookedFragment : Fragment() {

    private val viewModel: UnbookedViewModel by viewModels { UnbookedViewModelFactory(requireContext()) }
    private lateinit var binding: FragmentUnBookedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUnBookedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RoomAdapter(onBookNowClickListener)
        binding.recyclerViewUnbookedRooms.adapter = adapter
        binding.recyclerViewUnbookedRooms.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getUnbookedRooms().observe(viewLifecycleOwner, Observer { rooms ->
            adapter.submitList(rooms)
        })
    }

    private val onBookNowClickListener = object : RoomAdapter.OnBookNowClickListener {
        override fun onBookNowClick(roomData: RoomData) {
        }

        override fun onCancelBookingClick(roomData: RoomData) {
        }
    }
}
