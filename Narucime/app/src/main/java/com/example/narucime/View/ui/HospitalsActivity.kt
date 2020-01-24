package com.example.narucime.View.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.narucime.Model.DataClass
import com.example.narucime.R
import kotlinx.android.synthetic.main.activity_hospitals.*

class HospitalsActivity : AppCompatActivity() {

    lateinit var path: String

    companion object {
        const val CITYNAME = "position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospitals)

        setUpUi()
    }

    private fun setUpUi() {

        getDataFromFirebase()
    }

    private fun getDataFromFirebase() {
        val citiyname = intent?.getStringExtra(
            CITYNAME ?: "nothing recieved")

        path = "cities/${citiyname}"

        val data = DataClass()
        data.getHospitalsFromFirebase(hospitasRecyclerView as RecyclerView, path, citiyname!!)
    }
}

