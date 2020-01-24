package com.example.narucime.Model

class UserAppointment(val username: String, val date: String, val hospital: String, val examination: String) {
    constructor(): this("", "", "", "")
}