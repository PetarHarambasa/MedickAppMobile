package hr.medick.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import hr.medick.R
import hr.medick.VitalsActivity
import hr.medick.model.Vitali

class VitaliAdapter(
    private val context: VitalsActivity,
    private val dataSource: List<Vitali>
) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

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

        val rowView = inflater.inflate(R.layout.list_item_vitali, parent, false)

        val glukozaUKrviInList = rowView.findViewById<TextView>(R.id.glukozaUKrviTextView)
        val krvniTlakInList = rowView.findViewById<TextView>(R.id.krvniTlakTextView)
        val datumMjerenjaInList = rowView.findViewById<TextView>(R.id.datumMjerenjaTextView)

        val vitali = getItem(position) as Vitali
        glukozaUKrviInList.text = "Glukoza u krvi: " + vitali.glukozaukrvi
        krvniTlakInList.text = "Krvni tlak: " + vitali.krvnitlak
        datumMjerenjaInList.text = "Datum mjerenja: " + vitali.datummjerenja.toString()

        return rowView

    }
}