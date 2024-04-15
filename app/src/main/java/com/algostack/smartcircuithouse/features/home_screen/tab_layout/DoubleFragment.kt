package com.algostack.smartcircuithouse.features.home_screen.tab_layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.algostack.smartcircuithouse.databinding.FragmentDoubleBinding
import com.algostack.smartcircuithouse.features.home_screen.model.DoubleViewModel
import com.algostack.smartcircuithouse.features.home_screen.model.DoubleViewModelFactory
import com.algostack.smartcircuithouse.features.room_screen.adapter.RoomAdapter
import com.algostack.smartcircuithouse.features.room_screen.dialog.BookingBottomSheetDialog
import com.algostack.smartcircuithouse.services.model.RoomData

class DoubleFragment : Fragment(), RoomAdapter.OnBookNowClickListener {

    private val viewModel: DoubleViewModel by viewModels { DoubleViewModelFactory(requireContext()) }
    private lateinit var binding: FragmentDoubleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDoubleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RoomAdapter(this, null)
        binding.recyclerViewDoubleRooms.adapter = adapter
        binding.recyclerViewDoubleRooms.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getDoubleRooms().observe(viewLifecycleOwner, Observer { rooms ->
            adapter.submitList(rooms)
            binding.textViewTotalDoubleItems.text = "Total Items: ${rooms.size}"

        })
    }

    override fun onBookNowClick(roomData: RoomData) {
        val bookingBottomSheetDialog = BookingBottomSheetDialog()
        bookingBottomSheetDialog.setSelectedRoom(roomData)
        bookingBottomSheetDialog.show(parentFragmentManager, "BookingBottomSheetDialog")
    }

    override fun onCancelBookingClick(roomData: RoomData) {
        viewModel.cancelRoomBooking(roomData)
    }
}
