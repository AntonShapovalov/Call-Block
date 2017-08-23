package ru.org.adons.cblock.ui.view.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item_block.view.*
import ru.org.adons.cblock.R
import ru.org.adons.cblock.data.BlockListItem
import ru.org.adons.cblock.ext.formatPhoneNumber
import ru.org.adons.cblock.ext.getDescription
import java.util.*

/**
 * Recycler adapter for Block list
 */
class BlockListAdapter(private val deleteItem: (Long) -> Unit) : RecyclerView.Adapter<BlockListAdapter.ViewHolder>() {

    private val items = ArrayList<BlockListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_block, parent, false)
        return BlockListAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val view = holder.itemView
        view.textPhone.text = item.phoneNumber.formatPhoneNumber()
        view.textDesc.text = item.date.getDescription(item.name)
        view.buttonDelete.setOnClickListener { deleteItem(item.id) }
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<BlockListItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
