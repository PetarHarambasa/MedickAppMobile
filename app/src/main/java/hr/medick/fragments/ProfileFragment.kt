package hr.medick.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import hr.medick.HostActivity
import hr.medick.LoginActivity
import hr.medick.NewReminderActivity
import hr.medick.R
import hr.medick.databinding.FragmentProfileBinding
import hr.medick.databinding.FragmentReminderBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var logoutBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        iniComponents()

        return binding.root
    }

    private fun iniComponents() {
        logoutBtn = binding.logoutBtn
        logoutBtn.setOnClickListener{
            HostActivity.session.logout()

            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }
    }

}