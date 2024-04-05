package com.algostack.smartcircuithouse.features.room_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.databinding.FragmentRoomScreenBinding
import com.algostack.smartcircuithouse.features.room_screen.adapter.RoomAdapter
import com.algostack.smartcircuithouse.features.room_screen.dialog.AddRoomBottomSheetDialog
import com.algostack.smartcircuithouse.features.room_screen.dialog.BookingBottomSheetDialog
import com.algostack.smartcircuithouse.features.room_screen.model.RoomViewModel
import com.algostack.smartcircuithouse.services.db.RoomDB
import com.algostack.smartcircuithouse.services.db.RoomRepository
import com.algostack.smartcircuithouse.services.model.RoomData

class RoomScreen : Fragment(), RoomAdapter.OnBookNowClickListener {

    private var _binding: FragmentRoomScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RoomViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val roomDao = RoomDB.getDatabase(requireContext()).roomDao()
                val roomRepository = RoomRepository(roomDao)

                if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return RoomViewModel(roomRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoomScreenBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buildingId = arguments?.getInt("buildingId") ?: -1
        val buildingName = arguments?.getString("title") ?: "Default Building Name"

        val toolbar = view.findViewById<Toolbar>(R.id.toolbarBuildingName)
        toolbar.title = buildingName

        val adapter = RoomAdapter(this)
        binding.allRecyclerView.apply {
            this.adapter = adapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        viewModel.getRoomsForBuilding(buildingId).observe(viewLifecycleOwner) { rooms ->
            adapter.submitList(rooms)
        }

        binding.fabAddRoom.setOnClickListener {
            val roomDao = RoomDB.getDatabase(requireContext()).roomDao()
            val bottomSheet = AddRoomBottomSheetDialog(roomDao, buildingId)
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }

    }

    override fun onBookNowClick(roomData: RoomData) {
        val bottomSheet = BookingBottomSheetDialog()
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
