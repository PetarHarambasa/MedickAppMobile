package hr.medick

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.medick.databinding.ActivityUserProfileBinding
import hr.medick.model.Osoba
import hr.medick.model.Podsjetnik
import hr.medick.model.Vitali
import hr.medick.properties.UrlProperties
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.lang.reflect.Type

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding

    var vitaliList: List<Vitali> = ArrayList()

    var podsjetnikList: List<Podsjetnik> = ArrayList()

    private var isEditSuccessful: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listOfPodsjetniks: List<Podsjetnik> =
            intent.getParcelableArrayListExtra("PodsjetnikList")!!

        val intentReminder = Intent(this, ReminderActivity::class.java)

        val osoba: Osoba = intent.getSerializableExtra("OsobaPacijent") as Osoba

        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bootomnNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
//                    R.id.podsjetnikList ->
//                        openReminderActivity(osoba, intentReminder)
//                    R.id.vitaliList ->
//                        openVitaliActivity(osoba, listOfPodsjetniks)
//                    R.id.userProfile ->
//                        return@OnNavigationItemSelectedListener true
                }
                false
            })




        binding.imeEditText.setText(osoba.ime)
        binding.prezimeEditText.setText(osoba.prezime)
        binding.emailEditText.setText(osoba.email)
        binding.telefonEditText.setText(osoba.telefon)
        binding.adresaStanovanjaEditText.setText(osoba.adresaStanovanja)

        binding.saveUserProfile.setOnClickListener {
            val ime = binding.imeEditText.text
            val prezime = binding.prezimeEditText.text
            val email = binding.emailEditText.text
            val telefon = binding.telefonEditText.text
            val adresaStanovanja = binding.adresaStanovanjaEditText.text
            val lozinka = binding.lozinkaEditText.text

            val url = "http://${UrlProperties.IP_ADDRESS}:8080/mobileEditPacijent"
            val newDataOfOsoba = Osoba(
                osoba.id,
                ime.toString(),
                prezime.toString(),
                email.toString(),
                telefon.toString(),
                adresaStanovanja.toString(),
                lozinka.toString()
            )

            // Validate user input
            if (ime.isEmpty() || prezime.isEmpty() || email.isEmpty() || telefon.isEmpty() || adresaStanovanja.isEmpty() || lozinka.isEmpty()) {
                Toast.makeText(this, "Molim, popunite sva polja", Toast.LENGTH_SHORT).show()
            } else if (!email.contains("@")) {
                Toast.makeText(this, "Molim, upisite ispravan email", Toast.LENGTH_SHORT).show()
            } else {
                saveData(url, newDataOfOsoba)
                if (isEditSuccessful) {
                    Toast.makeText(
                        this, "Spremljene promjene, molim ponovno se" +
                                " ulogirajte kako bi se promjene primjenile ", Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(this, "Email već postoji!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveData(url: String, newDataOfOsoba: Osoba) {
        val client = OkHttpClient()

        val json = Gson().toJson(newDataOfOsoba)
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            // Your network activity
            val result = client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = client.newCall(request).execute().body
                    val stringCheck = responseBody?.string()
                    if (response.code == 200) {
                        println(stringCheck)
                        isEditSuccessful = false
                        println(response.message)
                    } else {
                        println(stringCheck)
                        isEditSuccessful = true
                        println(response.message)
                    }
                }
            })
            println(result)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun openReminderActivity(osoba: Osoba, intentReminderActivity: Intent) {
        intentReminderActivity.putExtra("OsobaPacijent", osoba)
        podsjetnikList = intent.getParcelableArrayListExtra("PodsjetnikList")!!
        intentReminderActivity.putExtra("PodsjetnikList", ArrayList(podsjetnikList))

        startActivity(intentReminderActivity)
    }

    private fun openVitaliActivity(osoba: Osoba, listOfPodsjetniks: List<Podsjetnik>) {
        val intentVitals = Intent(this, VitalsActivity::class.java)
        val urlMobileReminders = "http://${UrlProperties.IP_ADDRESS}:8080/mobileVitals"
        intentVitals.putExtra("OsobaPacijent", osoba)
        intentVitals.putExtra("PodsjetnikList", ArrayList(listOfPodsjetniks))
        loadVitalsIntoList(osoba, urlMobileReminders, intentVitals)
    }

    private fun loadVitalsIntoList(
        osoba: Osoba,
        url: String,
        intentVitalsActivity: Intent,
    ) {

        val client = OkHttpClient()

        val json = Gson().toJson(osoba)
        println(json)
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json)

        val request = Request.Builder()
            .url(url)
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

                        val type: Type = object : TypeToken<List<Vitali?>?>() {}.type
                        vitaliList = gson.fromJson(responseBody!!.string(), type)

                        addVitaliListToIntentExtra(intentVitalsActivity)
                    }
                    println(response)
                }

            })
            println(result)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun addVitaliListToIntentExtra(intentVitalsActivity: Intent) {
        intentVitalsActivity.putExtra("VitaliList", ArrayList(vitaliList))

        startActivity(intentVitalsActivity)
    }
}