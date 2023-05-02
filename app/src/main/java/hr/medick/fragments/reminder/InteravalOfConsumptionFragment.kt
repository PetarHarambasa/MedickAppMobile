package hr.medick.fragments.reminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import hr.medick.R
import hr.medick.databinding.FragmentInteravalOfConsumptionBinding
import hr.medick.databinding.FragmentMedicationNameBinding

class InteravalOfConsumptionFragment : Fragment() {

    private lateinit var binding: FragmentInteravalOfConsumptionBinding
    private lateinit var dailyBtn: Button
    private lateinit var otherIntervBtn: Button
    private lateinit var backBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInteravalOfConsumptionBinding.inflate(inflater, container, false)
        iniComponents()

        return binding.root
    }

    private fun iniComponents() {
        backBtn = binding.backBtn
        dailyBtn = binding.dailyBtn
        otherIntervBtn = binding.otherIntervalBtn

        backBtn.setOnClickListener {
            parentFragmentManager.commit {
                replace<MedicationNameFragment>(R.id.fragmentContianer)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
    }

}