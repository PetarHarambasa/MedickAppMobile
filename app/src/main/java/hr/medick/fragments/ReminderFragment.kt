package hr.medick.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hr.medick.HostActivity
import hr.medick.LoginActivity
import hr.medick.NewReminderActivity
import hr.medick.databinding.FragmentReminderBinding
import hr.medick.model.Podsjetnik
import hr.medick.model.Vitali


class ReminderFragment : Fragment() {

    private lateinit var binding: FragmentReminderBinding
    //var vitaliList: List<Vitali> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReminderBinding.inflate(inflater, container, false)
        iniComponents()

        return binding.root
    }

    private fun iniComponents() {
        binding.fabNew.setOnClickListener {
            openNewReminderActivity(HostActivity.listOfPodsjetniks)
        }
//        binding.logoutBtn.setOnClickListener{
//            HostActivity.session.logout()
//
//            startActivity(Intent(activity, LoginActivity::class.java))
//            activity?.finish()
//        }
    }

    private fun openNewReminderActivity(listOfPodsjetniks: List<Podsjetnik>) {
//        val osoba: Osoba = intent.getSerializableExtra("OsobaPacijent") as Osoba
//        val intent = Intent(this, NewReminderActivity::class.java)
//        println("Osoba:$osoba")
//        println("listOfPodsjetniks:$listOfPodsjetniks")
//        intent.putExtra("OsobaPacijent", osoba)
//        intent.putExtra("PodsjetnikList", ArrayList(listOfPodsjetniks))
        val intent = Intent(activity, NewReminderActivity::class.java)
        startActivity(intent)
    }
}