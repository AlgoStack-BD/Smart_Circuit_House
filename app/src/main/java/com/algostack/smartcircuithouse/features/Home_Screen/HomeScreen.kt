package com.algostack.smartcircuithouse.features.Home_Screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.databinding.FragmentHomeScreenBinding
import com.algostack.smartcircuithouse.features.Home_Screen.adapter.CardAdapter
import com.algostack.smartcircuithouse.features.Home_Screen.model.CardData



class HomeScreen : Fragment() {

    var _binding: FragmentHomeScreenBinding? = null
    val binding get() = _binding!!

    private lateinit var adapter : CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
     return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabTitles = listOf("All", "Single", "Double", "Time", "Booked", "Unbooked")
        tabTitles.forEach { title ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(title))
        }

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val dataList = createDataList()
        adapter = CardAdapter(dataList)
        recyclerView.adapter = adapter
    }


    private fun createDataList(): List<CardData> {
        val dataList = mutableListOf<CardData>()
        for (i in 1..10) {
            dataList.add(CardData(R.drawable.room2, "Building No. $i", "Description $i"))
        }
        return dataList
    }

}