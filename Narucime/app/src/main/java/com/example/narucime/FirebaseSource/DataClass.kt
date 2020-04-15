package com.example.narucime.FirebaseSource

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.narucime.Context.MyApplication
import com.example.narucime.Model.Hospital
import com.example.narucime.Model.UserAppointment
import com.example.narucime.SharedPreferences.MyPreference
import com.example.narucime.View.RecyclerViewClass
import com.example.narucime.View.adapters.recyclerviewAdapters.AppointemnetAdapter
import com.example.narucime.View.adapters.recyclerviewAdapters.CityAdapter
import com.example.narucime.View.adapters.recyclerviewAdapters.ExaminationAdapter
import com.example.narucime.View.adapters.recyclerviewAdapters.HospitalAdapter
import com.example.narucime.View.listeners.FetchAllHospitals
import com.example.narucime.View.listeners.FetchHospitalsName
import com.example.narucime.View.listeners.FetchMaxTimes
import com.example.narucime.View.listeners.OnGetDataListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class DataClass {
    lateinit var listOfUserAppointment: MutableList<UserAppointment>
    lateinit var list: MutableList<String>
    lateinit var cityList: MutableList<String>
    lateinit var dates: MutableList<String>
    lateinit var listOfHospital: MutableList<String>
    lateinit var listOfTimes: MutableList<String>

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
                cityList = mutableListOf()

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
                                        cityList.add(c.key.toString())
                                        Log.d("Kojigrad", c.key.toString())
                                    }
                                }
                            }

                            val l = LocalDate.parse("14/2/2018", DateTimeFormatter.ofPattern("d/M/yyyy"))

                            /*listOfUserAppointment.sortBy{
                                LocalDate.parse(it.date, DateTimeFormatter.ofPattern("d/M/yyyy"))
                            }*/


                            Log.d("stojeovosada", l.toString())

                            /*val recycler = RecyclerViewClass()
                            val adapter: RecyclerView.Adapter<*> =
                                AppointemnetAdapter(
                                    listOfUserAppointment,
                                    cityList
                                )
                            recycler.createRecyclerView(recyclerView, adapter)*/
                        }
                    }
                }

                listOfUserAppointment.sortBy{
                    LocalDate.parse(it.date, DateTimeFormatter.ofPattern("d/M/yyyy"))
                }

                val recycler = RecyclerViewClass()
                val adapter: RecyclerView.Adapter<*> =
                    AppointemnetAdapter(
                        listOfUserAppointment,
                        cityList
                    )
                recycler.createRecyclerView(recyclerView, adapter)
            }
        })
    }

    fun getCitiesFormFirebase(recyclerView: RecyclerView, path: String){
        val ref = FirebaseDatabase.getInstance().getReference(path)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                cityList = mutableListOf()

                for(c in p0.children) {
                    cityList.add(c.key.toString())
                }

                cityList.sortBy{ it }

                val recycler = RecyclerViewClass()
                val adapter: RecyclerView.Adapter<*> =
                    CityAdapter(
                        cityList
                    )
                recycler.createRecyclerView(recyclerView, adapter)
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
                //list.clear()
                for (h in p0.children) {
                    val numOfApp = h.childrenCount.toString()
                    for (n in h.children) {
                        Log.d("key123", n.childrenCount.toString())

                        val username = n.getValue(UserAppointment::class.java)

                        if (username?.date != null) {
                            if (numOfApp == "7" || username.username == pref.getUsername()) {
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
                val TimeRef = "cities/$city/$hospital/$examination/${pickedDate}"

                getTime(TimeRef, object : FetchMaxTimes {
                    override fun getMaxTime(maxTime: String) {
                        val userAppointemnt = UserAppointment(
                            username1!!,
                            pickedDate1,
                            hospital!!,
                            examination!!,
                            maxTime
                        )
                        ref.setValue(userAppointemnt).addOnSuccessListener {
                            Log.d("CalendarActivity", "Ok")
                            Log.d(
                                "CalendarActivity",
                                "cities/$city/$hospital/$position/$examination/$userUid"
                            )

                            return@addOnSuccessListener
                        }
                            .addOnFailureListener {
                                Log.d("CalendarActivity", "Error")
                            }
                    }

                })
                /*val userAppointemnt = UserAppointment(
                    username1!!,
                    pickedDate1,
                    hospital!!,
                    examination!!,
                    20.toString()
                )
                ref.setValue(userAppointemnt).addOnSuccessListener {
                    Log.d("CalendarActivity", "Ok")
                    Log.d(
                        "CalendarActivity",
                        "cities/$city/$hospital/$position/$examination/$userUid"
                    )

                    return@addOnSuccessListener
                }
                    .addOnFailureListener {
                        Log.d("CalendarActivity", "Error")
                    }*/

                }
        })
    }

    fun getTime(ref: String, fetchMaxTimes: FetchMaxTimes) {
        val pref = MyPreference(MyApplication.ApplicationContext)
        val ref = FirebaseDatabase.getInstance().getReference(ref)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                listOfTimes = mutableListOf()
                for(i in p0.children) {
                    val time = i.getValue(UserAppointment::class.java)

                    listOfTimes.add(time!!.time)

                    Log.d("TimeMyTime", time!!.time.toString())
                }

                val maxTime = listOfTimes.max()

                if(listOfTimes.isEmpty()) {
                    val newTime = "07:00"
                    fetchMaxTimes.getMaxTime(newTime)
                }
                else {

                    val newTime = LocalTime.parse(maxTime)
                    Log.d("TimeMyTime1", newTime.plusMinutes(30).toString())
                    fetchMaxTimes.getMaxTime(newTime.plusMinutes(30).toString())
                }
            }

        })

    }

    fun getHospotalName(path: String, address: String, fetchHospitalsName: FetchHospitalsName) {

        val ref = FirebaseDatabase.getInstance().getReference(path)

        //Log.d("Kojijetopath", address)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(h in p0.children) {
                    val hospital = h.getValue(Hospital::class.java)

                    if(address == hospital!!.address) {
                        Log.d("Kojijetopath", hospital!!.address)
                        fetchHospitalsName.getHospitalName(h.key)
                        Log.d("DataClassH", h.key!!)
                    }
                }
            }
        })
    }

    fun getAllHospitals(path: String, fetchAllHospitals: FetchAllHospitals) {
        val ref = FirebaseDatabase.getInstance().getReference(path)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                listOfHospital = mutableListOf()
                for(c in p0.children) {
                    for(h in c.children) {
                        listOfHospital.add(h.key!!)
                    }
                }
                fetchAllHospitals.getHospotals(listOfHospital)
            }

        })
    }

    fun deleteAppointemnt(path: String, date: String, examination: String, user: String, hospital: String) {
        val ref = FirebaseDatabase.getInstance().getReference(path)
        Log.d("Kojibrisem", path)

        ref.removeValue()

        /*ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(d in p0.children) {
                    val appointemnt = d.getValue(UserAppointment::class.java)

                    if(date == appointemnt!!.date && examination == appointemnt.examination && user == appointemnt.username && hospital == appointemnt.hospital) {
                        ref.removeValue()
                        Log.d("Kojibrisem", date)
                        return
                    }
                }
            }

        })*/
    }
}