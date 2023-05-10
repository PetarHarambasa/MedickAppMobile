package hr.medick.fragments.reminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import hr.medick.R
import hr.medick.databinding.FragmentDoseBinding
import hr.medick.databinding.FragmentMedicationNameBinding
import hr.medick.model.Terapija


class DoseFragment : Fragment(){
    private lateinit var binding: FragmentDoseBinding
    private lateinit var nextBtn: Button
    private lateinit var backBtn: Button
    private lateinit var doseTxt: EditText
    private lateinit var metricSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDoseBinding.inflate(inflater, container, false)
        iniComponents()

        return binding.root
    }

    private fun iniComponents() {
        nextBtn = binding.nextBtn
        backBtn = binding.backBtn
        doseTxt = binding.medicationDose
        metricSpinner = binding.doseMetricSpinner

        nextBtn.setOnClickListener {
            if (validateInput()) {
//                Terapija.getInstance().lijek?.naziv = name.text.trim().toString()

//                parentFragmentManager.commit {
//                    replace<InteravalOfConsumptionFragment>(R.id.fragmentContianer)
//                    setReorderingAllowed(true)
//                    addToBackStack(null)
//                }
            }

        }
        backBtn.setOnClickListener {
//                parentFragmentManager.commit {
//                    replace<MedicationNameFragment>(R.id.fragmentContianer)
//                    setReorderingAllowed(true)
//                    addToBackStack(null)
//                }
        }
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.DoseMetrics,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            metricSpinner.adapter = adapter
        }
    }

    private fun validateInput(): Boolean {
        if (doseTxt.text.isNullOrBlank()) {
            doseTxt.error = "Polje ne smije biti prazno."
            return false
        }
        return true
    }

}