package hr.medick.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import hr.medick.R
import hr.medick.ReminderActivity
import hr.medick.model.Podsjetnik

class PodsjetnikAdapter(private val context: ReminderActivity,
                        private val dataSource: List<Podsjetnik>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val rowView = inflater.inflate(R.layout.list_item_podsjetnik, parent, false)

        val lijekNazivInList = rowView.findViewById<TextView>(R.id.imeLijekaTextViewInListView)

        val podsjetnik = getItem(position) as Podsjetnik
        lijekNazivInList.text = podsjetnik.terapija?.lijek?.naziv

        return rowView

    }
}