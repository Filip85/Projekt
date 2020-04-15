package com.example.narucime.View.ui

import android.app.AlarmManager
import android.app.Dialog
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
import com.example.narucime.FirebaseSource.DataClass
import com.example.narucime.Notification.NotificationPublisher
import com.example.narucime.SharedPreferences.MyPreference
import com.google.firebase.auth.FirebaseAuth
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.notification_dialog.*
import kotlinx.android.synthetic.main.notification_dialog.confirm_button
import kotlinx.android.synthetic.main.set_up_notification_dialog.*
import java.lang.Double.parseDouble
import java.time.LocalDate
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
    lateinit var dialog: Dialog
    var firstNumber = 0
    var secondNumber = 0
    var thirdNumber = 0
    var num1: Double = 0.0
    var num2: Double = 0.0
    var num3: Double = 0.0

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

        this.title = "Appointment"

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

        confirmButton.setOnClickListener {
            Toast.makeText(this,"Please, pick your date", Toast.LENGTH_LONG).show()
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

        confirmButton.setOnClickListener(null)

        date.clear()

        val month = m + 1

        currentDate = "$d-$month-$y"

        val currentMonth = m

        Log.d("CalendarActivity", currentDate)

        val currentDatee = LocalDate.now()

        Log.d("koji mjesec", currentDatee.month.toString())

        datePicker = DatePickerDialog.newInstance(
            {view, year, monthOfYear, dayOfMounth ->
                if(monthOfYear == calInstance.get(Calendar.MONTH)) {
                    Log.d("CalendarActivity1", m.toString())
                }
                val pickedMonth = monthOfYear+1
                pickedDate = "$dayOfMounth-$pickedMonth-$year"
                pickedDate1 = "$dayOfMounth/$pickedMonth/$year"


                if(dayOfMounth > 15 && d > 15 && monthOfYear == currentMonth) {
                    Toast.makeText(this, "You can't make an appointment for this mounth. Please, make the appointmet for the next month.", Toast.LENGTH_LONG).show()
                }
                else if(dayOfMounth <= 15 && d <= 15 && monthOfYear == currentMonth) {
                    Toast.makeText(this, "You can't make an appointment for a first half of a mounth. Please, make the appointmet for the second half of the mounth.", Toast.LENGTH_LONG).show()
                }
                else {
                    cityTextView.text = city.toString()
                    hospitalTextView.text = hospital.toString()
                    textView6.text = examination.toString()
                    dateTextView.text = pickedDate1
                    confirmTextView.text = "Please, press CONFIRM button to confirm your appointment!"
                    confirmButton.setOnClickListener{
                        saveAppointement(pickedDate, pickedDate1, dayOfMounth, monthOfYear, year)
                    }
                }
            }, y, m, d
        ).also {
            it.minDate = Calendar.getInstance()
        }

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
    private fun saveAppointement(pickedDate: String, pickedDate1: String, day: Int, monthDay: Int, year: Int) {
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
        val path2 = "cities/$city/$hospital"

        Log.d("Gdjesenarucujem", path2)
        //val ref = FirebaseDatabase.getInstance().getReference("cities/$city/$hospital")

        path = "users/$userUid/username"
        path1 = "cities/$city/$hospital/$examination/${pickedDate}/$userUid"
        val data = DataClass()

        data.saveAppointmentToFirebase(path, path1, pickedDate, pickedDate1, city!!, position!!, hospital!!, examination!!, userUid!!)

        val content = "${city}, ${hospital}, ${examination}, ${pickedDate1}"


        notificationDialog(hospital, content, day, monthDay, year)

    }

    private fun notificationDialog(hospital: String, content: String, day: Int, monthDay: Int, year: Int) {

        val dialog = Dialog(this)
        dialog.setContentView(com.example.narucime.R.layout.notification_dialog)
        dialog.show()

        dialog.cancel_button.setOnClickListener {
            dialog.cancel()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        dialog.confirm_button.setOnClickListener {
            scheduleNotifaction(hospital ,content, day, monthDay, year, 1, 3, 7)
            dialog.cancel()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        dialog.set_up_notifications.setOnClickListener {
            dialog.cancel()
            val dialog = Dialog(this)
            dialog.setContentView(com.example.narucime.R.layout.set_up_notification_dialog)
            dialog.show()

            dialog.confirm_button.setOnClickListener {
                firstNumber = dialog.first_number.text.toString().toInt()
                secondNumber = dialog.second_number.text.toString().toInt()
                thirdNumber = dialog.third_number.text.toString().toInt()

                Log.d("Firtnumberuno", secondNumber.toString())
                var numeric = true


                try {
                    num1 = parseDouble(firstNumber.toString())
                    num2 = parseDouble(secondNumber.toString())
                    num3 = parseDouble(thirdNumber.toString())
                } catch (e: NumberFormatException) {
                    numeric = false
                }

                scheduleNotifaction(hospital ,content, day, monthDay, year, num1.toInt(), num2.toInt(), num3.toInt())
                dialog.cancel()

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)

                /*if(numeric == false) {
                    Toast.makeText(this, "Please, enter numeric value!", Toast.LENGTH_LONG).show()
                }
                else {
                    scheduleNotifaction(hospital ,content, day, monthDay, year, num1.toInt(), num2.toInt(), num3.toInt())
                    dialog.cancel()
                }*/
            }

            dialog.go_back.setOnClickListener {
                dialog.cancel()
                notificationDialog(hospital ,content, day, monthDay, year)
            }
        }
    }

    private fun scheduleNotifaction(hospital: String, content: String, day: Int, month: Int, year: Int, firstNumber: Int, secondNumber: Int, thirdNumber: Int) {
        val notificationIntent1 = intentFuncton(content)
        val notificationIntent2 = intentFuncton(content)
        val notificationIntent3 = intentFuncton(content)

        val calendar1 = calendarFunction(day - firstNumber, month, year)
        val calendar2 = calendarFunction(day - secondNumber, month, year)
        val calendar3 = calendarFunction(day - thirdNumber, month, year)

        val pendingIntent1 = createPendingIntent(notificationIntent1)
        val pendingIntent2 = createPendingIntent(notificationIntent2)
        val pendingIntent3 = createPendingIntent(notificationIntent3)

        Log.d("MyTime", System.currentTimeMillis().toString())

        val time =  calendar1.timeInMillis - System.currentTimeMillis()
        val time2 =  calendar2.timeInMillis - System.currentTimeMillis()
        val time3 =  calendar3.timeInMillis - System.currentTimeMillis()

        Log.d("TimeMy", time.toString())

        alarmFunction(time, pendingIntent1)
        alarmFunction(time2, pendingIntent2)
        alarmFunction(time3, pendingIntent3)
    }

    private fun intentFuncton(content: String): Intent {
        val notificationIntent = Intent(this, NotificationPublisher::class.java)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATON_TITLE, "You have an appintment.")
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_CONTENT, content)

        return notificationIntent

    }

    private fun calendarFunction(day: Int, month: Int, year: Int): Calendar {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 38)
            set(Calendar.SECOND, 30)
        }

        return calendar
    }

    private fun alarmFunction(time: Long, pendingIntent: PendingIntent){
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + time, pendingIntent)
    }

    private fun createPendingIntent(notificationIntent: Intent): PendingIntent{
        val randomId = Random.nextInt(1, 10000)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            randomId,                         //staviti id datum npr 2232020  22/3/2020
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        return pendingIntent
    }

}
