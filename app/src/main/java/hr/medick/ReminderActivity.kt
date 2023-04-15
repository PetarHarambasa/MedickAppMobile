package hr.medick

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.medick.databinding.ActivityReminderBinding
import hr.medick.model.Osoba

class ReminderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReminderBinding
    private var reminderThread = Thread()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabNew.setOnClickListener{
            openNewReminderActivity()
        }
    }

    private fun openNewReminderActivity(){
        val osoba : Osoba = intent.getSerializableExtra("OsobaPacijent") as Osoba
        val intent = Intent(this, NewReminderActivity::class.java)
        println("Osoba:$osoba")
        intent.putExtra("OsobaPacijent", osoba)
        startActivity(intent)
    }
}