package com.example.narucime.SharedPreferences

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MyPreference(context: Context) {

    val PREFERENCE_NAME = "CityNameFile"
    val CITYNAME = "cityname"
    val USERNAME = "username"
    val HOSPITAL = "hospital"
    val EXAMINATION = "examination"
    val BLOCKED_DATES = "BlockedDates"
    val DATE: MutableList<String>? = null
    //val temp: MutableList<String>? = null
    val TEMP = "alreadyHaveAnAppointmentForThisDay"

    val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getCityName(): String? {
        return preference.getString(CITYNAME, "0")
    }
    fun getUsername(): String? {
        return preference.getString(USERNAME, "0")
    }
    fun getHospitalName(): String? {
        return preference.getString(HOSPITAL, "0")
    }
    fun getExamination(): String? {
        return preference.getString(EXAMINATION, "0")
    }

    fun getAppointment(): String? {
        return preference.getString(TEMP, "false")
    }

    inline fun getDate(): MutableList<String>? {
        val GSON: Gson? = Gson()
        val json = preference.getString(BLOCKED_DATES, "0")
        val list: MutableList<String>? =  GSON?.fromJson(json, object: TypeToken<MutableList<String>>(){}.type)
        return list
    }

    fun setCityName(cityName: String) {
        val editor = preference.edit()
        editor.putString(CITYNAME, cityName)
        editor.apply()
    }
    fun setUsername(username: String) {
        val editor = preference.edit()
        editor.putString(USERNAME, username)
        editor.apply()
    }
    fun setHospitalName(hospital: String) {
        val editor = preference.edit()
        editor.putString(HOSPITAL, hospital)
        editor.apply()
    }
    fun setExamination(examination: String) {
        val editor = preference.edit()
        editor.putString(EXAMINATION, examination)
        editor.apply()
    }
    fun setDate(date: MutableList<String>) {
        val editor = preference.edit()
        val GSON: Gson? = Gson()
        val json = GSON?.toJson(date)
        //date.clear()
        editor.putString(BLOCKED_DATES, json)
        editor.apply()
        /*for (d in date) {
            editor.putStringSet(d, DATE)
        }*/
        //val d: Set<String> = date.toSet()

        /*for(d in date) {
            temp?.add(d)
        }*/
        /*editor.putStringSet(DATE.toString(), temp.toString())
        editor.apply()*/
    }

    fun setAppointment(app: String) {
        val editor = preference.edit()
        editor.putString(TEMP, app)
        editor.apply()
    }
}