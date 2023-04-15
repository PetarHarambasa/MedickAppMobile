package hr.medick

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import hr.medick.adapter.PodsjetnikAdapter
import hr.medick.databinding.ActivityReminderBinding
import hr.medick.model.Osoba
import hr.medick.model.Podsjetnik


class ReminderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReminderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderBinding.inflate(layoutInflater)

        val listOfPodsjetniks: List<Podsjetnik> =
            intent.getParcelableArrayListExtra("PodsjetnikList")!!

        val listView: ListView = binding.lvPodsjetnici
        setContentView(binding.root)

        binding.fabNew.setOnClickListener {
            openNewReminderActivity()
        }


        println("Treci$listOfPodsjetniks")
        setUpListView(listView, listOfPodsjetniks)

    }

    private fun setUpListView(listView: ListView, listOfPodsjetniks: List<Podsjetnik>) {
        val adapter = PodsjetnikAdapter(this, listOfPodsjetniks)
        listView.adapter = adapter
    }

    private fun openNewReminderActivity() {
        val osoba: Osoba = intent.getSerializableExtra("OsobaPacijent") as Osoba
        val intent = Intent(this, NewReminderActivity::class.java)
        println("Osoba:$osoba")
        intent.putExtra("OsobaPacijent", osoba)
        startActivity(intent)
    }
}