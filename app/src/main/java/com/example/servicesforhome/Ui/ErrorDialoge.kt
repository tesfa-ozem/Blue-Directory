package com.example.servicesforhome.Ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent.EXTRA_TITLE
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import androidx.fragment.app.DialogFragment
import com.example.servicesforhome.R

class ErrorDialoge : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.response_modal, null))
            // Add action buttons

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment Message Dialog Fragment.
         */
        @JvmStatic
        fun newInstance(title: String, msg: String) = ErrorDialoge().apply {
            arguments = Bundle().apply {
                putString(EXTRA_TITLE, title)
                putString(EXTRA_MESSAGE, msg)

            }
        }
    }
}
