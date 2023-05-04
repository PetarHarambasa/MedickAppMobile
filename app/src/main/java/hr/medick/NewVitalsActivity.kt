package hr.medick

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.medick.databinding.ActivityNewVitalsBinding
import hr.medick.model.Osoba
import hr.medick.model.Podsjetnik
import hr.medick.model.Vitali
import hr.medick.properties.UrlProperties
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.lang.reflect.Type

class NewVitalsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewVitalsBinding
    private var newVitalsThread = Thread()

    var podsjetnikList: List<Podsjetnik> = ArrayList()
    var vitaliList: List<Vitali> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewVitalsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentVitalsActivity = Intent(this, VitalsActivity::class.java)

        binding.fabBack.setOnClickListener {

            val osobaPacijent: Osoba = intent.getSerializableExtra("OsobaPacijent") as Osoba
            goBackToReminderActivity(
                osobaPacijent,
                intentVitalsActivity
            )
        }

        binding.addNewVitals.setOnClickListener {
            val krvniTlak = binding.krvniTlakEditText.text
            val glukozaUKrvi = binding.glukozaUKrviEditText.text

            val urlMobileSaveNewVitals =
                "http://${UrlProperties.IP_ADDRESS}:8080/mobileSaveNewVitals"

            saveNewVitals(
                urlMobileSaveNewVitals,
                krvniTlak.toString(),
                glukozaUKrvi.toString(),
                intentVitalsActivity
            )
        }
    }

    private fun saveNewVitals(
        urlMobileSaveNewVitals: String,
        krvniTlak: String,
        glukozaUKrvi: String,
        intentVitalsActivity: Intent
    ) {
        val client = OkHttpClient()

        val osobaPacijent: Osoba = intent.getSerializableExtra("OsobaPacijent") as Osoba

        val requestBody = FormBody.Builder()
            .add("glukozaukrvi", glukozaUKrvi)
            .add("krvnitlak", krvniTlak)
            .add("osobaPacijentId", osobaPacijent.id.toString())
            .build()

        val request = Request.Builder()
            .url(urlMobileSaveNewVitals)
            .post(requestBody)
            .build()

        newVitalsThread = Thread {
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
                            val vitali: Vitali? =
                                gson.fromJson(responseBody!!.string(), Vitali::class.java)

                            println("VITALI$vitali")

                            openVitalsActivity(
                                osobaPacijent,
                                intentVitalsActivity
                            )
                        }
                        println(response)
                    }
                })
                println(result)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        newVitalsThread.start()
    }

    private fun openVitalsActivity(osobaPacijent: Osoba, intentVitalsActivity: Intent) {
        intentVitalsActivity.putExtra("OsobaPacijent", osobaPacijent)
        podsjetnikList = intent.getParcelableArrayListExtra("PodsjetnikList")!!
        intentVitalsActivity.putExtra("PodsjetnikList", ArrayList(podsjetnikList))

        val urlMobileVitals = "http://${UrlProperties.IP_ADDRESS}:8080/mobileVitals"
        loadRemindersIntoList(urlMobileVitals, osobaPacijent, intentVitalsActivity)
    }

    private fun loadRemindersIntoList(
        urlMobileVitals: String,
        osobaPacijent: Osoba,
        intentVitalsActivity: Intent
    ) {
        val client = OkHttpClient()

        val json = Gson().toJson(osobaPacijent)
        println(json)
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json)

        val request = Request.Builder()
            .url(urlMobileVitals)
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

    private fun goBackToReminderActivity(osobaPacijent: Osoba, intentVitalsActivity: Intent) {
        podsjetnikList = intent.getParcelableArrayListExtra("PodsjetnikList")!!
        vitaliList = intent.getParcelableArrayListExtra("VitaliList")!!
        intentVitalsActivity.putExtra("PodsjetnikList", ArrayList(podsjetnikList))
        intentVitalsActivity.putExtra("OsobaPacijent", osobaPacijent)
        intentVitalsActivity.putExtra("VitaliList", ArrayList(vitaliList))
        startActivity(intentVitalsActivity)
    }
}