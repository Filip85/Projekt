package com.example.narucime.View.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.narucime.R

class CancelAppointment: DialogFragment() {

    companion object {
        fun newInstance(): CancelAppointment {
            return CancelAppointment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.cancel_appointment_dialog, container, false)
        return view
    }
}