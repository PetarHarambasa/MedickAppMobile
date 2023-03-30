package hr.medick


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import hr.medick.databinding.ActivityLoginBinding
import hr.medick.model.Osoba
import okhttp3.*
import java.io.IOException


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var loginThread = Thread()

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
            val url = "http://192.168.1.3:8080/mobileLogin"

            if (email.isEmpty() || lozinka.isEmpty()) {
                Toast.makeText(this, "Molim, popunite sva polja", Toast.LENGTH_SHORT).show()
            } else if (!email.contains("@")) {
                Toast.makeText(this, "Molim, upisite ispravan email", Toast.LENGTH_SHORT).show()
            } else {
                confirmLogin(url, email.toString(), lozinka.toString())
                binding.responseMessage.text = "Prijava neuspješna!"
                clearInputs()
            }
        }
    }

    private fun clearInputs() {
        binding.emailEditText.text.clear()
        binding.lozinkaEditText.text.clear()
    }

    private fun confirmLogin(url: String, email: String, lozinka: String) {
        val client = OkHttpClient()

        val requestBody = FormBody.Builder()
            .add("email", email)
            .add("lozinka", lozinka)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
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
                                openMainActivity()
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

    private fun openRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}