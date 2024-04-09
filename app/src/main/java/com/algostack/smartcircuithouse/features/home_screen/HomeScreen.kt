package com.algostack.smartcircuithouse.features.home_screen

import AddBuildingBottomSheetDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.algostack.smartcircuithouse.R
import com.algostack.smartcircuithouse.databinding.FragmentHomeScreenBinding
import com.algostack.smartcircuithouse.features.home_screen.adapter.TabPagerAdapter
import com.algostack.smartcircuithouse.features.home_screen.model.BuildingViewModel
import com.google.android.material.snackbar.Snackbar

class HomeScreen : Fragment() {

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BuildingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddBuilding.setOnClickListener {
            val bottomSheet = AddBuildingBottomSheetDialog()
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }

        val adapter = TabPagerAdapter(childFragmentManager)
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        viewModel.itemCreated.observe(viewLifecycleOwner) { itemCreated ->
            if (itemCreated) {
                showSnackbar(view, "Item created")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(view.context, R.color.green))
            .setTextColor(ContextCompat.getColor(view.context, R.color.white))
            .show()
    }
}
