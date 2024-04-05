package com.algostack.smartcircuithouse.features.home_screen.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.algostack.smartcircuithouse.features.home_screen.tab_layout.AllFragment
import com.algostack.smartcircuithouse.features.home_screen.tab_layout.BookedFragment
import com.algostack.smartcircuithouse.features.home_screen.tab_layout.DoubleFragment
import com.algostack.smartcircuithouse.features.home_screen.tab_layout.InOutFragment
import com.algostack.smartcircuithouse.features.home_screen.tab_layout.SingleFragment
import com.algostack.smartcircuithouse.features.home_screen.tab_layout.UnbookedFragment

class TabPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> AllFragment()
            1 -> SingleFragment()
            2 -> DoubleFragment()
            3 -> InOutFragment()
            4 -> BookedFragment()
            5 -> UnbookedFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    override fun getCount(): Int {
        return 6
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "All"
            1 -> "Single"
            2 -> "Double"
            3 -> "In & Out"
            4 -> "Booked"
            5 -> "Unbooked"
            else -> null
        }
    }
}
