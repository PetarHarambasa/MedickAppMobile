package hr.medick


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.medick.databinding.ActivityLoginBinding
import hr.medick.model.Osoba
import hr.medick.model.Podsjetnik
import hr.medick.properties.UrlProperties.IP_ADDRESS
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.io.Serializable
import java.lang.reflect.Type


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var loginThread = Thread()
    var currentOsoba: Osoba? = null

    var podsjetnikList: List<Podsjetnik> = ArrayList()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goToRegister.setOnClickListener() {
            openRegisterActivity()
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text
            val lozinka = binding.lozinkaEditText.text

            // ip of device that is vrtiti backend
            // žično ipadesa: 192.168.0.126 žično ne radi
            // bežično ipadrsa: 192.168.1.3 radi ko mašina
            // ipadrsa za emulator: 10.0.2.2
            // i guess dok publishamo backend na neki server stavljamo ip od servera
            // val url = "http://192.168.1.3:8080/mobileRegister" ili val url = "{ngrok link}/mobileRegister"
            val urlMobileLogin = "http://$IP_ADDRESS:8080/mobileLogin"

            if (email.isEmpty() || lozinka.isEmpty()) {
                Toast.makeText(this, "Molim, popunite sva polja", Toast.LENGTH_SHORT).show()
            } else if (!email.contains("@")) {
                Toast.makeText(this, "Molim, upisite ispravan email", Toast.LENGTH_SHORT).show()
            } else {
                confirmLogin(urlMobileLogin, email.toString(), lozinka.toString())
                clearInputs()
            }
        }
    }

    private fun clearInputs() {
        binding.emailEditText.text.clear()
        binding.lozinkaEditText.text.clear()
    }

    private fun confirmLogin(url: String, email: String, lozinka: String) {

        val intentReminderActivity = Intent(this, ReminderActivity::class.java)

        val client = OkHttpClient()

        val requestBody = FormBody.Builder().add("email", email).add("lozinka", lozinka).build()

        val request = Request.Builder().url(url).post(requestBody).build()
        loginThread = Thread {
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
                            val osoba: Osoba? =
                                gson.fromJson(responseBody!!.string(), Osoba::class.java)
                            println(osoba)
                            if (!osoba?.email.isNullOrBlank()) {
                                currentOsoba = osoba
                                openReminderActivity(intentReminderActivity)
                            }
                        }
                        println(response)
                    }
                })
                println(result)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        loginThread.start()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun loadRemindersIntoList(
        url: String,
        osoba: Osoba,
        intentReminderActivity: Intent,
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

                            val type: Type = object : TypeToken<List<Podsjetnik?>?>() {}.type
                            podsjetnikList = gson.fromJson(responseBody!!.string(), type)
                            println("DrugiPustlIst$podsjetnikList")
                            addPodsjtenikListToIntentExtra(intentReminderActivity)
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
        intentReminderActivity.putExtra("PodsjetnikList", ArrayList(podsjetnikList))
        println("Curent osoba:$currentOsoba")

        startActivity(intentReminderActivity)
    }

    private fun openRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun openReminderActivity(intentReminderActivity: Intent) {
        val urlMobileReminders = "http://${IP_ADDRESS}:8080/mobileReminders"
        loadRemindersIntoList(urlMobileReminders, currentOsoba!!, intentReminderActivity)

        intentReminderActivity.putExtra("OsobaPacijent", currentOsoba)
        println("Curent osoba:$currentOsoba")
    }
}
