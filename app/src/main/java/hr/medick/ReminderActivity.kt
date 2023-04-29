package hr.medick

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.medick.adapter.PodsjetnikAdapter
import hr.medick.databinding.ActivityReminderBinding
import hr.medick.model.Osoba
import hr.medick.model.Podsjetnik
import hr.medick.model.Vitali
import hr.medick.properties.UrlProperties
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.lang.reflect.Type


class ReminderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReminderBinding

    var vitaliList: List<Vitali> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderBinding.inflate(layoutInflater)

        val listOfPodsjetniks: List<Podsjetnik> =
            intent.getParcelableArrayListExtra("PodsjetnikList")!!

        val listView: ListView = binding.lvPodsjetnici
        setContentView(binding.root)

        binding.fabNew.setOnClickListener {
            openNewReminderActivity(listOfPodsjetniks)
        }

        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bootomnNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.podsjetnikList ->
                        return@OnNavigationItemSelectedListener true
                    R.id.vitaliList ->
                        openVitaliActivity(listOfPodsjetniks)
                }
                false
            })

        println("Treci$listOfPodsjetniks")
        setUpListView(listView, listOfPodsjetniks)

    }

    private fun openVitaliActivity(listOfPodsjetniks: List<Podsjetnik>) {
        val osoba: Osoba = intent.getSerializableExtra("OsobaPacijent") as Osoba
        val intentVitals = Intent(this, VitalsActivity::class.java)
        val urlMobileReminders = "http://${UrlProperties.IP_ADDRESS}:8080/mobileVitals"
        intentVitals.putExtra("OsobaPacijent", osoba)
        intentVitals.putExtra("PodsjetnikList", ArrayList(listOfPodsjetniks))
        loadVitalsIntoList(urlMobileReminders, osoba, intentVitals)
    }

    private fun setUpListView(listView: ListView, listOfPodsjetniks: List<Podsjetnik>) {
        val adapter = PodsjetnikAdapter(this, listOfPodsjetniks)
        listView.adapter = adapter
    }

    private fun openNewReminderActivity(listOfPodsjetniks: List<Podsjetnik>) {
        val osoba: Osoba = intent.getSerializableExtra("OsobaPacijent") as Osoba
        val intent = Intent(this, NewReminderActivity::class.java)
        println("Osoba:$osoba")
        println("listOfPodsjetniks:$listOfPodsjetniks")
        intent.putExtra("OsobaPacijent", osoba)
        intent.putExtra("PodsjetnikList", ArrayList(listOfPodsjetniks))

        startActivity(intent)
    }

    private fun loadVitalsIntoList(
        url: String,
        osoba: Osoba,
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