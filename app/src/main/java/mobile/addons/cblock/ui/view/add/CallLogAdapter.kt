package mobile.addons.cblock.ui.view.add

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import kotlinx.android.synthetic.main.list_item_call_log.view.*
import mobile.addons.cblock.R
import mobile.addons.cblock.data.CallLogItem
import mobile.addons.cblock.ext.formatPhoneNumber
import mobile.addons.cblock.ext.getDescription
import java.util.*

/**
 * Recycler adapter for Call Log list
 */
class CallLogAdapter : RecyclerView.Adapter<CallLogAdapter.ViewHolder>() {

    private val items = ArrayList<CallLogItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_call_log, parent, false)
        return CallLogAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val view = holder.itemView
        view.textPhoneNumber.text = item.phoneNumber.formatPhoneNumber()
        view.textDescription.text = item.date.getDescription(item.name)
        // disable checkbox if item already in block list
        if (item.isBlocked) {
            view.checkBox.isEnabled = false
            view.checkBox.isChecked = true
            view.setOnClickListener(null)
            view.checkBox.setOnClickListener(null)
        } else {
            view.checkBox.isEnabled = true
            // set selected if list scrolled
            view.checkBox.isChecked = item.isSelected
            //
            view.checkBox.setOnClickListener { setSelected(item, view.checkBox) }
            view.setOnClickListener { setSelected(item, view.checkBox) }
        }
    }

    override fun getItemCount(): Int = items.size

    internal fun setItems(newItems: List<CallLogItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    internal fun getItems(): List<CallLogItem> = items

    private fun setSelected(item: CallLogItem, checkBox: CheckBox) {
        item.isSelected = !item.isSelected
        checkBox.isChecked = item.isSelected
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
