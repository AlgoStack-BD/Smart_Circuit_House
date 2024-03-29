package com.algostackbd.doorways.features.home_screen

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.algostackbd.doorways.R
import com.algostackbd.doorways.databinding.ActivityHomeScreenBinding
import com.algostackbd.doorways.features.home_screen.adapter.CardAdapter
import com.algostackbd.doorways.features.home_screen.model.CardData
import com.google.android.material.tabs.TabLayout

class HomeScreen : AppCompatActivity() {
    private lateinit var binding: ActivityHomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tabTitles = listOf("All", "Single", "Double", "Time", "Booked", "Unbooked")
        tabTitles.forEach { title ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(title))
        }

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val dataList = createDataList()
        val adapter = CardAdapter(dataList)
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
