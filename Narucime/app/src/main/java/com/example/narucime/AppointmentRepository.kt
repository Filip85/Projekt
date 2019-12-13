package com.example.narucime

import com.example.narucime.Model.UserAppointment

object AppointmentRepository {

    val appointment: MutableList<UserAppointment>

    init {
        appointment = retriveAppointment()
    }

    private fun retriveAppointment(): MutableList<UserAppointment> {
        return mutableListOf(
            UserAppointment("admin5","24/12/2019"),
            UserAppointment("admin5", "25/12/2019"),
            UserAppointment("admin5", "26/12/2019"),
            UserAppointment("admin5", "27/12/2019")
        )
    }

    fun addAppointment(newAppointment: UserAppointment) {
        appointment.add(newAppointment)
    }
}