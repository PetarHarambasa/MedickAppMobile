package hr.medick.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import hr.medick.HostActivity.Companion.podsjetnikAdapter
import hr.medick.NewReminderActivity
import hr.medick.databinding.FragmentReminderBinding


class ReminderFragment : Fragment() {

    private lateinit var binding: FragmentReminderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReminderBinding.inflate(inflater, container, false)
        iniComponents()

        val listView: ListView = binding.lvPodsjetnici

        setUpListView(listView)

        return binding.root
    }

    private fun setUpListView(listView: ListView) {
        listView.adapter = podsjetnikAdapter
    }

    private fun iniComponents() {
        binding.fabNew.setOnClickListener {
            openNewReminderActivity()
        }
    }

    private fun openNewReminderActivity() {
        val intent = Intent(activity, NewReminderActivity::class.java)
        startActivity(intent)
    }
}