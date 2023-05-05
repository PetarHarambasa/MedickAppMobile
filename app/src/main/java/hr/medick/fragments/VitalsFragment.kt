package hr.medick.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.medick.HostActivity
import hr.medick.NewReminderActivity
import hr.medick.NewVitalsActivity
import hr.medick.databinding.FragmentVitalsBinding
import hr.medick.model.Osoba
import hr.medick.model.Vitali

class VitalsFragment : Fragment() {
    private lateinit var binding: FragmentVitalsBinding
    private lateinit var listOfVitals: List<Vitali>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVitalsBinding.inflate(inflater, container, false)
        iniComponents()

        listOfVitals = HostActivity.listOfVitals

        return binding.root
    }

    private fun iniComponents() {
        binding.fabNew.setOnClickListener {
            openNewVitalsActivity(listOfVitals)
        }
    }

    private fun openNewVitalsActivity(listOfVitals: List<Vitali>) {
//        val osoba: Osoba = intent.getSerializableExtra("OsobaPacijent") as Osoba
//        val intentNewVitalsActivity = Intent(this, NewVitalsActivity::class.java)
//
//        podsjetnikList = intent.getParcelableArrayListExtra("PodsjetnikList")!!
//        intentNewVitalsActivity.putExtra("PodsjetnikList", ArrayList(podsjetnikList))
//        intentNewVitalsActivity.putExtra("OsobaPacijent", osoba)
//        intentNewVitalsActivity.putExtra("VitaliList", ArrayList(listOfVitals))
        val intent = Intent(activity, NewVitalsActivity::class.java)
        startActivity(intent)
    }
}