package com.example.narucime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.narucime.SharedPreferences.MyPreference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_exams.*


class ExamsActivity : AppCompatActivity() {

    //lateinit var examsList: MutableList<Exams>
    lateinit var examsList: MutableList<String>

    companion object {
        const val HOSPITALNAME = "examination"
        const val CITYNAME = "cityname"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exams)


        setUpUi()
    }

    private fun setUpUi() {

        examsList = mutableListOf()

        examsList.clear()

        getDataFromFirebase()

        examsList.clear()

    }

    private fun getDataFromFirebase() {
        val hospitalName = intent?.getStringExtra(HOSPITALNAME ?: "nothing recieved")
        //val cityName = intent?.getStringExtra(CITYNAME ?: "nothing recieved")

        val myPreference = MyPreference(this)
        val cityname = myPreference.getCityName()

        Log.d("ExamsActivity", cityname!!)

        val city = FirebaseDatabase.getInstance().getReference("cities").child(cityname)
        val ref = city.child(hospitalName.toString())

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(e in p0.children) {
                    //val exams = e.getValue(Exams::class.java)

                    //examsList.add(exams!!)

                    examsList.add(e.key!!)

                    Log.d("ExamsActivity", examsList.toString())

                    examsRecyclerView.layoutManager =  LinearLayoutManager(MyApplication.ApplicationContext, RecyclerView.VERTICAL, false)
                    examsRecyclerView.itemAnimator = DefaultItemAnimator()
                    examsRecyclerView.addItemDecoration(DividerItemDecoration(MyApplication.ApplicationContext, RecyclerView.VERTICAL))
                    examsRecyclerView.adapter = ExaminationAdapter(examsList)

                }
            }
        })
    }
}
