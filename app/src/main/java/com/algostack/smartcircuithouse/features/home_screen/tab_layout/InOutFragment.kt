package com.algostack.smartcircuithouse.features.home_screen.tab_layout

import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.features.home_screen.adapter.FilteredItemsAdapter
import com.algostack.smartcircuithouse.features.home_screen.adapter.InOutAdapter
import com.algostack.smartcircuithouse.features.home_screen.model.InOutViewModel
import com.algostack.smartcircuithouse.features.home_screen.model.InOutViewModelFactory
import com.algostack.smartcircuithouse.features.home_screen.model.Item
import com.algostack.smartcircuithouse.services.db.RoomDB
import com.algostack.smartcircuithouse.services.db.RoomRepository
import java.util.Calendar

class InOutFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InOutAdapter
    private val viewModel: InOutViewModel by viewModels {
        val roomDao = RoomDB.getDatabase(requireContext()).roomDao()
        val roomRepository = RoomRepository(roomDao)
        InOutViewModelFactory(roomRepository, this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_in_out, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewInOut)
        adapter = InOutAdapter(viewModel)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@InOutFragment.adapter
        }

        val imageViewFilterDate = view.findViewById<ImageView>(R.id.imageViewFilterDate)

        imageViewFilterDate.setOnClickListener {
            showDatePicker()
        }

        val textViewTotalInOutItems = view.findViewById<TextView>(R.id.textViewTotalInOutItems)

        viewModel.itemList.observe(viewLifecycleOwner, Observer { itemList ->
            adapter.submitList(itemList)
            textViewTotalInOutItems.text = "Total Items: ${itemList.size}"

        })

        return view
    }

    private fun showFilteredItemsDialog(filteredItems: List<Item>) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_filtered_items, null)
        val recyclerViewFilteredItems = dialogView.findViewById<RecyclerView>(R.id.recyclerViewFilteredItems)
        recyclerViewFilteredItems.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewFilteredItems.adapter = FilteredItemsAdapter(filteredItems) // Create an adapter for your items

        AlertDialog.Builder(requireContext())
            .setTitle("Filtered Items")
            .setView(dialogView)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    private fun showDatePicker() {
        val datePicker = DatePickerDialog(requireContext(), R.style.DatePickerDialogStyle)
        datePicker.setOnDateSetListener { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }

            val selectedDateInMillis = selectedCalendar.timeInMillis

            val filteredItems = viewModel.filterItemsByExitDate(selectedDateInMillis)
            if (filteredItems.isNotEmpty()) {
                Log.d("Filtering", "Filtering successful")
                showFilteredItemsDialog(filteredItems)
            } else {
                Log.e("Filtering", "Filtering failed")
            }
        }

        datePicker.show()
    }





}
