package com.example.narucime

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.narucime.Model.User
import com.example.narucime.Model.UserAppointment
import com.example.narucime.SharedPreferences.MyPreference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.my_appointments.*

class MyAppointments : Fragment() {
    val userList: MutableList<UserAppointment> = mutableListOf()

    companion object{
        fun newInstance() : MyAppointments{
            return MyAppointments()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.my_appointments, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ref = FirebaseDatabase.getInstance().getReference("cities/")

        /*ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.d("sasa", "asas")
            }

            override fun onDataChange(p0: DataSnapshot) {
                val pref = MyPreference(MyApplication.ApplicationContext)
                val userName = pref.getUsername()

                for(c in p0.children){
                    for(h in c.children){
                        for(e in h.children){
                            val user = e.getValue(UserAppointment::class.java)
                            Log.d("dsds1", user!!.username.toString())
                            for(d in e.children){
                                for(i in d.children){
                                    Log.d("dsds", i.value.toString())
                                }
                            }
                        }
                    }
                }
            }

        })*/

        val pref = MyPreference(MyApplication.ApplicationContext)

        val query = FirebaseDatabase.getInstance().getReference("cities")
            .orderByChild("username")
            .equalTo(pref.getUsername())

        query.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("dsds", p0.toString())
            }

        })

        //Log.d("dsds", query.toString())

        myAppointmentsRecylcerView.layoutManager =  LinearLayoutManager(MyApplication.ApplicationContext, RecyclerView.VERTICAL, false)
        myAppointmentsRecylcerView.itemAnimator = DefaultItemAnimator()
        myAppointmentsRecylcerView.addItemDecoration(DividerItemDecoration(MyApplication.ApplicationContext, RecyclerView.VERTICAL))
        myAppointmentsRecylcerView.adapter = AppointemnetAdapter(AppointmentRepository.appointment)

    }
}