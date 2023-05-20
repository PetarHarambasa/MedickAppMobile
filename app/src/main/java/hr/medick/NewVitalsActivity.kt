package hr.medick

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import hr.medick.HostActivity.Companion.listOfVitals
import hr.medick.HostActivity.Companion.session
import hr.medick.databinding.ActivityNewVitalsBinding
import hr.medick.model.Osoba
import hr.medick.model.Vitali
import hr.medick.properties.UrlProperties
import okhttp3.*
import java.io.IOException

class NewVitalsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewVitalsBinding
    private var newVitalsThread = Thread()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewVitalsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabBack.setOnClickListener {
            finish()
        }

        binding.addNewVitals.setOnClickListener {
            val krvniTlak = binding.krvniTlakEditText.text
            val glukozaUKrvi = binding.glukozaUKrviEditText.text

            val urlMobileSaveNewVitals =
                "http://${UrlProperties.IP_ADDRESS}:8080/mobileSaveNewVitals"

            saveNewVitals(
                urlMobileSaveNewVitals,
                krvniTlak.toString(),
                glukozaUKrvi.toString()
            )
        }
    }

    private fun saveNewVitals(
        urlMobileSaveNewVitals: String,
        krvniTlak: String,
        glukozaUKrvi: String
    ) {
        val client = OkHttpClient()

        val osobaPacijent: Osoba = session.getOsoba()

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

                            reloadListVitals()

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

    private fun reloadListVitals() {
        val hostActivity = Intent(baseContext, HostActivity::class.java)
        hostActivity.action = "ACTION_CALL_FUNCTION"
        startActivityForResult(hostActivity, 2)
    }
}