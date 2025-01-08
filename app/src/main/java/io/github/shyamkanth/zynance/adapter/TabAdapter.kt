package io.github.shyamkanth.zynance.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.github.shyamkanth.zynance.fragment.ExpenseFragment
import io.github.shyamkanth.zynance.fragment.StatsFragment
import io.github.shyamkanth.zynance.fragment.TransactionFragment

class TabAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ExpenseFragment()
            1 -> TransactionFragment()
            2 -> StatsFragment()
            else -> ExpenseFragment()
        }
    }

}