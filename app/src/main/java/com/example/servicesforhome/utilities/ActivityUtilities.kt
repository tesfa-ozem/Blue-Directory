package com.example.servicesforhome.utilities

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

object ActivityUtilities {
    fun addFragmentToActivity(
        manager: FragmentManager,
        fragment: Fragment?,
        frameId: Int
    ) {
        val transaction: FragmentTransaction = manager.beginTransaction()
        if (fragment != null) {
            transaction.add(frameId, fragment)
        }
        transaction.commit()
    }
}
