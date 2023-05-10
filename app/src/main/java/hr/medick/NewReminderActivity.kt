package hr.medick

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.medick.databinding.ActivityNewReminderBinding
import hr.medick.fragments.reminder.MedicationNameFragment
import hr.medick.model.Osoba
import hr.medick.model.Podsjetnik
import hr.medick.properties.UrlProperties
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

        initComponents()
        binding.datePickerEditText.inputType = InputType.TYPE_NULL

//       This option is if we want to kill the activity after the last fragment
//        supportFragmentManager.addOnBackStackChangedListener {
//            if (supportFragmentManager.backStackEntryCount == 0) {
//                finish()
//            }
//        }

        //opens the 1st fragment
//        supportFragmentManager.commit {
//            replace<MedicationNameFragment>(R.id.fragmentContianer)
//            setReorderingAllowed(true)
//            addToBackStack(null)
//        }

        val intentReminderActivity = Intent(this, ReminderActivity::class.java)

        podsjetnikList =
            HostActivity.listOfPodsjetniks//intent.getParcelableArrayListExtra("PodsjetnikList")!!
    }

    private fun initComponents() {
        val intentReminderActivity = Intent(this, ReminderActivity::class.java)
        binding.fabBack.setOnClickListener {

            val osobaPacijent: Osoba = intent.getSerializableExtra("OsobaPacijent") as Osoba
            goBackToReminderActivity(
                osobaPacijent,
                intentReminderActivity
            )
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
        binding.fabBack.setOnClickListener {
            val osobaPacijent: Osoba = HostActivity.osoba

            goBackToReminderActivity(osobaPacijent, intentReminderActivity)
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


        val osobaPacijent: Osoba = HostActivity.osoba//intent.getSerializableExtra("OsobaPacijent") as Osoba

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

        val datePickerDialog = DatePickerDialog(
            this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Do something with the selected date
                val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                binding.datePickerEditText.setText(selectedDate)
            }, year, month, day
        )

        datePickerDialog.show()
    }

    private fun goBackToReminderActivity(
        osobaPacijent: Osoba, intentReminderActivity: Intent
//        ,
//        podsjetnikList: List<Podsjetnik>
    ) {
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