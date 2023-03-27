package hr.medick

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import hr.medick.databinding.ActivityRegisterBinding
import hr.medick.model.Osoba
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val ime = binding.imeEditText.text
            val prezime = binding.prezimeEditText.text
            val email = binding.emailEditText.text
            val telefon = binding.telefonEditText.text
            val adresaStanovanja = binding.adresaStanovanjaEditText.text
            val lozinka = binding.lozinkaEditText.text
            val ponovljenaLozinka = binding.ponovnoLozinkaEditText.text


            // ip of device that is vrtiti backend
            // žično ipadesa: 192.168.0.126 žično ne radi
            // bežično ipadrsa: 192.168.1.3 radi ko mašina
            val url = "http://192.168.1.3:8080/mobileRegister"
            val osoba = Osoba(
                null,
                ime.toString(),
                prezime.toString(),
                email.toString(),
                telefon.toString(),
                adresaStanovanja.toString(),
                lozinka.toString()
            )

            // Validate user input
            if (ime.isEmpty() || prezime.isEmpty() || email.isEmpty() || telefon.isEmpty() || adresaStanovanja.isEmpty() || lozinka.isEmpty() || ponovljenaLozinka.isEmpty()) {
                Toast.makeText(this, "Molim, popunite sva polja", Toast.LENGTH_SHORT).show()
            } else if (!email.contains("@")) {
                Toast.makeText(this, "Molim, upisite svoj email", Toast.LENGTH_SHORT).show()
            } else if (lozinka.trim() != ponovljenaLozinka.trim()) {
                Toast.makeText(this, "Molim, potvrdite lozinku", Toast.LENGTH_SHORT).show()
            } else {
                saveData(url, osoba)
                clearInputs()
            }
        }
    }

    private fun saveData(url: String, osoba: Osoba) {
        val client = OkHttpClient()

        val json = Gson().toJson(osoba)
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()


        val gfgThread = Thread {
            try {
                // Your network activity
                val result = client.newCall(request).enqueue(object : Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        println(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        println(response)
                    }
                })
                println(result)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        gfgThread.start()
    }

    fun clearInputs() {
        binding.imeEditText.text.clear()
        binding.prezimeEditText.text.clear()
        binding.emailEditText.text.clear()
        binding.telefonEditText.text.clear()
        binding.adresaStanovanjaEditText.text.clear()
        binding.lozinkaEditText.text.clear()
        binding.ponovnoLozinkaEditText.text.clear()
    }
}