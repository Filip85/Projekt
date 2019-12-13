package com.example.narucime

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.narucime.Model.UserAppointment
import java.util.*
import com.example.narucime.SharedPreferences.MyPreference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_calendar.*


class CalendarActivity : AppCompatActivity() {

    var days: Array<Calendar> = arrayOf()
    val blockedDays: MutableList<Calendar> = mutableListOf()
    var date: MutableList<String> = mutableListOf()
    var datePicker: com.wdullaer.materialdatetimepicker.date.DatePickerDialog = DatePickerDialog()
    lateinit var currentDate: String
    lateinit var pickedDate: String
    lateinit var pickedDate1: String

    companion object {
        const val POSITION = "position"
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val calInstance = Calendar.getInstance()
        val y = calInstance.get(Calendar.YEAR)
        val m = calInstance.get(Calendar.MONTH)
        val d = calInstance.get(Calendar.DAY_OF_MONTH)

        val month = m + 1

        currentDate = "$d/$month/$y"

        //blockDates(currentDate, datePicker)

        Thread.sleep(1000)
        disabledDays()

        datePickerButton.setOnClickListener{
            setUpUi()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setUpUi() {

        date.clear()

        val calInstance = Calendar.getInstance()
        val y = calInstance.get(Calendar.YEAR)
        val m = calInstance.get(Calendar.MONTH)
        val d = calInstance.get(Calendar.DAY_OF_MONTH)

        val month = m + 1

        currentDate = "$d-$month-$y"

        Log.d("CalendarActivity", currentDate)

        //disabledDays()


        datePicker = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
            {view, year, monthOfYear, dayOfMounth ->
                if(monthOfYear == calInstance.get(Calendar.MONTH)) {
                    Log.d("CalendarActivity1", m.toString())
                }
                val pickedMonth = monthOfYear+1
                pickedDate = "$dayOfMounth-$pickedMonth-$year"
                pickedDate1 = "$dayOfMounth/$pickedMonth/$year"
                if(dayOfMounth > 15 && monthOfYear == m) {
                    Toast.makeText(this, "You can't make an appointment for this mounth. Please, make the appointemt for next month.", Toast.LENGTH_LONG).show()
                }
                else {
                    //saveAppointementToFirebase(pickedDate, pickedDate1)
                }
            }, y, m, d
        )

        //disabledDays()

        confirmButton.setOnClickListener{
            saveAppointementToFirebase(pickedDate, pickedDate1)
        }

        blockDates(currentDate, datePicker)

        datePicker.show(supportFragmentManager, "Date picker")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun disabledDays() {
        val pref = MyPreference(this)  //case 1, case 2 u funkciju da stavim
        val city = pref.getCityName()
        val hospital = pref.getHospitalName()
        val examination = pref.getExamination()
        val username = pref.getUsername()

        Log.d("Ivan", username)


        val ref1 = FirebaseDatabase.getInstance().getReference("cities/$city/$hospital/$examination")

        ref1.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                date.clear()
                for(h in p0.children) {
                    val numOfApp = h.childrenCount.toString()
                    for(n in h.children) {
                        Log.d("key123", n.childrenCount.toString())
                        val username = n.getValue(UserAppointment::class.java)

                        if(username?.date != null) {
                            if(numOfApp == "2" || username?.username == pref.getUsername()) {
                                date.add(username.date)
                            }
                        }
                    }
                }

                Log.d("SASA2", date.toString())
                pref.setDate(date)
                Log.d("SASA3", pref.getDate().toString())

            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getDatesValue(dates: MutableList<String>) {
        Log.d("SASA1", date.toString())
        //blockDates(dates, currentDate, datePicker)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun blockDates(currentDate: String, datePicker: com.wdullaer.materialdatetimepicker.date.DatePickerDialog) {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)

        //date.add()

        val pref = MyPreference(this)
        date = pref.getDate()!!

        val cal = Calendar.getInstance()
        for (day in date) {
            val date = formatter.parse(day)

            if(day != currentDate){
                cal.time = date
                blockedDays.add(cal)
                days = blockedDays.toTypedArray()
                datePicker.disabledDays = days
            }
        }

        blockedDays.clear()

        /*Log.d("CalendarActivity", date.toString())
        Log.d("CalendarActivity", days.contentToString())*/
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun saveAppointementToFirebase(pickedDate: String, pickedDate1: String) {
        val position = intent?.getStringExtra(POSITION ?: "nothing recieved")


        val uid = FirebaseAuth.getInstance().currentUser
        val userUid = uid?.uid

        val pref = MyPreference(this@CalendarActivity)

        val city = pref.getCityName()
        val hospital = pref.getHospitalName()

        pref.setAppointment("false")

        val ref1 = FirebaseDatabase.getInstance().getReference("cities/$city/$hospital")

        ref1.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                val pref = MyPreference(this@CalendarActivity)
                val username = pref.getUsername()
                //val date = pref.getDate()
                var temp = false
                for (h in p0.children) {
                    for (n in h.children) {
                        for (s in n.children) {
                            val numOfApp = s.getValue(UserAppointment::class.java)
                            //Log.d("Filip", numOfApp?.date)
                            if (numOfApp?.date == pickedDate1 && numOfApp?.username == username) {
                                temp = true
                                /*pref.setAppointment("true")
                                val temp = pref.getAppointment()*/
                                Log.d("Filip1", temp.toString())
                            }
                        }
                    }
                }

                if (temp == false) {
                    val ref = FirebaseDatabase.getInstance().getReference("users/$userUid/username")
                    //val ref = FirebaseDatabase.getInstance().getReference("users/$userUid/username")

                    ref.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            Log.d("CalendarActivity", "Cancelled")
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            val pref = MyPreference(this@CalendarActivity)

                            val city = pref.getCityName()
                            val hospital = pref.getHospitalName()
                            val examination = pref.getExamination()
                            val username1 = pref.getUsername()


                            val username = p0.getValue(String::class.java)!!
                            //val pref = MyPreference(this@CalendarActivity)
                            pref.setUsername(username)

                            /*examsList.add(username!!)*/
                            Log.d("CalendarActivity", username)
                            //val pref = MyPreference(this)  //case 1, case 2 u funkciju da stavim
                            /*val city = pref.getCityName()
                              val hospital = pref.getHospitalName()
                            val examination = pref.getExamination()
                            val username1 = pref.getUsername()*/

                            Log.d("date", pickedDate)


                            val ref = FirebaseDatabase.getInstance()
                                .getReference("cities/$city/$hospital/$examination/${pickedDate}/$userUid")

                            val userAppointemnt = UserAppointment(username1!!, pickedDate1)
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
            }

        })
    }

}
