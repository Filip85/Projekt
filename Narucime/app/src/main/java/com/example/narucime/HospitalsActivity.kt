package com.example.narucime

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.narucime.Model.Hospital
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_city.*
import kotlinx.android.synthetic.main.activity_hospitals.*
import kotlinx.android.synthetic.main.item_hospital.*
import org.jetbrains.anko.doAsync

class HospitalsActivity : AppCompatActivity() {

    lateinit var Hospitals: MutableList<Hospital>
    lateinit var hosp: MutableList<String>

    //var Hospitals: ArrayList<Hospitals> = ArrayList()

    companion object {
        const val CITYNAME = "position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospitals)

        setUpUi()
    }

    private fun setUpUi() {

        Hospitals = mutableListOf()
        hosp = mutableListOf()

        doAsync {
            getDataFromFirebase()
        }
        //getDataFromFirebase()
    }

    private fun getDataFromFirebase() {
        val citiyname = intent?.getStringExtra(CITYNAME ?: "nothing recieved")

        val ref = FirebaseDatabase.getInstance().getReference("cities").child(citiyname!!)

        Log.d("HospitalActivity", ref.toString())

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("HospitalActivity", p0.toString())

                for(h in p0.children) {

                    //val hos = h.getValue(Hospital::class.java)

                    //Hospitals.add(hos!!)
                    hosp.add(h.key!!)

                    Log.d("HospitalActivity", Hospitals.toString())
                    Log.d("HospitalActivity", hosp.toString())
                }

                hospitasRecyclerView.layoutManager =  LinearLayoutManager(MyApplication.ApplicationContext, RecyclerView.VERTICAL, false)
                hospitasRecyclerView.itemAnimator = DefaultItemAnimator()
                hospitasRecyclerView.addItemDecoration(DividerItemDecoration(MyApplication.ApplicationContext, RecyclerView.VERTICAL))
                hospitasRecyclerView.adapter = HospitalAdapter(hosp)
            }
        })
    }
}

