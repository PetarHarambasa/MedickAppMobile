package hr.medick

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.medick.databinding.ActivityNewReminderBinding
import hr.medick.fragments.reminder.MedicationNameFragment
import hr.medick.model.Osoba
import hr.medick.model.Podsjetnik
import hr.medick.properties.UrlProperties.IP_ADDRESS
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.lang.reflect.Type
import java.util.*

class NewReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewReminderBinding
    private var newReminderThread = Thread()

    var podsjetnikList: List<Podsjetnik> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //opens the 1st fragment
        supportFragmentManager.commit {
            replace<MedicationNameFragment>(R.id.fragmentContianer)
            setReorderingAllowed(true)
            addToBackStack(null)
        }

        val intentReminderActivity = Intent(this, ReminderActivity::class.java)

        podsjetnikList = intent.getParcelableArrayListExtra("PodsjetnikList")!!

//        binding.datePickerEditText.inputType = InputType.TYPE_NULL
//
//        binding.fabBack.setOnClickListener {
//
//            val osobaPacijent: Osoba = intent.getSerializableExtra("OsobaPacijent") as Osoba
//            goBackToReminderActivity(
//                osobaPacijent,
//                intentReminderActivity,
//                podsjetnikList
//            )
//        }
//
//        binding.datePickerEditText.setOnClickListener {
//            showDatePickerDialog()
//        }
//
//        binding.addNewReminder.setOnClickListener {
//            val imeLijeka = binding.imeLijekaEditText.text
//            val dozaLijeka = binding.dozaLijekaEditText.text
//            val putaDnevno = binding.putaDnevnoEditText.text
//            val tableta = binding.tabletaEditText.text
//            val satiRazmaka = binding.satiRazmakaEditText.text
//            val datumPrvogUzimanja = binding.datePickerEditText.text
//
//            val urlMobileSaveNewReminder = "http://${IP_ADDRESS}:8080/mobileSaveNewReminder"
//
//            saveNewReminder(
//                urlMobileSaveNewReminder,
//                imeLijeka.toString(),
//                dozaLijeka.toString(),
//                putaDnevno.toString(),
//                tableta.toString(),
//                satiRazmaka.toString(),
//                datumPrvogUzimanja.toString(),
//                intentReminderActivity
//            )
//        }
    }

    private fun saveNewReminder(
        urlMobileSaveNewReminder: String,
        imeLijeka: String,
        dozaLijeka: String,
        putaDnevno: String,
        tableta: String,
        satiRazmaka: String,
        datumPrvogUzimanja: String,
        intentReminderActivity: Intent,
    ) {

        val client = OkHttpClient()

//        val osobaPacijent: Osoba = intent.getSerializableExtra("OsobaPacijent") as Osoba

//        println("osobaPacijent$osobaPacijent")
//
//        val requestBody = FormBody.Builder()
//            .add("imeLijeka", imeLijeka)
//            .add("dozaLijeka", dozaLijeka)
//            .add("putaDnevno", putaDnevno)
//            .add("tableta", tableta)
//            .add("satiRazmaka", satiRazmaka)
//            .add("datumPrvogUzimanja", datumPrvogUzimanja)
//            .add("osobaPacijentId", osobaPacijent.id.toString())
//            .build()

//        val request = Request.Builder()
//            .url(urlMobileSaveNewReminder)
//            .post(requestBody)
//            .build()

//        newReminderThread = Thread {
//            try {
//                // Your network activity
//                val result = client.newCall(request).enqueue(object : Callback {
//                    override fun onFailure(call: Call, e: IOException) {
//                        println(e)
//                    }
//
//                    override fun onResponse(call: Call, response: Response) {
//                        if (response.isSuccessful) {
//                            val gson = Gson()
//                            val responseBody = client.newCall(request).execute().body
//                            val podsjetnik: Podsjetnik? =
//                                gson.fromJson(responseBody!!.string(), Podsjetnik::class.java)
//
//                            println("SPREMLJENPODSJETNIK$podsjetnik")
//                            openReminderActivity(
//                                osobaPacijent,
//                                intentReminderActivity
//                            )
//
//                        }
//                        println(response)
//                    }
//                })
//                println(result)
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//            }
//        }
        newReminderThread.start()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->
                // Do something with the selected date
                val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
//                binding.datePickerEditText.setText(selectedDate)
            }, year, month, day
        )

        datePickerDialog.show()
    }

    private fun goBackToReminderActivity(
        osobaPacijent: Osoba,
        intentReminderActivity: Intent,
        podsjetnikList: List<Podsjetnik>
    ) {
        println("Current osoba$osobaPacijent")
        println("PodsjetnikList$podsjetnikList")
        intentReminderActivity.putExtra("OsobaPacijent", osobaPacijent)
        intentReminderActivity.putExtra("PodsjetnikList", ArrayList(podsjetnikList))
//        startActivity(intentReminderActivity)
    }

    private fun loadRemindersIntoList(
        urlMobileReminders: String,
        osoba: Osoba,
        intentReminderActivity: Intent
    ) {
        val client = OkHttpClient()

        val json = Gson().toJson(osoba)
        println(json)
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json)

        val request = Request.Builder()
            .url(urlMobileReminders)
            .post(requestBody)
            .build()

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

                        val type: Type = object : TypeToken<List<Podsjetnik?>?>() {}.type
//                        podsjetnikList = gson.fromJson(responseBody!!.string(), type)
//                        println("DrugiPustlIst$podsjetnikList")
                        //addPodsjtenikListToIntentExtra(intentReminderActivity)
                    }
                    println(response)
                }

            })
            println(result)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun addPodsjtenikListToIntentExtra(intentReminderActivity: Intent) {
//        intentReminderActivity.putExtra("PodsjetnikList", ArrayList(podsjetnikList))
//        println("KOAJJELISTA:$podsjetnikList")
//
//        startActivity(intentReminderActivity)
    }

    private fun openReminderActivity(osobaPacijent: Osoba, intentReminderActivity: Intent) {
        val urlMobileReminders = "http://${IP_ADDRESS}:8080/mobileReminders"
        loadRemindersIntoList(urlMobileReminders, osobaPacijent, intentReminderActivity)

        println("Current osoba$osobaPacijent")
        intentReminderActivity.putExtra("OsobaPacijent", osobaPacijent)
    }
}