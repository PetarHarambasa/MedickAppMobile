package hr.medick.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import hr.medick.R
import hr.medick.model.Vitali
import java.text.SimpleDateFormat
import java.util.*

class VitaliAdapter(
    private val dataSource: List<Vitali>
) : BaseAdapter() {

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("SetTextI18n", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val rowView = LayoutInflater.from(parent?.context)
            .inflate(R.layout.list_item_vitali, parent, false)

        val glukozaUKrviInList = rowView.findViewById<TextView>(R.id.glukozaUKrviTextView)
        val krvniTlakInList = rowView.findViewById<TextView>(R.id.krvniTlakTextView)
        val datumMjerenjaInList = rowView.findViewById<TextView>(R.id.datumMjerenjaTextView)

        val vitali = getItem(position) as Vitali

        val inputDateStr = vitali.datummjerenja.toString()
        val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
        val inputDate: Date = inputFormat.parse(inputDateStr)!!

        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val outputDateStr: String = outputFormat.format(inputDate)

        glukozaUKrviInList.text = "Glukoza u krvi: " + vitali.glukozaukrvi
        krvniTlakInList.text = "Krvni tlak: " + vitali.krvnitlak
        datumMjerenjaInList.text = "Datum mjerenja: $outputDateStr"

        return rowView

    }
}