package hr.medick.fragments.reminder

import android.app.TimePickerDialog
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import hr.medick.R
import hr.medick.databinding.FragmentHowManyDailyBinding
import java.util.*

class HowManyDailyFragment : Fragment() { // TODO: sakri nepotrebne stvari iz layouta i nastavi s ostalim fragmentima

    private lateinit var binding: FragmentHowManyDailyBinding
    private lateinit var dailyBtn: Button
    private lateinit var moreDailyBtn: Button
    private lateinit var backBtn: Button
    private lateinit var dailyTimePicker: EditText
    private lateinit var intervalTimePicker: EditText
    private lateinit var firstPillTimePicker: EditText
    private lateinit var oneDailyLinearLayout: LinearLayout
    private lateinit var moreDailyLinearLayout: LinearLayout
    private lateinit var mainLinearLayout: LinearLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHowManyDailyBinding.inflate(inflater, container, false)
        iniComponents()

        return binding.root
    }

    private fun iniComponents() {
        dailyTimePicker = binding.dailyTimePicker
        intervalTimePicker = binding.intervalTimePicker
        firstPillTimePicker = binding.firstPillTimePicker
        dailyBtn = binding.dailyBtn
        moreDailyBtn = binding.moreDailyBtn
        backBtn = binding.backBtn
        oneDailyLinearLayout = binding.oneDailyLinearLayout
        moreDailyLinearLayout = binding.moreDailyLinearLayout
        mainLinearLayout = binding.mainLinearLayout

        dailyTimePicker.setOnClickListener {
            showDatePickerDialog(dailyTimePicker)
        }
        intervalTimePicker.setOnClickListener {
            showDatePickerDialog(intervalTimePicker)
        }
        firstPillTimePicker.setOnClickListener {
            showDatePickerDialog(firstPillTimePicker)
        }

        dailyBtn.setOnClickListener{
            mainLinearLayout.visibility = View.GONE
            oneDailyLinearLayout.visibility = View.VISIBLE
        }
        moreDailyBtn.setOnClickListener{
            mainLinearLayout.visibility = View.GONE
            moreDailyLinearLayout.visibility = View.VISIBLE
        }
        backBtn.setOnClickListener{
            parentFragmentManager.popBackStack()
        }
    }

    private fun showDatePickerDialog(timePicker: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(  //TODO: usporedi s petrovim
            requireContext(),
            {view, hour, minute ->
                val selectTime = "$hour:$minute"
                timePicker.setText(selectTime)
            }, hour, minute, true
        )

        timePickerDialog.show()
    }



}