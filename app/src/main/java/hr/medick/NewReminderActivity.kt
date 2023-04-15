package hr.medick

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import com.google.gson.Gson
import hr.medick.databinding.ActivityNewReminderBinding
import hr.medick.model.Osoba
import hr.medick.model.Podsjetnik
import hr.medick.properties.UrlProperties
import okhttp3.*
import java.io.IOException
import java.util.*

class NewReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewReminderBinding
    private var newReminderThread = Thread()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.datePickerEditText.inputType = InputType.TYPE_NULL

        binding.fabBack.setOnClickListener {

            val osobaPacijent : Osoba  = intent.getSerializableExtra("OsobaPacijent") as Osoba
            goBackToReminderActivity(osobaPacijent)
        }

        binding.datePickerEditText.setOnClickListener {
            showDatePickerDialog()
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
                putaDnevno.toString(), tableta.toString(),
                satiRazmaka.toString(), datumPrvogUzimanja.toString()
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


        val osobaPacijent : Osoba  = intent.getSerializableExtra("OsobaPacijent") as Osoba

        println("osobaPacijent$osobaPacijent")

        val requestBody = FormBody.Builder()
            .add("imeLijeka", imeLijeka)
            .add("dozaLijeka", dozaLijeka)
            .add("putaDnevno", putaDnevno)
            .add("tableta", tableta)
            .add("satiRazmaka", satiRazmaka)
            .add("datumPrvogUzimanja", datumPrvogUzimanja)
            .add("osobaPacijentId", osobaPacijent.id.toString())
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

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
                            val podsjetnik: Podsjetnik? =
                                gson.fromJson(responseBody!!.string(), Podsjetnik::class.java)

                            println(podsjetnik)
                            openReminderActivity(osobaPacijent)

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

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Do something with the selected date
                val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                binding.datePickerEditText.setText(selectedDate)
            }, year, month, day
        )

        datePickerDialog.show()
    }

    private fun goBackToReminderActivity(osobaPacijent: Osoba) {
        val intent = Intent(this, ReminderActivity::class.java)
        println("Current osoba$osobaPacijent")
        intent.putExtra("OsobaPacijent", osobaPacijent)
        startActivity(intent)
    }

    private fun openReminderActivity(osobaPacijent: Osoba) {
        val intent = Intent(this, ReminderActivity::class.java)
        println("Current osoba$osobaPacijent")
        intent.putExtra("OsobaPacijent", osobaPacijent)
        startActivity(intent)
    }
}