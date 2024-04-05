package com.algostack.smartcircuithouse.features.home_screen.tab_layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.databinding.FragmentAllBinding
import com.algostack.smartcircuithouse.features.home_screen.HomeScreenDirections
import com.algostack.smartcircuithouse.features.home_screen.adapter.CardAdapter
import com.algostack.smartcircuithouse.features.home_screen.model.CardData
import com.algostack.smartcircuithouse.services.db.BuildingDB

class AllFragment : Fragment(), CardAdapter.OnItemClickListener {

    private var _binding: FragmentAllBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CardAdapter(this)
        binding.allRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.allRecyclerView.adapter = adapter

        val buildingDao = BuildingDB.getDatabase(requireContext()).buildingDao()
        buildingDao.getAllBuildings().observe(viewLifecycleOwner) { buildings ->
            adapter.submitList(buildings.map {
                CardData(
                    id = it.id,
                    imageResource = R.drawable.building_room,
                    title = it.name
                )
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(title: String, buildingId: Int) {
        val action = HomeScreenDirections.actionHomeScreenToRoomScreen(title, buildingId)
        findNavController().navigate(action)
    }
}
