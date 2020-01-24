package com.example.narucime.View.adapters.fragmentsAdapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.narucime.View.fragments.MakeAnAppointment
import com.example.narucime.View.fragments.MyAppointments

class MyFragmentAdapter(fragmentManager : FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    val fragments = arrayOf(
        MyAppointments.newInstance(),
        MakeAnAppointment.newInstance()
    )
    val titles = arrayOf("MY APPOINTMENTS", "MAKE AN APPOINTMENT")

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}