package hr.medick.fragments.reminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import hr.medick.R
import hr.medick.databinding.FragmentInteravalOfConsumptionBinding
import hr.medick.databinding.FragmentOtherIntervalBinding

class OtherIntervalFragment : Fragment() {

    private lateinit var binding: FragmentOtherIntervalBinding
    private lateinit var backBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtherIntervalBinding.inflate(inflater, container, false)
        iniComponents()

        return binding.root
    }

    private fun iniComponents() {
        backBtn = binding.backBtn

        backBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}