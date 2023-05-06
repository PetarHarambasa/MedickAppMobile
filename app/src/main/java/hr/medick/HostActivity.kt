package hr.medick

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.medick.databinding.ActivityHostBinding
import hr.medick.model.Osoba
import hr.medick.model.Podsjetnik
import hr.medick.model.Vitali
import hr.medick.properties.UrlProperties
import hr.medick.session.Session
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.lang.reflect.Type

class HostActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHostBinding

    companion object{
        lateinit var listOfPodsjetniks: List<Podsjetnik>
        lateinit var listOfVitals: List<Vitali>
        lateinit var session: Session
        lateinit var osoba: Osoba
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fm: FragmentManager = supportFragmentManager
//        osoba = intent.getSerializableExtra("OsobaPacijent") as Osoba
        session = Session(this)
        osoba = session.getOsoba()
        initNavigation()

        loadRemindersIntoList("http://${UrlProperties.IP_ADDRESS}:8080/mobileReminders", osoba)
        loadVitalsIntoList("http://${UrlProperties.IP_ADDRESS}:8080/mobileVitals", osoba)

//        listOfPodsjetniks = intent.getParcelableArrayListExtra("PodsjetnikList")!!

//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }

    private fun initNavigation() {
        val navController = Navigation.findNavController(this, R.id.navController)
        NavigationUI.setupWithNavController(binding.bootomnNavigation, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_host)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun loadVitalsIntoList(
        url: String,
        osoba: Osoba
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
                        listOfVitals = gson.fromJson(responseBody!!.string(), type)
                    }
                    println(response)
                }

            })
            println(result)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun loadRemindersIntoList(
        url: String,
        osoba: Osoba
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
                        listOfPodsjetniks = gson.fromJson(responseBody!!.string(), type)
                        println("DrugiPustlIst$listOfPodsjetniks")
                    }
                    println(response)
                }

            })
            println(result)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}