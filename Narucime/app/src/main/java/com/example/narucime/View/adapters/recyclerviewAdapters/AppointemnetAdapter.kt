package com.example.narucime.View.adapters.recyclerviewAdapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.narucime.Context.MyApplication
import com.example.narucime.FirebaseSource.DataClass
import com.example.narucime.Model.UserAppointment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.cancel_appointment_dialog.*
import kotlinx.android.synthetic.main.item_appointment.view.*


class AppointemnetAdapter(val appointemnts: MutableList<UserAppointment>, val cityList: MutableList<String>): RecyclerView.Adapter<AppointmentHolder>() {

    lateinit var dialog: Dialog

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentHolder {
        val appointemntView = LayoutInflater.from(parent.context).inflate(com.example.narucime.R.layout.item_appointment, parent, false)
        val appointmentHolder =
            AppointmentHolder(
                appointemntView,
                parent.context
            )

        dialog = Dialog(parent.context)

        return appointmentHolder

    }

    override fun getItemCount(): Int {
        return appointemnts.size
    }

    override fun onBindViewHolder(holder: AppointmentHolder, position: Int) {
        val app = appointemnts[position]
        val city = cityList[position]
        holder.bind(app, city, dialog)
    }
}

class AppointmentHolder(itemView: View, context: Context): RecyclerView.ViewHolder(itemView){
    fun bind(appointment: UserAppointment, city: String, dialog: Dialog) {
        itemView.cityApp.text = appointment.hospital
        itemView.timeApp.text = appointment.examination
        itemView.hospitalApp.text = appointment.date
        itemView.time.text = appointment.time

        val spliter = appointment.date.split("/")

        val day = spliter[0]
        val mounth = spliter[1]
        val year = spliter[2]

        val newDateFormat = "$day-$mounth-$year"

        dialog.setContentView(com.example.narucime.R.layout.cancel_appointment_dialog)

        val fireabse = FirebaseAuth.getInstance().currentUser

        itemView.setOnClickListener{
            val path = "cities/$city/${appointment.hospital}/${appointment.examination}/${newDateFormat}/${fireabse!!.uid}"
            dialog.show()
            dialog.confirm_button.setOnClickListener{
                val data = DataClass()
                data.deleteAppointemnt(path, appointment.date, appointment.examination, appointment.username, appointment.hospital)
                dialog.dismiss()
                Toast.makeText(MyApplication.ApplicationContext,"Appointment is deleted.", Toast.LENGTH_LONG).show()
            }
            dialog.quite_button.setOnClickListener{
                dialog.dismiss()
            }
        }
    }
}
