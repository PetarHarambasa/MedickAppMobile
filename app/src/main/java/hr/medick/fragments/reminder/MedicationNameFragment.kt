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
import hr.medick.databinding.FragmentMedicationNameBinding
import hr.medick.model.Terapija

class MedicationNameFragment : Fragment() {
    private lateinit var binding: FragmentMedicationNameBinding
    private lateinit var nextBtn: Button
    private lateinit var name: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMedicationNameBinding.inflate(inflater, container, false)
        iniComponents()

        return binding.root
    }

    private fun iniComponents() {
        nextBtn = binding.nextBtn
        name = binding.medicationName
        nextBtn.setOnClickListener {
            if (validateInput()) {
                Terapija.getInstance().lijek?.naziv = name.text.trim().toString()

                parentFragmentManager.commit {
                    replace<DoseFragment>(R.id.fragmentContianer)
                    setReorderingAllowed(true)
                    addToBackStack(null)
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        if (name.text.isNullOrBlank()) {
            name.error = "Polje ne smije biti prazno."
            return false
        }
        return true
    }
}