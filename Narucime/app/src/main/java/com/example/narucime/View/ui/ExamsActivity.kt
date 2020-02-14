package com.example.narucime.View.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.narucime.FirebaseSource.DataClass
import com.example.narucime.R
import kotlinx.android.synthetic.main.activity_exams.*


class ExamsActivity : AppCompatActivity() {

    //lateinit var examsList: MutableList<Exams>
    lateinit var examsList: MutableList<String>
    lateinit var path: String

    companion object {
        const val HOSPITALNAME = "examination"
        const val CITYNAMEE = "cityname"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exams)


        setUpUi()
    }

    private fun setUpUi() {
        this.title = "Examinations"

        getDataFromFirebase()


    }

    private fun getDataFromFirebase() {
        val hospitalName = intent?.getStringExtra(
            HOSPITALNAME ?: "nothing recieved")
        val cityName = intent?.getStringExtra(
            CITYNAMEE ?: "nothing recieved")

        Log.d("NekiGrad", cityName!!)
        path  = "cities//${cityName}/${hospitalName}"
        Log.d("PathIsMy", path)

        val data = DataClass()
        data.getExamesFromFirebase(examsRecyclerView as RecyclerView, path, cityName, hospitalName!!)
    }
}
