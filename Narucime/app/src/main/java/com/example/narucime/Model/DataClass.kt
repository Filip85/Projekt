package com.example.narucime.Model

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.narucime.MyApplication
import com.example.narucime.SharedPreferences.MyPreference
import com.example.narucime.View.RecyclerViewClass
import com.example.narucime.View.adapters.recyclerviewAdapters.AppointemnetAdapter
import com.example.narucime.View.adapters.recyclerviewAdapters.ExaminationAdapter
import com.example.narucime.View.adapters.recyclerviewAdapters.HospitalAdapter
import com.example.narucime.View.listeners.FetchHospitalsName
import com.example.narucime.View.listeners.OnGetDataListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DataClass {
    lateinit var listOfUserAppointment: MutableList<UserAppointment>
    lateinit var list: MutableList<String>

    fun getData(recyclerView: RecyclerView, path: String) {

        val ref = FirebaseDatabase.getInstance().getReference(path)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("DataClass", "Error: ${p0}")
            }

            override fun onDataChange(p0: DataSnapshot) {
                val pref = MyPreference(MyApplication.ApplicationContext)
                val userName = pref.getUsername()

                listOfUserAppointment = mutableListOf()

                val date: LocalDate = LocalDate.now()

                for (c in p0.children) {
                    for (h in c.children) {
                        for (e in h.children) {
                            for (d in e.children) {
                                for (i in d.children) {
                                    val user = i.getValue(UserAppointment::class.java)

                                    val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
                                    val formattedDate = date.format(formatter)

                                    val appointmentDate = LocalDate.parse(user!!.date, formatter)
                                    val todaysDate = LocalDate.parse(formattedDate, formatter)

                                    if (user!!.username == userName && todaysDate.isBefore(appointmentDate)) {
                                        listOfUserAppointment.add(user)
                                    }
                                    Log.d("dsds1", user!!.username.toString())
                                }
                            }

                            val recycler = RecyclerViewClass()
                            val adapter: RecyclerView.Adapter<*> =
                                AppointemnetAdapter(
                                    listOfUserAppointment
                                )
                            recycler.createRecyclerView(recyclerView, adapter)
                        }
                    }
                }
            }
        })
    }

    fun getHospitalsFromFirebase(recyclerView: RecyclerView, path: String, cityname: String) {

        val ref = FirebaseDatabase.getInstance().getReference(path)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.d("HospitalActivity", p0.toString())

                list = mutableListOf()

                for (h in p0.children) {

                    list.add(h.key!!)

                    Log.d("HospitalActivity", list.toString())
                }

                val recycler = RecyclerViewClass()
                val adapter: RecyclerView.Adapter<*> =
                    HospitalAdapter(
                        list,
                        cityname
                    )
                recycler.createRecyclerView(recyclerView, adapter)
            }
        })
    }

    fun getExamesFromFirebase(recyclerView: RecyclerView, path: String, cityname: String, hospitalname: String) {

        val ref = FirebaseDatabase.getInstance().getReference(path)

        val onGetDataListener = object : OnGetDataListener {
            override fun onsuccess() {
                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        Log.d("ExaminationActivity", p0.toString())

                        list = mutableListOf()

                        for (h in p0.children) {

                            list.add(h.key!!)

                            Log.d("ExaminationActivity", list.toString())
                        }

                        val recycler = RecyclerViewClass()
                        val adapter: RecyclerView.Adapter<*> =
                            ExaminationAdapter(
                                list,
                                cityname,
                                hospitalname
                            )
                        recycler.createRecyclerView(recyclerView, adapter)
                    }
                })
            }

            override fun onFailed() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        onGetDataListener.onsuccess()
    }

    fun getDatesFromFirebase(path: String) {

        val pref = MyPreference(MyApplication.ApplicationContext)

        val ref1 = FirebaseDatabase.getInstance().getReference(path)

        ref1.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                list = mutableListOf()
                list.clear()
                for (h in p0.children) {
                    val numOfApp = h.childrenCount.toString()
                    for (n in h.children) {
                        Log.d("key123", n.childrenCount.toString())
                        val username = n.getValue(UserAppointment::class.java)

                        if (username?.date != null) {
                            if (numOfApp == "2" || username.username == pref.getUsername()) {
                                list.add(username.date)
                            }
                        }
                    }
                }

                Log.d("SASA2", list.toString())
                pref.setDate(list)
                Log.d("SASA3", pref.getDate().toString())

            }
        })
    }

    fun saveAppointmentToFirebase(path: String, path1: String, pickedDate: String, pickedDate1: String, city: String, position: String, hospital: String, examination: String, userUid: String) {
        val pref = MyPreference(MyApplication.ApplicationContext)
        val ref = FirebaseDatabase.getInstance().getReference(path)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("CalendarActivity", "Cancelled")
            }

            override fun onDataChange(p0: DataSnapshot) {
                val username1 = pref.getUsername()


                val username = p0.getValue(String::class.java)!!
                pref.setUsername(username)

                Log.d("date", pickedDate)


                val ref = FirebaseDatabase.getInstance()
                    .getReference("cities/$city/$hospital/$examination/${pickedDate}/$userUid")

                val userAppointemnt = UserAppointment(username1!!, pickedDate1, hospital!!, examination!!)
                ref.setValue(userAppointemnt).addOnSuccessListener {
                    Log.d("CalendarActivity", "Ok")
                    Log.d(
                        "CalendarActivity",
                        "cities/$city/$hospital/$position/$examination/$userUid"
                    )
                }
                    .addOnFailureListener {
                        Log.d("CalendarActivity", "Error")
                    }

            }

        })

    }

    fun getHospotalName(path: String, address: String, fetchHospitalsName: FetchHospitalsName) {

        val ref = FirebaseDatabase.getInstance().getReference(path)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(h in p0.children) {
                    val hospital = h.getValue(Hospital::class.java)

                    if(address == hospital!!.address) {
                        fetchHospitalsName.getHospitalName(hospital.hositalName)
                        Log.d("DataClass", hospital.address)
                    }
                }
            }
        })
    }
}