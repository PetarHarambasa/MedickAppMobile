package hr.medick

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import hr.medick.adapter.VitaliAdapter
import hr.medick.databinding.ActivityVitalsBinding
import hr.medick.model.Osoba
import hr.medick.model.Podsjetnik
import hr.medick.model.Vitali

class VitalsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVitalsBinding
    var podsjetnikList: List<Podsjetnik> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVitalsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentReminder = Intent(this, ReminderActivity::class.java)

        val listOfVitals: List<Vitali> =
            intent.getParcelableArrayListExtra("VitaliList")!!

        val listView: ListView = binding.lvVitali
        setContentView(binding.root)

        binding.fabNew.setOnClickListener {
            openNewVitalsActivity(listOfVitals)
        }

        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bootomnNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
//                    R.id.podsjetnikList ->
//                        openReminderActivity(intentReminder)
//                    R.id.vitaliList ->
//                        return@OnNavigationItemSelectedListener true
                }
                false
            })

        setUpListView(listView, listOfVitals)
    }

    private fun setUpListView(listView: ListView, listOfVitals: List<Vitali>) {
        val adapter = VitaliAdapter(this, listOfVitals)
        listView.adapter = adapter
    }

    private fun openNewVitalsActivity(listOfVitals: List<Vitali>) {
        val osoba: Osoba = intent.getSerializableExtra("OsobaPacijent") as Osoba
        val intentNewVitalsActivity = Intent(this, NewVitalsActivity::class.java)

        podsjetnikList = intent.getParcelableArrayListExtra("PodsjetnikList")!!
        intentNewVitalsActivity.putExtra("PodsjetnikList", ArrayList(podsjetnikList))
        intentNewVitalsActivity.putExtra("OsobaPacijent", osoba)
        intentNewVitalsActivity.putExtra("VitaliList", ArrayList(listOfVitals))
        startActivity(intentNewVitalsActivity)
    }

    private fun openReminderActivity(intentReminderActivity: Intent) {
        val osoba: Osoba = intent.getSerializableExtra("OsobaPacijent") as Osoba
        println("Vitali$osoba")
        intentReminderActivity.putExtra("OsobaPacijent", osoba)
        podsjetnikList = intent.getParcelableArrayListExtra("PodsjetnikList")!!
        intentReminderActivity.putExtra("PodsjetnikList", ArrayList(podsjetnikList))

        startActivity(intentReminderActivity)
    }
}