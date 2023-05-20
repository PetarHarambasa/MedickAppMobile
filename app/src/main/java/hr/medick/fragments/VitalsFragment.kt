package hr.medick.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import hr.medick.HostActivity.Companion.vitalsAdapter
import hr.medick.NewVitalsActivity
import hr.medick.databinding.FragmentVitalsBinding

class VitalsFragment : Fragment() {
    private lateinit var binding: FragmentVitalsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVitalsBinding.inflate(inflater, container, false)
        iniComponents()

        val listView: ListView = binding.lvVitali

        setUpListView(listView)

        return binding.root
    }

    private fun setUpListView(listView: ListView) {
        listView.adapter = vitalsAdapter
    }

    private fun iniComponents() {
        binding.fabNew.setOnClickListener {
            openNewVitalsActivity()
        }
    }

    private fun openNewVitalsActivity() {
        val intent = Intent(activity, NewVitalsActivity::class.java)
        startActivity(intent)
    }
}