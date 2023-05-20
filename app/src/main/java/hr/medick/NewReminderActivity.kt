package hr.medick

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import hr.medick.HostActivity.Companion.listOfPodsjetniks
import hr.medick.HostActivity.Companion.osoba
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

        initComponents()
        binding.datePickerEditText.inputType = InputType.TYPE_NULL

    }

    private fun initComponents() {
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

        println("osobaPacijent$osobaPacijent")

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
                            val podsjetnik: Podsjetnik? =
                                gson.fromJson(responseBody!!.string(), Podsjetnik::class.java)

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

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this, { view, year, monthOfYear, dayOfMonth ->
                // Do something with the selected date
                val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                binding.datePickerEditText.setText(selectedDate)
            }, year, month, day
        )

        datePickerDialog.show()
    }
}