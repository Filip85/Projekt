package com.example.narucime.View.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.narucime.FirebaseSource.DataClass
import com.example.narucime.Model.UserAppointment
import com.example.narucime.R
import kotlinx.android.synthetic.main.my_appointments.*

class MyAppointments : Fragment() {
    val userList: MutableList<UserAppointment> = mutableListOf()
    lateinit var path: String

    companion object{
        fun newInstance() : MyAppointments {
            return MyAppointments()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.my_appointments, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        path = "cities/"

        val data = DataClass()
        data.getData(myAppointmentsRecylcerView as RecyclerView, path)
    }
}