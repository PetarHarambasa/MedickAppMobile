package hr.medick.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import hr.medick.R
import hr.medick.model.Podsjetnik

class PodsjetnikAdapter(
    private val dataSource: List<Podsjetnik>
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
            .inflate(R.layout.list_item_podsjetnik, parent, false)

        val lijekNazivInList = rowView.findViewById<TextView>(R.id.imeLijekaTextViewInListView)
        val lijekDozaInList = rowView.findViewById<TextView>(R.id.dozaLijekaTextViewInListView)
        val brojTabletaInList = rowView.findViewById<TextView>(R.id.brojTabletaTextViewInListView)
        val brojPutaDnevnoInList =
            rowView.findViewById<TextView>(R.id.brojPutaDnevnoTextViewInListView)
        val brojSvakihSatiInList =
            rowView.findViewById<TextView>(R.id.brojSvakihSatiTextViewInListView)

        if (getItem(position) != null) {
            val podsjetnik = getItem(position) as Podsjetnik
            lijekNazivInList.text = podsjetnik.terapija?.lijek?.naziv + ","
            lijekDozaInList.text = podsjetnik.terapija?.dozalijeka
            brojTabletaInList.text =
                "Preostalo " + podsjetnik.terapija?.kolicinatableta.toString() + " tableta, "
            brojSvakihSatiInList.text =
                "svakih " + podsjetnik.terapija?.ponavljanja.toString() + "h"
            brojPutaDnevnoInList.text =
                podsjetnik.terapija?.kolicinadnevno.toString() + " puta dnevno, "
        }
        return rowView

    }
}