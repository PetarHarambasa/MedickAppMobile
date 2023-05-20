package hr.medick.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.gson.Gson
import hr.medick.HostActivity
import hr.medick.HostActivity.Companion.osoba
import hr.medick.HostActivity.Companion.session
import hr.medick.LoginActivity
import hr.medick.NewReminderActivity
import hr.medick.R
import hr.medick.databinding.FragmentProfileBinding
import hr.medick.databinding.FragmentReminderBinding
import hr.medick.dto.ProfileDto
import hr.medick.model.Osoba
import hr.medick.model.Podsjetnik
import hr.medick.model.Vitali
import hr.medick.properties.UrlProperties
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var logoutBtn: Button
    private lateinit var profileOsoba: Osoba
    var emailChanged: Boolean = false
    private var isEditSuccessful: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        initVariables()
        initComponents()

        return binding.root
    }

    private fun initVariables() {
        profileOsoba = osoba
    }

    private fun initComponents() {
        logoutBtn = binding.logoutBtn

        binding.imeEditText.setText(osoba.ime)
        binding.prezimeEditText.setText(osoba.prezime)
        binding.emailEditText.setText(osoba.email)
        binding.telefonEditText.setText(osoba.telefon)
        binding.adresaStanovanjaEditText.setText(osoba.adresaStanovanja)

        binding.emailEditText.addTextChangedListener {
            emailChanged = true
        }

        binding.saveUserProfile.setOnClickListener {
            saveProfileChanges()
        }

        logoutBtn.setOnClickListener{
            session.logout()

            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }
    }

    private fun saveProfileChanges() {
        val ime = binding.imeEditText.text
        val prezime = binding.prezimeEditText.text
        val email = binding.emailEditText.text.toString()
        val telefon = binding.telefonEditText.text
        val adresaStanovanja = binding.adresaStanovanjaEditText.text
        val lozinka = binding.lozinkaEditText.text

        val url = "http://${UrlProperties.IP_ADDRESS}:8080/mobileEditPacijent"
        val newDataOfOsoba = Osoba(
            osoba.id,
            ime.toString(),
            prezime.toString(),
            email,
            telefon.toString(),
            adresaStanovanja.toString(),
            lozinka.toString()
        )

        // Validate user input
        if (ime.isEmpty() || prezime.isEmpty() || email.isEmpty() || telefon.isEmpty() || adresaStanovanja.isEmpty() || lozinka.isEmpty()) {
            Toast.makeText(context, "Molim, popunite sva polja", Toast.LENGTH_SHORT).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Molim, upisite ispravan email", Toast.LENGTH_SHORT).show()
        } else {
            val pd = ProfileDto(newDataOfOsoba, emailChanged)
            saveData(url, pd)
            if (isEditSuccessful) {
                Toast.makeText(
                    context, "Spremljene promjene, molim ponovno se" +
                            " ulogirajte kako bi se promjene primjenile ", Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(context, "Email već postoji!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveData(url: String, profileDto: ProfileDto) {
        val client = OkHttpClient()

        val json = Gson().toJson(profileDto)
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
                        session.setUser(profileDto.osoba)
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

}