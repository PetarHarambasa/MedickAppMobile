package hr.medick

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import hr.medick.HostActivity.Companion.osoba
import hr.medick.databinding.ActivityNewReminderBinding
import hr.medick.model.Osoba
import hr.medick.model.Podsjetnik
import hr.medick.notification.PopupNotificationService
import hr.medick.properties.UrlProperties
import okhttp3.*
import java.io.IOException
import java.util.*

class NewReminderActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityNewReminderBinding
    private var newReminderThread = Thread()

    companion object {
        lateinit var newPodsjetnik: Podsjetnik
    }

    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0

    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        binding.datePickerEditText.inputType = InputType.TYPE_NULL

    }

    private fun initComponents() {
        binding.datePickerEditText.setOnClickListener {
            showDateAndTimePickerDialog()
        }

        binding.addNewReminder.setOnClickListener {
            val imeLijeka = binding.imeLijekaEditText.text
            val dozaLijeka = binding.dozaLijekaEditText.text
            val putaDnevno = binding.putaDnevnoEditText.text
            val tableta = binding.tabletaEditText.text
            val satiRazmaka = binding.satiRazmakaEditText.text
            val datumPrvogUzimanja = binding.datePickerEditText.text

            val url = "http://${UrlProperties.IP_ADDRESS}:8080/mobileSaveNewReminder"

            saveNewReminder(
                url,
                imeLijeka.toString(),
                dozaLijeka.toString(),
                putaDnevno.toString(),
                tableta.toString(),
                satiRazmaka.toString(),
                datumPrvogUzimanja.toString()
            )
        }
        binding.fabBack.setOnClickListener {
            finish()
        }
        binding.datePickerEditText.setOnClickListener {
            showDateAndTimePickerDialog()
        }

        binding.addNewReminder.setOnClickListener {
            val imeLijeka = binding.imeLijekaEditText.text
            val dozaLijeka = binding.dozaLijekaEditText.text
            val putaDnevno = binding.putaDnevnoEditText.text
            val tableta = binding.tabletaEditText.text
            val satiRazmaka = binding.satiRazmakaEditText.text
            val datumPrvogUzimanja = binding.datePickerEditText.text

            val url = "http://${UrlProperties.IP_ADDRESS}:8080/mobileSaveNewReminder"

            saveNewReminder(
                url,
                imeLijeka.toString(),
                dozaLijeka.toString(),
                putaDnevno.toString(),
                tableta.toString(),
                satiRazmaka.toString(),
                datumPrvogUzimanja.toString()
            )
        }
    }


    private fun saveNewReminder(
        url: String,
        imeLijeka: String,
        dozaLijeka: String,
        putaDnevno: String,
        tableta: String,
        satiRazmaka: String,
        datumPrvogUzimanja: String
    ) {

        val client = OkHttpClient()

        val osobaPacijent: Osoba =
            osoba

        val requestBody =
            FormBody.Builder().add("imeLijeka", imeLijeka).add("dozaLijeka", dozaLijeka)
                .add("putaDnevno", putaDnevno).add("tableta", tableta)
                .add("satiRazmaka", satiRazmaka)
                .add("datumPrvogUzimanja", datumPrvogUzimanja)
                .add("osobaPacijentId", osobaPacijent.id.toString()).build()

        val request = Request.Builder().url(url).post(requestBody).build()

        newReminderThread = Thread {
            try {
                // Your network activity
                val result = client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            val gson = Gson()
                            val responseBody = client.newCall(request).execute().body
                            newPodsjetnik =
                                gson.fromJson(responseBody!!.string(), Podsjetnik::class.java)

                            val notificationTime = Calendar.getInstance()
                            notificationTime.set(Calendar.YEAR, myYear) // Set the desired year
                            notificationTime.set(
                                Calendar.MONTH,
                                myMonth
                            ) // Set the desired month (0-11)
                            notificationTime.set(
                                Calendar.DAY_OF_MONTH,
                                myDay
                            ) // Set the desired day
                            notificationTime.set(
                                Calendar.HOUR_OF_DAY,
                                myHour
                            ) // Set the desired hour (in 24-hour format)

                            notificationTime.set(
                                Calendar.MINUTE,
                                myMinute
                            ) // Set the desired minute

                            scheduleNotification(applicationContext, notificationTime)

                            reloadRemindersList()

                        }
                        println(response)
                    }
                })
                println(result)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        newReminderThread.start()
    }

    private fun reloadRemindersList() {
        val hostActivity = Intent(baseContext, HostActivity::class.java)
        hostActivity.action = "ACTION_CALL_FUNCTION"
        startActivityForResult(hostActivity, 1)
    }

    fun scheduleNotification(context: Context, notificationTime: Calendar) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

//        val intent = Intent(context, FirebaseMessagingService::class.java)
        val intent = Intent(context, PopupNotificationService::class.java)

        val pendingIntent =
            PendingIntent.getService(context, 0, intent, 0)

//        val serviceIntent = Intent(context, PopupNotificationService::class.java)
//        ContextCompat.startForegroundService(context, serviceIntent)

        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime.timeInMillis, pendingIntent)
    }

    private fun showDateAndTimePickerDialog() {

        val calendar: Calendar = Calendar.getInstance()

        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, this, year, month, day)

        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myYear = year
        myMonth = month
        myDay = dayOfMonth

        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this, this, hour, minute,
            DateFormat.is24HourFormat(this)
        )
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute

        val selectedDate: String = if (myMinute < 10) {
            "$myDay/${myMonth + 1}/$myYear $myHour:0$myMinute"
        } else if (myHour < 10) {
            "$myDay/${myMonth + 1}/$myYear 0$myHour:$myMinute"
        } else if (myMinute < 10 && myHour < 10) {
            "$myDay/${myMonth + 1}/$myYear 0$myHour:0$myMinute"
        } else {
            "$myDay/${myMonth + 1}/$myYear $myHour:$myMinute"
        }

        binding.datePickerEditText.setText(selectedDate)
    }
}