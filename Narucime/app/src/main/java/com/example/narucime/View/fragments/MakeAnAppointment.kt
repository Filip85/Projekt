package com.example.narucime.View.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.narucime.View.ui.HospitalMapsActivity
import com.example.narucime.R
import com.example.narucime.View.ui.CityActivity
import kotlinx.android.synthetic.main.make_an_appointment.*

class MakeAnAppointment : Fragment() {

    companion object {
        fun newInstance() : MakeAnAppointment {
            return MakeAnAppointment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.make_an_appointment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        manualAppointment.setOnClickListener{
            val intent = Intent(context, CityActivity::class.java)
            startActivity(intent)
        }

        locationAppointment.setOnClickListener{
            val intent = Intent(context, HospitalMapsActivity::class.java)
            startActivity(intent)
        }
        super.onViewCreated(view, savedInstanceState)
    }
}