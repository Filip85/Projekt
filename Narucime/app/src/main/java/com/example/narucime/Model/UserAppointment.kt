package com.example.narucime.Model

class UserAppointment(val username: String, var date: String, val hospital: String, val examination: String, val time: String) {
    constructor(): this("", "", "", "", "")
}