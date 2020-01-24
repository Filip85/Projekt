package com.example.narucime.View.adapters.recyclerviewAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.narucime.Model.UserAppointment
import com.example.narucime.R
import kotlinx.android.synthetic.main.item_appointment.view.*

class AppointemnetAdapter(val appointemnts: MutableList<UserAppointment>): RecyclerView.Adapter<AppointmentHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentHolder {
        val appointemntView = LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false)
        val appointmentHolder =
            AppointmentHolder(
                appointemntView
            )
        return appointmentHolder

    }

    override fun getItemCount(): Int {
        return appointemnts.size
    }

    override fun onBindViewHolder(holder: AppointmentHolder, position: Int) {
        val app = appointemnts[position]
        holder.bind(app)
    }
}

class AppointmentHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(appointment: UserAppointment) {
        itemView.cityApp.text = appointment.hospital
        itemView.timeApp.text = appointment.examination
        itemView.hospitalApp.text = appointment.date
    }
}
