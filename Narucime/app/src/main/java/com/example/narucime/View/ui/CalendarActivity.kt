package com.example.narucime.View.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.narucime.Model.DataClass
import com.example.narucime.Model.UserAppointment
import com.example.narucime.MyApplication
import com.example.narucime.NotificationPublisher
import com.example.narucime.SharedPreferences.MyPreference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_calendar.*
import java.util.*
import kotlin.random.Random


class CalendarActivity : AppCompatActivity() {

    var days: Array<Calendar> = arrayOf()
    val blockedDays: MutableList<Calendar> = mutableListOf()
    var date: MutableList<String> = mutableListOf()
    var datePicker: com.wdullaer.materialdatetimepicker.date.DatePickerDialog = DatePickerDialog()
    lateinit var currentDate: String
    lateinit var pickedDate: String
    lateinit var pickedDate1: String
    lateinit var path: String
    lateinit var path1: String

    companion object {
        const val POSITION = "position"
        const val HOSPITALNAME = "hospital"
        const val CITY = "city"
        const val EXAMINATION = "examination"
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.narucime.R.layout.activity_calendar)

        val calInstance = Calendar.getInstance()
        val y = calInstance.get(Calendar.YEAR)
        val m = calInstance.get(Calendar.MONTH)
        val d = calInstance.get(Calendar.DAY_OF_MONTH)

        val month = m + 1

        currentDate = "$d/$month/$y"

        //Thread.sleep(1000)
        disabledDays()

        datePickerButton.setOnClickListener{
            setUpUi(calInstance, y, m, d)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setUpUi(calInstance: Calendar, y: Int, m: Int, d: Int) {
        val city = intent?.getStringExtra(
            CITY ?: "nothing recieved")
        val hospital = intent?.getStringExtra(
            HOSPITALNAME ?: "nothing recieved")
        val examination = intent?.getStringExtra(
            EXAMINATION ?: "nothing recieved")

        date.clear()

        val month = m + 1

        currentDate = "$d-$month-$y"

        Log.d("CalendarActivity", currentDate)


        datePicker = DatePickerDialog.newInstance(
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
                    cityTextView.text = city.toString()
                    hospitalTextView.text = hospital.toString()
                    textView6.text = examination.toString()
                    dateTextView.text = pickedDate1
                    confirmTextView.text = "Please, press CONFIRM button to confirm your appointment!"
                    confirmButton.setOnClickListener{
                        saveAppointement(pickedDate, pickedDate1)
                    }
                }
            }, y, m, d
        )

        blockDates(currentDate, datePicker)

        datePicker.show(supportFragmentManager, "Date picker")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun disabledDays() {
        val city = intent?.getStringExtra(
            CITY ?: "nothing recieved")
        val hospital = intent?.getStringExtra(
            HOSPITALNAME ?: "nothing recieved")
        val examination = intent?.getStringExtra(
            EXAMINATION ?: "nothing recieved")

        path = "cities/$city/$hospital/$examination"

        val data = DataClass()
        data.getDatesFromFirebase(path)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun blockDates(currentDate: String, datePicker: DatePickerDialog) {
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
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun saveAppointement(pickedDate: String, pickedDate1: String) {
        val position = intent?.getStringExtra(
            POSITION ?: "nothing recieved")
        val city = intent?.getStringExtra(
            CITY ?: "nothing recieved")
        val hospital = intent?.getStringExtra(
            HOSPITALNAME ?: "nothing recieved")
        val examination = intent?.getStringExtra(
            EXAMINATION ?: "nothing recieved")


        val uid = FirebaseAuth.getInstance().currentUser
        val userUid = uid?.uid

        val ref = FirebaseDatabase.getInstance().getReference("cities/$city/$hospital")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(MyApplication.ApplicationContext, "Error occurred. Please, try again later.", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                val pref = MyPreference(this@CalendarActivity)
                val username = pref.getUsername()
                var temp = false
                for (h in p0.children) {
                    for (n in h.children) {
                        for (s in n.children) {
                            val numOfApp = s.getValue(UserAppointment::class.java)
                            //Log.d("Filip", numOfApp?.date)
                            if (numOfApp?.date == pickedDate1 && numOfApp?.username == username) {
                                temp = true
                                Log.d("Filip1", temp.toString())
                            }
                        }
                    }
                }

                if (temp == false) {

                    path = "users/$userUid/username"
                    path1 = "cities/$city/$hospital/$examination/${pickedDate}/$userUid"
                    val data = DataClass()

                    data.saveAppointmentToFirebase(path, path1, pickedDate, pickedDate1, city!!, position!!, hospital!!, examination!!, userUid!!)

                    scheduleNotifaction(hospital ,pickedDate1)

                    val intent = Intent(this@CalendarActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        })
    }

    private fun scheduleNotifaction(title: String, content: String) {
        val randomId = Random.nextInt(1, 100)
        val notificationIntent = Intent(this, NotificationPublisher::class.java)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, randomId)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATON_TITLE, title)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_CONTENT, content)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.DAY_OF_YEAR, 12)
            set(Calendar.MONTH, 0)
            set(Calendar.YEAR, 2020)
            set(Calendar.HOUR_OF_DAY, 16)
            set(Calendar.MINUTE, 3)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            randomId,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        Log.d("MyTime", System.currentTimeMillis().toString())

        val time = calendar.timeInMillis - System.currentTimeMillis()

        val futureInMillis = SystemClock.elapsedRealtime() + time
        Log.d("TimeMy", futureInMillis.toString())
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent)
    }
}
