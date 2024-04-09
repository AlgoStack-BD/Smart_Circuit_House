package com.algostack.smartcircuithouse.features.home_screen.tab_layout

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.features.home_screen.adapter.ItemAdapter
import com.algostack.smartcircuithouse.features.home_screen.model.InOutViewModel
import com.algostack.smartcircuithouse.features.home_screen.model.InOutViewModelFactory
import com.algostack.smartcircuithouse.services.db.RoomDB
import com.algostack.smartcircuithouse.services.db.RoomRepository

class InOutFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private val viewModel: InOutViewModel by viewModels {
        val roomDao = RoomDB.getDatabase(requireContext()).roomDao()
        val roomRepository = RoomRepository(roomDao)
        InOutViewModelFactory(roomRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_in_out, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewInOut)
        adapter = ItemAdapter()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@InOutFragment.adapter
        }

        val textViewTotalInOutItems = view.findViewById<TextView>(R.id.textViewTotalInOutItems)

        viewModel.itemList.observe(viewLifecycleOwner, Observer { itemList ->
            adapter.submitList(itemList)
            textViewTotalInOutItems.text = "Total Items: ${itemList.size}"

        })

        return view
    }
}
